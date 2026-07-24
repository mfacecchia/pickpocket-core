package com.feis.splitnings.features.cashTransfer.service;

import com.feis.splitnings.common.exception.ResourceNotFoundException;
import com.feis.splitnings.features.cashTransfer.data.CashTransfer;
import com.feis.splitnings.features.cashTransfer.repository.CashTransferRepository;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CashTransferReadService {
    private final CashTransferRepository cashTransferRepository;
    private final String resourceName = "Cash Transfer";

    public CashTransfer getById(Integer id) {
        return cashTransferRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(resourceName, id.toString()));
    }

    public Boolean existsByIdAndUserId(Integer id, Integer userId) {
        return cashTransferRepository.existsByIdAndAccountUserId(id, userId);
    }
}

