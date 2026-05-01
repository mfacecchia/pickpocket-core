package com.feis.splitnings.features.split.service;

import com.feis.splitnings.common.exception.ResourceNotFoundException;
import com.feis.splitnings.features.split.data.Split;
import com.feis.splitnings.features.split.repository.SplitRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class SplitReadService {
    private final SplitRepository splitRepository;
    private final String resourceName = "Split";

    public Split getById(Integer id) {
        return splitRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(resourceName, id.toString()));
    }

    public List<Split> getAllByAccountId(Integer accountId) {
        return splitRepository.findAllByAccountId(accountId);
    }

    public List<Split> getAllByUserId(Integer userId) {
        return splitRepository.findAllByAccountUserId(userId);
    }

    public Split getByIdAndUserId(Integer id, Integer userId, boolean includeDeleted) {
        Optional<Split> split = splitRepository.findByIdAndAccountUserId(id, userId);

        if (split.isEmpty() || (includeDeleted && split.get().getDeleted())) {
            throw new ResourceNotFoundException(resourceName, id.toString());
        }

        return split.get();
    }

    public Split getByIdAndAccountId(Integer id, Integer accountId) {
        return splitRepository.getByIdAndAccountId(id, accountId).orElseThrow(
                () -> new ResourceNotFoundException(resourceName, id.toString()));
    }

}
