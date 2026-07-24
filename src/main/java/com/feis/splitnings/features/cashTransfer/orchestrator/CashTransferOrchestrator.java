package com.feis.splitnings.features.cashTransfer.orchestrator;

import com.feis.splitnings.features.cashTransfer.data.dto.request.CashTransferCreateDto;
import com.feis.splitnings.features.cashTransfer.data.dto.response.CashTransferDto;
import com.feis.splitnings.features.cashTransfer.service.CashTransferService;
import com.feis.splitnings.features.split.service.SplitService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CashTransferOrchestrator {
    private final CashTransferService cashTransferService;
    private final SplitService splitService;

    @Transactional(rollbackFor = Exception.class)
    public CashTransferDto transfer(CashTransferCreateDto createDto) {
        CashTransferDto dto = cashTransferService.create(createDto);

        splitService.transferAmount(dto.getFromSplitId(), dto.getToSplitId(), dto.getAmount());

        return dto;
    }
}
