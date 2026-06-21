package com.feis.splitnings.features.purchase.repository;

import com.feis.splitnings.common.repository.BaseRepository;
import com.feis.splitnings.features.purchase.data.Purchase;

import java.util.Optional;

import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseRepository extends BaseRepository<Purchase, Integer> {

    Boolean existsByIdAndSplitAccountUserId(Integer id, Integer userId);

    Optional<Purchase> findByGoalId(Integer goalId);
}

