package com.feis.splitnings.features.split.service;

import com.feis.splitnings.common.exception.ConflictException;
import com.feis.splitnings.common.exception.ForbiddenOperationException;
import com.feis.splitnings.common.exception.ResourceNotFoundException;
import com.feis.splitnings.common.exception.ValidationException;
import com.feis.splitnings.common.exception.errors.Error;
import com.feis.splitnings.common.exception.enums.InternalErrorCode;
import com.feis.splitnings.common.service.AbstractService;
import com.feis.splitnings.features.account.data.dto.response.AccountDto;
import com.feis.splitnings.features.account.service.AccountService;
import com.feis.splitnings.features.split.data.Split;
import com.feis.splitnings.features.split.data.dto.request.SplitCreateDto;
import com.feis.splitnings.features.split.data.dto.request.SplitUpdateDto;
import com.feis.splitnings.features.split.data.dto.response.SplitDto;
import com.feis.splitnings.features.split.data.dto.response.SplitPageDto;
import com.feis.splitnings.features.split.mapper.SplitMapper;
import com.feis.splitnings.features.split.repository.SplitRepository;
import com.feis.splitnings.security.utils.SecurityUtils;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SplitService extends AbstractService<Split, SplitDto, SplitCreateDto, SplitUpdateDto, SplitPageDto, Integer> {
    private AccountService accountService;

    public SplitService(SplitMapper splitMapper, SplitRepository splitRepository, AccountService accountService) {
        this.mapper = splitMapper;
        this.repository = splitRepository;
        this.accountService = accountService;
        this.resourceName = "Split";
    }

    @Override
    protected void validateCreateDto(SplitCreateDto createDto) {
        // This checks whether the account is owned by the current user.
        // If not, the calling method will throw a `ResourceNotFoundException`
        accountService.getByIdAndUserId(createDto.getAccountId(), SecurityUtils.getJwtUserId());

        Optional<Split> split = ((SplitRepository) repository).findByNameAndAccountId(createDto.getName(), createDto.getAccountId());

        if (split.isPresent()) {
            String message = String.format("%s with name %s already existing for this account", resourceName, createDto.getName());
            Error error = new Error(InternalErrorCode.CONFLICT, message);
            throw new ConflictException(error);
        }

        validateSplitAllocation(createDto.getAccountId(), createDto.getSplitPercentage());
    }

    @Override
    protected void validateUpdateDto(SplitUpdateDto updateDto, Split existing) {
        if (existing.getIsDefault()) {
            throw new ForbiddenOperationException("You can't update the default split.");
        }

        Optional<Split> split = ((SplitRepository) repository).findByNameAndAccountId(updateDto.getName(), existing.getAccountId());

       if (split.isPresent() && !split.get().getId().equals(existing.getId())) {
            String message = String.format("%s with name %s already existing for this account", resourceName, updateDto.getName());
            Error error = new Error(InternalErrorCode.CONFLICT, message);
            throw new ConflictException(error);
        }

        Short updateSplitPercentage = updateDto.getSplitPercentage();
        Short existingSplitPercentage = updateDto.getSplitPercentage();

        if (!updateSplitPercentage.equals(existingSplitPercentage) || updateSplitPercentage > existingSplitPercentage) {
            validateSplitAllocation(existing.getAccountId(), updateDto.getSplitPercentage());
        }

    }

    @Override
    protected void validateDelete(Integer id) {
        Split split = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(resourceName, id.toString()));

        if (split.getIsDefault()) {
            throw new ForbiddenOperationException("You can't delete the default split.");
        }

        // This checks whether the account is owned by the current user.
        // If not, the calling method will throw a `ResourceNotFoundException`
        accountService.getByIdAndUserId(split.getAccountId(), SecurityUtils.getJwtUserId());
    }

    @Override
    protected Integer getResourceId(Split entity) {
        return entity.getId();
    }

    @Transactional
    @Override
    protected void doCreate(Split toCreate) {
        Integer jwtUserId = SecurityUtils.getJwtUserId();

        updateDefaultSplit(toCreate.getAccountId(), toCreate.getSplitPercentage(), false, jwtUserId.toString());

        AccountDto account = accountService.get(toCreate.getAccountId());
        double splitTheoreticalAmount = computeSplitTheoreticalAmount(account.getWealth(), toCreate.getSplitPercentage());
        toCreate.setAvailableAmount(0.00);
        toCreate.setTheoreticalAmount(splitTheoreticalAmount);

        toCreate.setActive(true);
        toCreate.setIsDefault(false);

        toCreate.setCreatedBy(jwtUserId.toString());
        toCreate.setModifiedBy(jwtUserId.toString());
    }

    @Transactional
    @Override
    protected void doUpdate(Split toUpdate, SplitUpdateDto updateDto) {
        Integer jwtUserId = SecurityUtils.getJwtUserId();

        // FIXME: It's not needed to decrease default split amount every time.
        // A fix for this might be to calculate the difference between the default split and the
        // updated split allocated percentage, and sum the difference to the current
        // default split amount (such value can also be negative of course).
        updateDefaultSplit(toUpdate.getAccountId(), updateDto.getSplitPercentage(), false, jwtUserId.toString());

        AccountDto account = accountService.get(toUpdate.getAccountId());
        double splitTheoreticalAmount = computeSplitTheoreticalAmount(account.getWealth(), updateDto.getSplitPercentage());
        toUpdate.setTheoreticalAmount(splitTheoreticalAmount);

        toUpdate.setModifiedBy(jwtUserId.toString());
    }

    @Transactional
    private void validateSplitAllocation(int accountId, int percentageToAllocate) {
        Split defaultSplit = ((SplitRepository) repository).findByAccountIdAndIsDefaultTrue(accountId);

        if (defaultSplit.getSplitPercentage() < percentageToAllocate) {
            String errorMessage = String.format("Cannot allocate the requested percentage for such split. Exceeds by %s%%", Math.abs(defaultSplit.getSplitPercentage() - percentageToAllocate));
            Error error = new Error(InternalErrorCode.PARAMETER_INVALID, errorMessage);
            throw new ValidationException(List.of(error));
        }
    }

    @Transactional
    @Override
    protected void doDelete(Split entity) {
        Integer jwtUserId = SecurityUtils.getJwtUserId();

        updateDefaultSplit(entity.getAccountId(), entity.getSplitPercentage(), true, jwtUserId.toString());

        entity.setModifiedBy(jwtUserId.toString());
    }

    private double computeSplitTheoreticalAmount(double accountWealth, short splitPercentage) {
        return (accountWealth * splitPercentage) / 100;
    }

    /**
     * Updates the default split for a specific account.
     * More specifically, updates the allocated percentage,
     * and its theoretical amount based on the updated percentage.
     *
     * @param accountId - the account id which default split belongs to
     * @param updateBy - the percentage amount to increase/decrease by
     * @param increasePercentage - whether to increase (`true`) or decrease (`false`)
     *  the allocated percentage by
     * @param auditor who triggered the default split update. This field generally matches
     *  the authenticated user making the web request
     */
    @Transactional
    private void updateDefaultSplit(int accountId, short updateBy, boolean increasePercentage, String auditor) {
        Split defaultSplit = ((SplitRepository) repository).findByAccountIdAndIsDefaultTrue(accountId);

        short updatedDefaultSplitPercentage = increasePercentage ?
                (short) (defaultSplit.getSplitPercentage() + updateBy)
                : (short) (defaultSplit.getSplitPercentage() - updateBy);

        defaultSplit.setSplitPercentage(updatedDefaultSplitPercentage);

        AccountDto accountDto = accountService.get(accountId);

        double updatedDefaultSplitTheoreticalAmount = computeSplitTheoreticalAmount(accountDto.getWealth(), updatedDefaultSplitPercentage);
        defaultSplit.setTheoreticalAmount(updatedDefaultSplitTheoreticalAmount);

        defaultSplit.setModifiedBy(auditor);
    }
}
