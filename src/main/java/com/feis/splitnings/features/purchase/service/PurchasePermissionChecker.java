package com.feis.splitnings.features.purchase.service;

import com.feis.splitnings.common.service.AbstractPermissionChecker;
import com.feis.splitnings.features.purchase.data.Purchase;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class PurchasePermissionChecker extends AbstractPermissionChecker<Purchase> {
    private final PurchaseReadService purchaseReadService;

    @Override
    protected Boolean isResourceOwner(Integer userId, Purchase entity) {
        return purchaseReadService.existsByIdAndUserId(entity.getId(), userId);
    }
}
