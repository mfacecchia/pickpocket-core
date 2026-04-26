package com.feis.splitnings.features.purchase.orchestrator;

import com.feis.splitnings.features.account.service.AccountService;
import com.feis.splitnings.features.purchase.data.dto.request.PurchaseCreateDto;
import com.feis.splitnings.features.purchase.data.dto.response.PurchaseDto;
import com.feis.splitnings.features.purchase.service.PurchaseService;
import com.feis.splitnings.features.split.data.Split;
import com.feis.splitnings.features.split.service.SplitReadService;
import com.feis.splitnings.features.split.service.SplitService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PurchaseOrchestrator {
    private final PurchaseService purchaseService;
    private final AccountService accountService;
    private final SplitService splitService;
    private final SplitReadService splitReadService;

    @Transactional(rollbackFor = Exception.class)
    public PurchaseDto submitPurchase(PurchaseCreateDto purchaseCreateDto) {
        PurchaseDto purchaseDto = purchaseService.create(purchaseCreateDto);

        Integer splitId = purchaseCreateDto.getSplitId();
        Double chargeAmount = purchaseCreateDto.getAmount();

        Split split = splitReadService.getById(splitId);

        accountService.chargeAccount(split.getAccountId(), chargeAmount);
        splitService.refreshTheoreticalAmountsByAccountId(split.getAccountId());
        splitService.chargeSplit(splitId, chargeAmount);

        return purchaseDto;
    }
}
