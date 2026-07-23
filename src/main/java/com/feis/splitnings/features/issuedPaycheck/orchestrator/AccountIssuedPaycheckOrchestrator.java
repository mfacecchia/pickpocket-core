package com.feis.splitnings.features.issuedPaycheck.orchestrator;

import com.feis.splitnings.features.account.service.AccountService;
import com.feis.splitnings.features.goal.service.GoalService;
import com.feis.splitnings.features.issuedPaycheck.data.dto.request.IssuedPaycheckCreateDto;
import com.feis.splitnings.features.issuedPaycheck.data.dto.response.IssuedPaycheckDto;
import com.feis.splitnings.features.issuedPaycheck.service.IssuedPaycheckService;
import com.feis.splitnings.features.split.service.SplitReadService;
import com.feis.splitnings.features.split.service.SplitService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AccountIssuedPaycheckOrchestrator {
    private final static Logger logger = LogManager.getLogger(AccountIssuedPaycheckOrchestrator.class);

    private final AccountService accountService;
    private final SplitService splitService;
    private final IssuedPaycheckService issuedPaycheckService;
    private final GoalService goalService;
    private final SplitReadService splitReadService;

    /**
     * Top-ups the specified account by the specified amount, based on the
     * provided {@code IssuedPaycheckCreateDto} parameter.
     * It's possible to top-up either the whole account
     * (if the {@code splitId} attribute is {@code null}), or the single split
     * if the same attribute is set with a valid id.
     */
    @Transactional(rollbackFor = Exception.class)
    public IssuedPaycheckDto issueManualPaycheck(IssuedPaycheckCreateDto issuedPaycheckCreateDto) {
        Integer accountId = issuedPaycheckCreateDto.getAccountId();
        Integer splitId = issuedPaycheckCreateDto.getSplitId();
        Double issuedAmount = issuedPaycheckCreateDto.getIssuedAmount();

        if (splitId != null) {
            assertSplitInAccount(accountId, splitId);
        }

        accountService.topUpAccount(accountId, issuedAmount);
        topUpSplitsForAccount(issuedPaycheckCreateDto);
        goalService.refreshGoalsAmountByAccountId(accountId);

        return issuedPaycheckService.create(issuedPaycheckCreateDto);
    }

    /**
     * Validates whether the defined split exists in the
     * provided account.
     *
     * @throws ResourceNotFoundException if the split does not exist
     */
    private void assertSplitInAccount(Integer accountId, Integer splitId) {
        splitReadService.getByIdAndAccountId(splitId, accountId);
    }

    private void topUpSplitsForAccount(IssuedPaycheckCreateDto paycheckDetails) {
        Integer accountId = paycheckDetails.getAccountId();
        Integer splitId = paycheckDetails.getSplitId();
        Double issuedAmount = paycheckDetails.getIssuedAmount();

        if (splitId == null) {
            logger.info("TopUpSplitsForAccount ::: Issuing paycheck for all splits in accountId {}.", accountId);

            splitService.topUpByAmountAndAccountId(accountId, issuedAmount);
        } else {
            logger.info("TopUpSplitsForAccount ::: Issuing paycheck for split with id {} from account with id {}", splitId, accountId);

            splitService.topUpByAmount(splitId, issuedAmount);
            splitService.refreshTheoreticalAmountsByAccountId(accountId);
        }
    }
}

