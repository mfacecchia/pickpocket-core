package com.feis.splitnings.features.split.service;

import com.feis.splitnings.common.exception.ConflictException;
import com.feis.splitnings.common.exception.ForbiddenOperationException;
import com.feis.splitnings.common.exception.ResourceNotFoundException;
import com.feis.splitnings.common.exception.ValidationException;
import com.feis.splitnings.common.exception.errors.Error;
import com.feis.splitnings.common.exception.enums.InternalErrorCode;
import com.feis.splitnings.common.service.AbstractService;
import com.feis.splitnings.features.account.data.Account;
import com.feis.splitnings.features.account.service.AccountReadService;
import com.feis.splitnings.features.goal.data.Goal;
import com.feis.splitnings.features.goal.service.GoalReadService;
import com.feis.splitnings.features.split.data.Split;
import com.feis.splitnings.features.split.data.dto.request.SplitCreateDto;
import com.feis.splitnings.features.split.data.dto.request.SplitUpdateDto;
import com.feis.splitnings.features.split.data.dto.response.SplitDto;
import com.feis.splitnings.features.split.data.dto.response.SplitPageDto;
import com.feis.splitnings.features.split.mapper.SplitMapper;
import com.feis.splitnings.features.split.repository.SplitRepository;
import com.feis.splitnings.features.split.utils.SplitUtils;
import com.feis.splitnings.security.utils.SecurityUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SplitService extends AbstractService<Split, SplitDto, SplitCreateDto, SplitUpdateDto, SplitPageDto, Integer> {
    private final AccountReadService accountReadService;
    private final SplitReadService splitReadService;
    private final GoalReadService goalReadService;

    private final static Logger logger = LogManager.getLogger(SplitService.class);

    public SplitService(SplitMapper splitMapper, SplitRepository splitRepository, AccountReadService accountReadService,
            SplitReadService splitReadService, GoalReadService goalReadService) {

        this.mapper = splitMapper;
        this.repository = splitRepository;
        this.accountReadService = accountReadService;
        this.splitReadService = splitReadService;
        this.goalReadService = goalReadService;
        this.resourceName = "Split";
    }

    @Transactional(rollbackFor = Exception.class)
    public SplitDto createDefaultSplit(Integer accountId, Double amount) {
        Optional<Split> defaultSplit = ((SplitRepository) repository).findByAccountIdAndIsDefaultTrue(accountId);

        if (defaultSplit.isPresent()) {
            logger.error(String.format("CreateDefaultSplit ::: A default split already exists for account id %s", accountId));

            String message = "A default split already exists for this account";
            Error error = new Error(InternalErrorCode.CONFLICT, message);
            throw new ConflictException(error);
        }

        Integer jwtUserId = SecurityUtils.getJwtUserId();

        Split toCreate = SplitUtils.buildDefaultSplit(amount, accountId);
        toCreate.setCreatedBy(jwtUserId.toString());
        toCreate.setModifiedBy(jwtUserId.toString());

        repository.save(toCreate);

        return convertToDto(toCreate);
    }

    /**
     * Updates theoretical && available amounts for all the
     * splits in an account based on the provided `topUpAmount`
     * considering defined percentages for each split.
     */
    @Transactional(rollbackFor = Exception.class)
    public void topUpByAmountAndAccountId(Integer accountId, Double topUpAmount) {
        accountReadService.getById(accountId);

        List<Split> accountSplits = splitReadService.getAllByAccountId(accountId);

        logger.info("TopUpByAmountAndAccountId ::: Updating amounts for {} splits from account {} by {}", accountSplits.size(), accountId, topUpAmount);

        accountSplits.forEach((split) -> {
            Double splitPreviousTheoreticalAmount = split.getTheoreticalAmount();
            Double splitPreviousAvailableAmount = split.getAvailableAmount();
            Short splitPercentage = split.getSplitPercentage();

            Double splitTheoreticalAmount = SplitUtils.computeSplitAmount(topUpAmount, splitPercentage, splitPreviousTheoreticalAmount);
            split.setTheoreticalAmount(splitTheoreticalAmount);

            Double splitAvailableAmount = SplitUtils.computeSplitAmount(topUpAmount, splitPercentage, splitPreviousAvailableAmount);
            split.setAvailableAmount(splitAvailableAmount);

            repository.save(split);

            logger.info("TopUpByAmountAndAccountId ::: Updated split {} amounts.\n\tTheoretical amount: {}, was {}\n\tAvailable amount: {}, was {}", split.getId(), splitTheoreticalAmount, splitPreviousTheoreticalAmount, splitAvailableAmount, splitPreviousAvailableAmount);
        });
    }

    /**
     * Updates theoretical && available amounts for
     * the specified split based on the provided `topUpAmount`
     * considering defined percentage for such split.
     */
    @Transactional(rollbackFor = Exception.class)
    public void topUpByAmount(Integer splitId, Double topUpAmount) {
        Split split = splitReadService.getById(splitId);

        Double splitPreviousTheoreticalAmount = split.getTheoreticalAmount();
        Double splitPreviousAvailableAmount = split.getAvailableAmount();
        Short splitPercentage = split.getSplitPercentage();

        Double splitTheoreticalAmount = SplitUtils.computeSplitAmount(topUpAmount, splitPercentage, splitPreviousTheoreticalAmount);
        split.setTheoreticalAmount(splitTheoreticalAmount);

        Double splitAvailableAmount = splitPreviousAvailableAmount + topUpAmount;
        split.setAvailableAmount(splitAvailableAmount);

        repository.save(split);

        logger.info("TopUpByAmount ::: Updated split {} amounts.\n\tTheoretical amount: {}, was {}\n\tAvailable amount: {}, was {}", split.getId(), splitTheoreticalAmount, splitPreviousTheoreticalAmount, splitAvailableAmount, splitPreviousAvailableAmount);
    }

    /**
     * Updates the specified split available amount
     * based on the provided `chargeAmount`.
     */
    @Transactional(rollbackFor = Exception.class)
    public void chargeSplit(Integer id, Double chargeAmount) {
        Split split = splitReadService.getById(id);

        Double previousAvailableAmount = split.getAvailableAmount();

        split.setAvailableAmount(split.getAvailableAmount() - chargeAmount);

        repository.save(split);

        logger.info("ChargeSplit ::: Updated split {} available amount to {}. Was {}", split.getId(), split.getAvailableAmount(), previousAvailableAmount);
    }

    /**
     * Updates theoretical amount for all the
     * splits in an account based on the up-to-date account wealth
     * considering defined percentages for each split.
     */
    @Transactional(rollbackFor = Exception.class)
    public void refreshTheoreticalAmountsByAccountId(Integer accountId) {
        Account account = accountReadService.getById(accountId);

        List<Split> accountSplits = splitReadService.getAllByAccountId(accountId);

        logger.info("RefreshTheoreticalAmountsByAccountId ::: Updating amounts for {} splits from account {}", accountSplits.size(), accountId);

        accountSplits.forEach((split) -> {
            Double previousTheoreticalAmount = split.getTheoreticalAmount();
            Short splitPercentage = split.getSplitPercentage();

            Double updatedTheoreticalAmount = SplitUtils.computeSplitAmount(account.getWealth(), splitPercentage);
            split.setTheoreticalAmount(updatedTheoreticalAmount);

            repository.save(split);

            logger.info("RefreshTheoreticalAmountsByAccountId ::: Updated split {} theoretical amount to {}. Was {}.", split.getId(), updatedTheoreticalAmount, previousTheoreticalAmount);
        });
    }

    @Override
    protected void validateCreateDto(SplitCreateDto createDto) {
        // This checks whether the account is owned by the current user.
        // If not, the calling method will throw a `ResourceNotFoundException`
        accountReadService.getByIdAndUserId(createDto.getAccountId(), SecurityUtils.getJwtUserId());

        Optional<Split> split = ((SplitRepository) repository).findByNameAndAccountId(createDto.getName(), createDto.getAccountId());

        if (split.isPresent()) {
            String message = String.format("%s with name %s already existing for this account", resourceName, createDto.getName());
            Error error = new Error(InternalErrorCode.CONFLICT, message);
            throw new ConflictException(error);
        }

        Split defaultSplit = getDefaultSplitByAccountId(createDto.getAccountId());
        if (defaultSplit.getSplitPercentage() < createDto.getSplitPercentage()) {
            String errorMessage = String.format("Cannot allocate the requested percentage for such split. Exceeds by %s%%", Math.abs(defaultSplit.getSplitPercentage() - createDto.getSplitPercentage()));
            Error error = new Error(InternalErrorCode.PARAMETER_INVALID, errorMessage);
            throw new ValidationException(List.of(error));
        }
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
        Short existingSplitPercentage = existing.getSplitPercentage();

        if (!updateSplitPercentage.equals(existingSplitPercentage) && updateSplitPercentage > existingSplitPercentage) {
            // This represents the allocating percentage difference. It's used to
            // actually calculate whether the default split can allocate such more on the updated split
            short percentageDiff = (short) (existingSplitPercentage - updateSplitPercentage);

            Split defaultSplit = getDefaultSplitByAccountId(existing.getAccountId());
            if (defaultSplit.getSplitPercentage() + percentageDiff < 0) {
                String errorMessage = String.format("Cannot allocate the requested percentage for such split. Exceeds by %s%%", Math.abs(defaultSplit.getSplitPercentage() + percentageDiff));
                Error error = new Error(InternalErrorCode.PARAMETER_INVALID, errorMessage);
                throw new ValidationException(List.of(error));
            }
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
        accountReadService.getByIdAndUserId(split.getAccountId(), SecurityUtils.getJwtUserId());

        List<Goal> linkedGoals = goalReadService.getBySplitId(id);

        if (!linkedGoals.isEmpty()) {
            Error error = new Error(InternalErrorCode.CONFLICT, "Delete or move linked goals before proceeding.");
            throw new ValidationException(List.of(error));
        }
    }

    @Override
    protected List<Split> doFilter(List<Split> entityPage) {
        if (entityPage == null) {
            return new ArrayList<>();
        }
        if (entityPage.isEmpty()) {
            return entityPage;
        }

        Integer jwtUserId = SecurityUtils.getJwtUserId();

        Set<Integer> userAccountIds = accountReadService.getAllByUserId(jwtUserId).stream()
                .map(Account::getId)
                .collect(Collectors.toSet());

        // Returning only splits which belong to the requesting user
        List<Split> filtered = entityPage.stream()
                .filter((entity) -> userAccountIds.contains(entity.getAccountId()))
                .collect(Collectors.toList());

        return filtered;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    protected void doCreate(Split toCreate) {
        Integer jwtUserId = SecurityUtils.getJwtUserId();

        // Default split percentage needs to be decreased by new split amount, therefore
        // we pass the same value, but prefix it with `-` sign.
        updateDefaultSplit(toCreate.getAccountId(), (short) (toCreate.getSplitPercentage() * -1), jwtUserId.toString());

        Account account = accountReadService.getById(toCreate.getAccountId());
        double splitTheoreticalAmount = SplitUtils.computeSplitAmount(account.getWealth(), toCreate.getSplitPercentage());
        toCreate.setAvailableAmount(0.00);
        toCreate.setTheoreticalAmount(splitTheoreticalAmount);

        toCreate.setActive(true);
        toCreate.setIsDefault(false);

        toCreate.setCreatedBy(jwtUserId.toString());
        toCreate.setModifiedBy(jwtUserId.toString());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    protected void doUpdate(Split toUpdate, SplitUpdateDto updateDto) {
        Integer jwtUserId = SecurityUtils.getJwtUserId();

        short defaultSplitPercentageIncrBy = (short) (toUpdate.getSplitPercentage() - updateDto.getSplitPercentage());
        updateDefaultSplit(toUpdate.getAccountId(), defaultSplitPercentageIncrBy, jwtUserId.toString());

        Account account = accountReadService.getById(toUpdate.getAccountId());
        double splitTheoreticalAmount = SplitUtils.computeSplitAmount(account.getWealth(), updateDto.getSplitPercentage());
        toUpdate.setTheoreticalAmount(splitTheoreticalAmount);

        toUpdate.setModifiedBy(jwtUserId.toString());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    protected void doDelete(Split entity) {
        Integer jwtUserId = SecurityUtils.getJwtUserId();

        updateDefaultSplit(entity.getAccountId(), entity.getSplitPercentage(), jwtUserId.toString());

        entity.setModifiedBy(jwtUserId.toString());
    }

    /**
     * Updates the default split for a specific account.
     * More specifically, updates the allocated percentage,
     * and its theoretical amount based on the updated percentage.
     *
     * @param accountId - the account id which default split belongs to
     * @param updateBy - the percentage amount to increase/decrease by.
     *  Negative values are expected here as well to decrease default split
     *  allocated percentage
     * @param auditor who triggered the default split update. This field generally matches
     *  the authenticated user making the web request
     */
    @Transactional(rollbackFor = Exception.class)
    private void updateDefaultSplit(int accountId, short updateBy, String auditor) {
        Split defaultSplit = getDefaultSplitByAccountId(accountId);

        short updatedDefaultSplitPercentage = (short) (defaultSplit.getSplitPercentage() + updateBy);

        defaultSplit.setSplitPercentage(updatedDefaultSplitPercentage);

        Account account = accountReadService.getById(accountId);

        double updatedDefaultSplitTheoreticalAmount = SplitUtils.computeSplitAmount(account.getWealth(), updatedDefaultSplitPercentage);
        defaultSplit.setTheoreticalAmount(updatedDefaultSplitTheoreticalAmount);

        defaultSplit.setModifiedBy(auditor);
    }

    private Split getDefaultSplitByAccountId(Integer accountId) {
        return ((SplitRepository) repository).findByAccountIdAndIsDefaultTrue(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Default split", "account", accountId.toString()));

    }

    @Override
    protected void checkReadPermission(Split entity) {
        Integer jwtUserId = SecurityUtils.getJwtUserId();

        if (!isSplitOwner(jwtUserId, entity)) {
            throw new ForbiddenOperationException();
        }
    }

    @Override
    protected void checkUpdatePermission(Split entity) {
        Integer jwtUserId = SecurityUtils.getJwtUserId();

        if (!isSplitOwner(jwtUserId, entity)) {
            throw new ForbiddenOperationException();
        }
    }

    @Override
    protected void checkDeletePermission(Split entity) {
        Integer jwtUserId = SecurityUtils.getJwtUserId();

        if (!isSplitOwner(jwtUserId, entity)) {
            throw new ForbiddenOperationException();
        }
    }

    private Boolean isSplitOwner(Integer userId, Split entity) {
        return accountReadService.existsByIdAndUserId(entity.getAccountId(), userId);
    }
}

