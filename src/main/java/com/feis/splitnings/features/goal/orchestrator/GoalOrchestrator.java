package com.feis.splitnings.features.goal.orchestrator;

import com.feis.splitnings.features.account.service.AccountService;
import com.feis.splitnings.features.goal.data.dto.response.GoalDto;
import com.feis.splitnings.features.goal.service.GoalService;
import com.feis.splitnings.features.purchase.data.Purchase;
import com.feis.splitnings.features.purchase.data.dto.request.PurchaseCreateDto;
import com.feis.splitnings.features.purchase.service.PurchaseReadService;
import com.feis.splitnings.features.purchase.service.PurchaseService;
import com.feis.splitnings.features.split.data.Split;
import com.feis.splitnings.features.split.service.SplitReadService;
import com.feis.splitnings.features.split.service.SplitService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class GoalOrchestrator {
    private final GoalService goalService;
    private final PurchaseService purchaseService;
    private final PurchaseReadService purchaseReadService;
    private final AccountService accountService;
    private final SplitService splitService;
    private final SplitReadService splitReadService;

    @Transactional(rollbackFor = Exception.class)
    public GoalDto completeGoal(Integer goalId) {
        GoalDto goalDto = goalService.complete(goalId);

        PurchaseCreateDto purchaseCreateDto = convertGoalDtoToPurchaseCreateDto(goalDto);
        submitGoalPurchase(purchaseCreateDto, goalId);

        return goalDto;
    }

    @Transactional(rollbackFor = Exception.class)
    public GoalDto uncompleteGoal(Integer goalId) {
        goalService.uncomplete(goalId);

        Purchase purchase = purchaseReadService.getByGoalId(goalId);
        purchaseService.delete(purchase.getId());

        Split linkedSplit = splitReadService.getById(purchase.getSplitId());

        Double purchaseAmount = purchase.getAmount();
        Integer accountId = linkedSplit.getAccountId();

        accountService.topUpAccount(accountId, purchaseAmount);

        splitService.topUpByAmount(purchase.getSplitId(), purchaseAmount);
        splitService.refreshTheoreticalAmountsByAccountId(accountId);

        GoalDto goalDto = goalService.refreshAmount(goalId);

        return goalDto;
    }

    @Transactional(rollbackFor = Exception.class)
    private void submitGoalPurchase(PurchaseCreateDto purchaseCreateDto, Integer goalId) {
        Integer splitId = purchaseCreateDto.getSplitId();
        Double chargeAmount = purchaseCreateDto.getAmount();

        Split split = splitReadService.getById(splitId);

        purchaseService.createCompletedGoalPurchase(purchaseCreateDto, goalId);

        accountService.chargeAccount(split.getAccountId(), chargeAmount);

        splitService.refreshTheoreticalAmountsByAccountId(split.getAccountId());
        splitService.chargeSplit(splitId, chargeAmount);
    }

    private PurchaseCreateDto convertGoalDtoToPurchaseCreateDto(GoalDto goalDto) {
        PurchaseCreateDto purchaseCreateDto = new PurchaseCreateDto();

        purchaseCreateDto.setAmount(goalDto.getTargetAmount());
        purchaseCreateDto.setName(goalDto.getName());
        purchaseCreateDto.setDescription(String.format("Completed goal \"%s\".", goalDto.getName()));
        purchaseCreateDto.setSplitId(goalDto.getSplitId());

        return purchaseCreateDto;
    }
}
