package com.feis.splitnings.features.account.repository;

import com.feis.splitnings.common.repository.BaseRepository;
import com.feis.splitnings.features.account.data.Account;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends BaseRepository<Account, Integer> {

    Optional<Account> findByNameAndUserId(String name, Integer userId);

    Optional<Account> findByIdAndUserId(Integer id, Integer userId);

    List<Account> findByUserId(Integer userId);
}
