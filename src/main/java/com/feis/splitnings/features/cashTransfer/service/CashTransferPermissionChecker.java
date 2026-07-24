package com.feis.splitnings.features.cashTransfer.service;

import com.feis.splitnings.common.service.AbstractPermissionChecker;
import com.feis.splitnings.features.cashTransfer.data.CashTransfer;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CashTransferPermissionChecker extends AbstractPermissionChecker<CashTransfer> {
    private final CashTransferReadService cashTransferReadService;

    @Override
    protected Boolean isResourceOwner(Integer userId, CashTransfer entity) {
        return cashTransferReadService.existsByIdAndUserId(entity.getId(), userId);
    }
}
