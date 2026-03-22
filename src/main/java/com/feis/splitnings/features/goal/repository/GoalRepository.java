package com.feis.splitnings.features.goal.repository;

import com.feis.splitnings.common.repository.BaseRepository;
import com.feis.splitnings.features.goal.data.Goal;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;

public interface GoalRepository extends BaseRepository<Goal, Integer> {

    List<Goal> findAllBySplitId(Integer splitId);

    Optional<Goal> findByNameAndSplitAccountId(String name, Integer accountId);

    List<Goal> findAllBySplitAccountId(Integer accountId);

    @Query("""
        SELECT g
        FROM goal g
        JOIN FETCH g.split s
        WHERE s.accountId = :accountId
    """)
    List<Goal> findAllByAccountIdFetchSplit(Integer accountId);
}
