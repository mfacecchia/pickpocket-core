package com.feis.splitnings.features.goal.service;

import com.feis.splitnings.common.exception.errors.Error;
import com.feis.splitnings.common.exception.ConflictException;
import com.feis.splitnings.common.exception.ValidationException;
import com.feis.splitnings.common.exception.enums.InternalErrorCode;
import com.feis.splitnings.common.service.AbstractService;
import com.feis.splitnings.features.goal.data.Goal;
import com.feis.splitnings.features.goal.data.dto.request.GoalCreateDto;
import com.feis.splitnings.features.goal.data.dto.request.GoalUpdateDto;
import com.feis.splitnings.features.goal.data.dto.response.GoalDto;
import com.feis.splitnings.features.goal.data.dto.response.GoalPageDto;
import com.feis.splitnings.features.goal.mapper.GoalMapper;
import com.feis.splitnings.features.goal.repository.GoalRepository;
import com.feis.splitnings.features.split.data.Split;
import com.feis.splitnings.features.split.service.SplitReadService;
import com.feis.splitnings.security.utils.SecurityUtils;

import java.time.Instant;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GoalService extends AbstractService<Goal, GoalDto, GoalCreateDto, GoalUpdateDto, GoalPageDto, Integer> {
    private final SplitReadService splitReadService;
    private final GoalReadService goalReadService;

    public GoalService(GoalMapper goalMapper, GoalRepository goalRepository, SplitReadService splitReadService,
            GoalReadService goalReadService) {

        this.mapper = goalMapper;
        this.repository = goalRepository;
        this.splitReadService = splitReadService;
        this.goalReadService = goalReadService;
        this.resourceName = "Goal";
    }

    @Override
    protected void validateCreateDto(GoalCreateDto createDto) {
        Integer jwtUserId = SecurityUtils.getJwtUserId();

        Split split = splitReadService.getByIdAndUserId(createDto.getSplitId(), jwtUserId, false);

        if (!split.getActive()) {
            String errorMessage = "The split is not active, to link this goal to the split, first reactivate it";
            Error error = new Error(InternalErrorCode.PARAMETER_INVALID, errorMessage);
            throw new ValidationException(List.of(error));
        }

        Optional<Goal> goals = ((GoalRepository) repository).findByNameAndSplitAccountId(createDto.getName(), split.getAccountId());

        if (goals.isPresent()) {
            String message = String.format("%s with name %s already existing for this split", resourceName, createDto.getName());
            Error error = new Error(InternalErrorCode.CONFLICT, message);
            throw new ConflictException(error);
        }
    }

    @Override
    protected void validateUpdateDto(GoalUpdateDto updateDto, Goal existing) {
        Integer jwtUserId = SecurityUtils.getJwtUserId();

        if (existing.getCompleted()) {
            Error error = new Error(InternalErrorCode.CONFLICT, "Cannot update an already completed goal.");
            throw new ValidationException(List.of(error));
        }

        // This will throw a `ResourceNotFoundException` if not found,
        // used to check whether the new split exists for the requesting user
        Split split = splitReadService.getByIdAndUserId(updateDto.getSplitId(), jwtUserId, false);

        // Cannot move goal to another split if such is inactive
        if (!existing.getSplitId().equals(updateDto.getSplitId()) && !split.getActive()) {
            String errorMessage = "The split is not active, to link this goal to the split, first reactivate it";
            Error error = new Error(InternalErrorCode.PARAMETER_INVALID, errorMessage);
            throw new ValidationException(List.of(error));
        }

        Optional<Goal> goal = ((GoalRepository) repository).findByNameAndSplitAccountId(updateDto.getName(), split.getAccountId());

        if (goal.isPresent() && !goal.get().getSplitId().equals(existing.getSplitId())) {
            String message = String.format("%s with name %s already existing for this split", resourceName, updateDto.getName());
            Error error = new Error(InternalErrorCode.CONFLICT, message);
            throw new ConflictException(error);
        }
    }

    @Override
    protected void validateDelete(Integer id) {
    }

    @Override
    protected Integer getResourceId(Goal entity) {
        return entity.getId();
    }

    @Override
    protected List<Goal> doFilter(List<Goal> entityPage) {
        if (entityPage == null) {
            return new ArrayList<>();
        }
        if (entityPage.isEmpty()) {
            return entityPage;
        }

        Integer jwtUserId = SecurityUtils.getJwtUserId();

        Set<Integer> userSplitIds = splitReadService.getAllByUserId(jwtUserId).stream()
                .map(Split::getId)
                .collect(Collectors.toSet());

        // Returning only goals which belong to the requesting user
        List<Goal> filtered = entityPage.stream()
                .filter((entity) -> userSplitIds.contains(entity.getSplitId()))
                .collect(Collectors.toList());

        return filtered;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    protected void doCreate(Goal toCreate) {
        Integer jwtUserId = SecurityUtils.getJwtUserId();

        Split split = splitReadService.getById(toCreate.getSplitId());

        updateGoalAmount(toCreate, split.getAvailableAmount());

        toCreate.setCreatedBy(jwtUserId.toString());
        toCreate.setModifiedBy(jwtUserId.toString());
        toCreate.setCompleted(false);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    protected void doUpdate(Goal toUpdate, GoalUpdateDto updateDto) {
        Integer jwtUserId = SecurityUtils.getJwtUserId();

        if (!toUpdate.getSplitId().equals(updateDto.getSplitId()) ||
                !toUpdate.getTargetAmount().equals(updateDto.getTargetAmount())) {

            Split split = splitReadService.getByIdAndUserId(updateDto.getSplitId(), jwtUserId, false);

            toUpdate.setTargetAmount(updateDto.getTargetAmount());
            updateGoalAmount(toUpdate, split.getAvailableAmount());
        }

        toUpdate.setModifiedBy(jwtUserId.toString());
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateGoalsAmountByAccountId(Integer accountId) {
        List<Goal> accountGoals = goalReadService.getAllNotCompletedByAccountIdFetchSplit(accountId);

        logger.info("UpdateGoalsAmountByAccountId ::: Updating amounts for {} goals from account {}", accountGoals.size(), accountId);

        accountGoals.forEach((goal) -> {
            double goalPrevAmount = goal.getCurrentAmount();

            updateGoalAmount(goal, goal.getSplit().getAvailableAmount());

            logger.info("UpdateGoalsAmountByAccountId ::: Updated goal {} amount. {}, was {}", goal.getId(), goal.getCurrentAmount(), goalPrevAmount);
        });

        repository.saveAll(accountGoals);
    }

    public GoalDto complete(Integer goalId) {
        // TODO: Create purchase on goal completion
        Integer jwtUserId = SecurityUtils.getJwtUserId();

        Goal goal = goalReadService.getByIdAndUserId(goalId, jwtUserId);

        if (goal.getCurrentAmount() < goal.getTargetAmount()) {
            Error error = new Error(InternalErrorCode.PARAMETER_INVALID, "Cannot complete this goal. The target has not yet been reached.");
            throw new ValidationException(List.of(error));
        }

        if (goal.getCompleted()) {
            Error error = new Error(InternalErrorCode.CONFLICT, "Cannot complete this goal. It appears as already completed.");
            throw new ConflictException(error);
        }

        goal.setCompleted(true);
        goal.setCompletedAt(Instant.now());

        repository.save(goal);

        return mapper.mapToDto(goal);
    }

    public GoalDto uncomplete(Integer goalId) {
        // TODO: Delete linked purchase on goal uncomplete
        Integer jwtUserId = SecurityUtils.getJwtUserId();

        Goal goal = goalReadService.getByIdAndUserId(goalId, jwtUserId);

        if (!goal.getCompleted()) {
            Error error = new Error(InternalErrorCode.CONFLICT, "Cannot uncomplete this goal. It's already active.");
            throw new ConflictException(error);
        }

        goal.setCompleted(false);
        goal.setCompletedAt(null);
        updateGoalAmount(goal, goal.getSplit().getAvailableAmount());

        repository.save(goal);

        return mapper.mapToDto(goal);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    protected void doDelete(Goal entity) {
        Integer jwtUserId = SecurityUtils.getJwtUserId();

        entity.setModifiedBy(jwtUserId.toString());
    }

    private void updateGoalAmount(Goal goal, Double splitAvailableAmount) {
        if (splitAvailableAmount <= 0) {
            goal.setCurrentAmount(0.00);
        }
        else if (splitAvailableAmount >= goal.getTargetAmount()) {
            goal.setCurrentAmount(goal.getTargetAmount());
        } else {
            goal.setCurrentAmount(splitAvailableAmount);
        }
    }
}

