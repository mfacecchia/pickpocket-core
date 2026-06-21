package com.feis.splitnings.features.account.orchestrator;

import com.feis.splitnings.features.account.data.dto.request.AccountCreateDto;
import com.feis.splitnings.features.account.data.dto.response.AccountDto;
import com.feis.splitnings.features.account.service.AccountService;
import com.feis.splitnings.features.split.service.SplitService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AccountSplitOrchestrator {
    private final AccountService accountService;
    private final SplitService splitService;

    @Transactional(rollbackFor = Exception.class)
    public AccountDto createAccountAndDefaultSplit(AccountCreateDto accountCreateDto) {
        // TODO: Create paycheck if `initialAmount` is set
        AccountDto accountDto = accountService.create(accountCreateDto);
        splitService.createDefaultSplit(accountDto.getId(), accountDto.getWealth());

        return accountDto;
    }
}
