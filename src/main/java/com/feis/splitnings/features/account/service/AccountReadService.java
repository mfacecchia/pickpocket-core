package com.feis.splitnings.features.account.service;

import com.feis.splitnings.common.exception.ResourceNotFoundException;
import com.feis.splitnings.features.account.data.Account;
import com.feis.splitnings.features.account.repository.AccountRepository;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AccountReadService {
    private final AccountRepository repository;
    private final String resourceName = "Account";

    public Account getById(Integer accountId) {
        return repository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException(resourceName, accountId.toString()));
    }

    public List<Account> getAllByUserId(Integer userId) {
        return repository.findByUserId(userId);
    }

    public Account getByIdAndUserId(Integer accountId, Integer userId) {
        return repository.findByIdAndUserId(accountId, userId)
                .orElseThrow(() -> new ResourceNotFoundException(resourceName, accountId.toString()));
    }

    public Boolean existsByIdAndUserId(Integer accountId, Integer userId) {
        return repository.existsByIdAndUserId(accountId, userId);
    }
}
