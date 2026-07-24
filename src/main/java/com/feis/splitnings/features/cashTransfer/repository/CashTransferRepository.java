package com.feis.splitnings.features.cashTransfer.repository;

import com.feis.splitnings.common.repository.BaseRepository;
import com.feis.splitnings.features.cashTransfer.data.CashTransfer;

import org.springframework.stereotype.Repository;

@Repository
public interface CashTransferRepository extends BaseRepository<CashTransfer, Integer> {

    Boolean existsByIdAndAccountUserId(Integer id, Integer userId);
}

