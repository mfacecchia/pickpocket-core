package com.feis.splitnings.features.goal.repository;

import com.feis.splitnings.common.repository.BaseRepository;
import com.feis.splitnings.features.goal.data.Goal;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;

public interface GoalRepository extends BaseRepository<Goal, Integer> {

    Optional<Goal> findByNameAndSplitAccountId(String name, Integer accountId);

    @Query("""
        SELECT g
        FROM goal g
        JOIN FETCH g.split s
        WHERE s.accountId = :accountId
        AND g.deleted = false
        AND g.completed = false
    """)
    List<Goal> findAllNotCompletedByAccountIdFetchSplit(Integer accountId);

    Optional<Goal> findByIdAndSplitAccountUserIdAndDeletedFalse(Integer id, Integer userId);

    Boolean existsByIdAndSplitAccountUserIdAndDeletedFalse(Integer id, Integer userId);

    List<Goal> findBySplitIdAndDeletedFalse(Integer splitId);
}
