package com.feis.splitnings.features.split.repository;

import com.feis.splitnings.common.repository.BaseRepository;
import com.feis.splitnings.features.split.data.Split;

import java.util.List;
import java.util.Optional;

public interface SplitRepository extends BaseRepository<Split, Integer> {

    Optional<Split> findByNameAndAccountId(String name, Integer accountId);

    List<Split> findAllByAccountId(Integer accountId);

    Optional<Split> findByAccountIdAndIsDefaultTrue(Integer accountId);

    List<Split> findAllByAccountUserId(Integer userId);

    Optional<Split> findByIdAndAccountUserId(Integer id, Integer accountId);

    Optional<Split> getByIdAndAccountId(Integer id, Integer accountId);
}
