package com.feis.splitnings.features.issuedPaycheck.orchestrator;

import com.feis.splitnings.features.account.service.AccountService;
import com.feis.splitnings.features.goal.service.GoalService;
import com.feis.splitnings.features.issuedPaycheck.data.dto.request.IssuedPaycheckCreateDto;
import com.feis.splitnings.features.issuedPaycheck.data.dto.response.IssuedPaycheckDto;
import com.feis.splitnings.features.issuedPaycheck.service.IssuedPaycheckService;
import com.feis.splitnings.features.split.service.SplitService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AccountIssuedPaycheckOrchestrator {
    private final AccountService accountService;
    private final SplitService splitService;
    private final IssuedPaycheckService issuedPaycheckService;
    private final GoalService goalService;

    @Transactional(rollbackFor = Exception.class)
    public IssuedPaycheckDto issueManualPaycheck(IssuedPaycheckCreateDto issuedPaycheckCreateDto) {
        Integer accountId = issuedPaycheckCreateDto.getAccountId();
        Double issuedAmount = issuedPaycheckCreateDto.getIssuedAmount();

        accountService.topUpAccount(accountId, issuedAmount);
        splitService.topUpByAmountAndAccountId(accountId, issuedAmount);
        goalService.updateGoalsAmountByAccountId(accountId);

        return issuedPaycheckService.create(issuedPaycheckCreateDto);
    }
}
