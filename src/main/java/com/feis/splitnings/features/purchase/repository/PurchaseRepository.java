package com.feis.splitnings.features.purchase.repository;

import com.feis.splitnings.common.repository.BaseRepository;
import com.feis.splitnings.features.purchase.data.Purchase;

import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseRepository extends BaseRepository<Purchase, Integer> {
}

