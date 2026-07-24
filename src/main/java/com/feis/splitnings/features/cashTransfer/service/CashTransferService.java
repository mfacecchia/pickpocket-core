package com.feis.splitnings.features.cashTransfer.service;

import com.feis.splitnings.common.exception.ValidationException;
import com.feis.splitnings.common.exception.enums.InternalErrorCode;
import com.feis.splitnings.common.exception.errors.Error;
import com.feis.splitnings.common.service.AbstractService;
import com.feis.splitnings.features.account.data.Account;
import com.feis.splitnings.features.account.service.AccountReadService;
import com.feis.splitnings.features.cashTransfer.data.CashTransfer;
import com.feis.splitnings.features.cashTransfer.data.dto.request.CashTransferCreateDto;
import com.feis.splitnings.features.cashTransfer.data.dto.request.CashTransferUpdateDto;
import com.feis.splitnings.features.cashTransfer.data.dto.response.CashTransferDto;
import com.feis.splitnings.features.cashTransfer.data.dto.response.CashTransferPageDto;
import com.feis.splitnings.features.cashTransfer.mapper.CashTransferMapper;
import com.feis.splitnings.features.cashTransfer.repository.CashTransferRepository;
import com.feis.splitnings.features.split.data.Split;
import com.feis.splitnings.features.split.service.SplitReadService;
import com.feis.splitnings.security.utils.SecurityUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

@Service
public class CashTransferService extends AbstractService<CashTransfer, CashTransferDto, CashTransferCreateDto, CashTransferUpdateDto, CashTransferPageDto, Integer> {
    private final SplitReadService splitReadService;
    private final AccountReadService accountReadService;

    public CashTransferService(CashTransferMapper cashTransferMapper, CashTransferRepository cashTransferRepository,
            CashTransferPermissionChecker cashTransferPermissionChecker, SplitReadService splitReadService,
            AccountReadService accountReadService) {

        this.mapper = cashTransferMapper;
        this.repository = cashTransferRepository;
        this.permissionChecker = cashTransferPermissionChecker;
        this.splitReadService = splitReadService;
        this.accountReadService = accountReadService;
        this.resourceName = "Cash Transfer";
    }

    @Override
    protected void doCreate(CashTransfer toCreate) {
        // NOTE: From-To splits are supposed to be in the same account by design,
        //  therefore, the selection of one over the other in this case is negligible
        Split fromSplit = splitReadService.getById(toCreate.getFromSplitId());
        toCreate.setAccountId(fromSplit.getAccountId());
    }

    @Override
    protected void validateCreateDto(CashTransferCreateDto createDto) {
        Integer jwtUserId = SecurityUtils.getJwtUserId();

        List<Error> validationErrors = new ArrayList<>();

        if (createDto.getFromSplitId().equals(createDto.getToSplitId())) {
            String errorMessage = "Cannot transfer cash to the same split as the source";
            Error error = new Error(InternalErrorCode.PARAMETER_INVALID, errorMessage);
            validationErrors.add(error);
        }

        Split fromSplit = splitReadService.getByIdAndUserId(createDto.getFromSplitId(), jwtUserId);
        Split toSplit = splitReadService.getByIdAndUserId(createDto.getToSplitId(), jwtUserId);

        if (!fromSplit.getAccountId().equals(toSplit.getAccountId())) {
            String errorMessage = "Cannot transfer cash to a split of another account";
            Error error = new Error(InternalErrorCode.PARAMETER_INVALID, errorMessage);
            validationErrors.add(error);
        }

        Double transferAmount = createDto.getAmount();
        if (fromSplit.getAvailableAmount() < transferAmount) {
            String errorMessage = String.format("Cannot transfer %s to the split. Split's available amount is %.2f", transferAmount, fromSplit.getAvailableAmount());
            Error error = new Error(InternalErrorCode.PARAMETER_INVALID, errorMessage);
            validationErrors.add(error);
        }

        if (!validationErrors.isEmpty()) {
            throw new ValidationException(validationErrors);
        }
    }

    @Override
    protected void validateUpdateDto(CashTransferUpdateDto updateDto, CashTransfer existing) {
    }

    @Override
    protected void validateDelete(Integer id) {
    }

    @Override
    protected List<CashTransfer> doFilter(List<CashTransfer> entityPage) {
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
        List<CashTransfer> filtered = entityPage.stream()
                .filter((entity) -> userAccountIds.contains(entity.getAccountId()))
                .collect(Collectors.toList());

        return filtered;
    }
}

