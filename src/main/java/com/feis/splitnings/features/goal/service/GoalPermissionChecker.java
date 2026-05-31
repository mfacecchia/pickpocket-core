package com.feis.splitnings.features.goal.service;

import com.feis.splitnings.common.exception.ForbiddenOperationException;
import com.feis.splitnings.common.service.AbstractPermissionChecker;
import com.feis.splitnings.features.account.data.Account;
import com.feis.splitnings.features.account.service.AccountReadService;
import com.feis.splitnings.features.goal.data.Goal;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class GoalPermissionChecker extends AbstractPermissionChecker<Goal> {
    private final GoalReadService goalReadService;
    private final AccountReadService accountReadService;

    @Override
    protected Boolean isResourceOwner(Integer userId, Goal entity) {
        return goalReadService.existsByIdAndUserId(entity.getId(), userId);
    }

    /**
     * Checks goal update permission by checking if the inheritely
     * linked account actually belongs to the requesting user.
     * This method comes in handy when it comes to bulk goals updates,
     * since in such cases the caller knows the accountId other than
     * a single goal to check against.
     */
    public void checkUpdatePermissionByAccountId(Integer userId, Integer accountId) {
        Account linkedAccount = accountReadService.getById(accountId);

        if (!linkedAccount.getUserId().equals(userId)) {
            throw new ForbiddenOperationException();
        }
    }
}
