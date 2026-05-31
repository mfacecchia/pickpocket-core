package com.feis.splitnings.features.split.service;

import com.feis.splitnings.common.exception.ForbiddenOperationException;
import com.feis.splitnings.common.service.AbstractPermissionChecker;
import com.feis.splitnings.features.account.data.Account;
import com.feis.splitnings.features.account.service.AccountReadService;
import com.feis.splitnings.features.split.data.Split;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class SplitPermissionChecker extends AbstractPermissionChecker<Split> {
    private final AccountReadService accountReadService;

    @Override
    protected Boolean isResourceOwner(Integer userId, Split entity) {
        return accountReadService.existsByIdAndUserId(entity.getAccountId(), userId);
    }

    /**
     * Checks split update permission by checking if the linked account
     * actually belongs to the requesting user.
     * This method comes in handy when it comes to bulk splits updates,
     * since in such cases the caller knows the accountId other than
     * a single splitId to check against.
     */
    public void checkUpdatePermissionByAccountId(Integer userId, Integer accountId) {
        Account linkedAccount = accountReadService.getById(accountId);

        if (!linkedAccount.getUserId().equals(userId)) {
            throw new ForbiddenOperationException();
        }
    }
}
