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

    public Goal getById(Integer id) {
        return goalRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Goal", id.toString()));
    }

    // Returns a list of goals from the specificed `accountId`
    // and EAGER fetches associated Split for each goal
    public List<Goal> getAllByAccountIdFetchSplit(Integer accountId) {
        return goalRepository.findAllByAccountIdFetchSplit(accountId);
    }
}

