package com.feis.splitnings.features.goal.service;

import com.feis.splitnings.common.exception.ResourceNotFoundException;
import com.feis.splitnings.features.goal.data.Goal;
import com.feis.splitnings.features.goal.repository.GoalRepository;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class GoalReadService {
    private final GoalRepository goalRepository;
    private final String resourceName = "Goal";

    public Goal getById(Integer id) {
        return goalRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(resourceName, id.toString()));
    }

    // Returns a list of goals from the specificed `accountId`
    // and EAGER fetches associated Split for each goal
    public List<Goal> getAllNotCompletedByAccountIdFetchSplit(Integer accountId) {
        return goalRepository.findAllNotCompletedByAccountIdFetchSplit(accountId);
    }

    public Goal getByIdAndUserId(Integer id, Integer userId) {
        return goalRepository.findByIdAndSplitAccountUserId(id, userId).orElseThrow(
                () -> new ResourceNotFoundException(resourceName, id.toString()));
    }

    public Boolean existsByIdAndUserId(Integer id, Integer userId) {
        return goalRepository.existsByIdAndSplitAccountUserId(id, userId);
    }

    public List<Goal> getBySplitId(Integer splitId) {
        return goalRepository.findBySplitId(splitId);
    }
}

