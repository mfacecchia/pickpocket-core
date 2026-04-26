package com.feis.splitnings.features.purchase.service;

import com.feis.splitnings.common.exception.ResourceNotFoundException;
import com.feis.splitnings.features.purchase.data.Purchase;
import com.feis.splitnings.features.purchase.repository.PurchaseRepository;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PurchaseReadService {
    private final PurchaseRepository purchaseRepository;
    private final String resourceName = "Purchase";

    public Purchase getById(Integer id) {
        return purchaseRepository.findByIdAndDeleted(id, false).orElseThrow(
                () -> new ResourceNotFoundException(resourceName, id.toString()));
    }
}

