package com.feis.splitnings.features.account.orchestrator;

import com.feis.splitnings.features.account.data.dto.request.AccountCreateDto;
import com.feis.splitnings.features.account.data.dto.response.AccountDto;
import com.feis.splitnings.features.account.service.AccountService;
import com.feis.splitnings.features.issuedPaycheck.data.dto.request.IssuedPaycheckCreateDto;
import com.feis.splitnings.features.issuedPaycheck.service.IssuedPaycheckService;
import com.feis.splitnings.features.split.service.SplitService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AccountSplitOrchestrator {
    private final AccountService accountService;
    private final SplitService splitService;
    private final IssuedPaycheckService issuedPaycheckService;

    @Transactional(rollbackFor = Exception.class)
    public AccountDto createAccountAndDefaultSplit(AccountCreateDto accountCreateDto) {
        AccountDto accountDto = accountService.create(accountCreateDto);
        splitService.createDefaultSplit(accountDto.getId(), accountDto.getWealth());

        if (accountCreateDto.getInitialAmount() > 0) {
            issueAccountOpeningPaycheck(accountDto);
        }

        return accountDto;
    }

    private void issueAccountOpeningPaycheck(AccountDto accountDto) {
        IssuedPaycheckCreateDto initialPaycheck = new IssuedPaycheckCreateDto();
        initialPaycheck.setAccountId(accountDto.getId());
        initialPaycheck.setIssuedAmount(accountDto.getWealth());

        issuedPaycheckService.create(initialPaycheck);
    }
}
