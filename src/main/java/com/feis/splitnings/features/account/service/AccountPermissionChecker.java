package com.feis.splitnings.features.account.service;

import org.springframework.stereotype.Component;

import com.feis.splitnings.common.service.AbstractPermissionChecker;
import com.feis.splitnings.features.account.data.Account;

@Component
public class AccountPermissionChecker extends AbstractPermissionChecker<Account> {

    @Override
    protected Boolean isResourceOwner(Integer userId, Account entity) {
        return userId.equals(entity.getUserId());
    }

}
