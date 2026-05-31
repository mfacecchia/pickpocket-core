package com.feis.splitnings.features.issuedPaycheck.service;

import com.feis.splitnings.common.service.AbstractPermissionChecker;
import com.feis.splitnings.features.account.service.AccountReadService;
import com.feis.splitnings.features.issuedPaycheck.data.IssuedPaycheck;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class IssuedPaycheckPermissionChecker extends AbstractPermissionChecker<IssuedPaycheck> {
    private final AccountReadService accountReadService;

    @Override
    protected Boolean isResourceOwner(Integer userId, IssuedPaycheck entity) {
        return accountReadService.existsByIdAndUserId(entity.getAccountId(), userId);
    }
}
