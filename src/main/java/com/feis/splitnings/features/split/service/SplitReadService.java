package com.feis.splitnings.features.split.service;

import com.feis.splitnings.features.split.data.Split;
import com.feis.splitnings.features.split.repository.SplitRepository;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class SplitReadService {
    private final SplitRepository splitRepository;

    public List<Split> getAllByAccountId(Integer accountId) {
        return splitRepository.findAllByAccountId(accountId);
    }
}
