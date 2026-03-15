package com.feis.splitnings.features.goal.repository;

import com.feis.splitnings.common.repository.BaseRepository;
import com.feis.splitnings.features.goal.data.Goal;

import java.util.List;
import java.util.Optional;

public interface GoalRepository extends BaseRepository<Goal, Integer> {

    List<Goal> findAllBySplitId(Integer splitId);

    Optional<Goal> findByNameAndSplitId(String name, Integer splitId);
}
