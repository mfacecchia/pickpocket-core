package com.feis.splitnings.features.purchase.service;

import com.feis.splitnings.common.exception.ValidationException;
import com.feis.splitnings.common.exception.enums.InternalErrorCode;
import com.feis.splitnings.common.exception.errors.Error;
import com.feis.splitnings.common.service.AbstractService;
import com.feis.splitnings.features.goal.data.Goal;
import com.feis.splitnings.features.goal.service.GoalReadService;
import com.feis.splitnings.features.purchase.data.Purchase;
import com.feis.splitnings.features.purchase.data.dto.request.PurchaseCreateDto;
import com.feis.splitnings.features.purchase.data.dto.request.PurchaseUpdateDto;
import com.feis.splitnings.features.purchase.data.dto.response.PurchaseDto;
import com.feis.splitnings.features.purchase.data.dto.response.PurchasePageDto;
import com.feis.splitnings.features.purchase.data.enums.PurchaseCategory;
import com.feis.splitnings.features.purchase.mapper.PurchaseMapper;
import com.feis.splitnings.features.purchase.repository.PurchaseRepository;
import com.feis.splitnings.features.split.data.Split;
import com.feis.splitnings.features.split.service.SplitReadService;
import com.feis.splitnings.security.utils.SecurityUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PurchaseService extends AbstractService<Purchase, PurchaseDto, PurchaseCreateDto, PurchaseUpdateDto, PurchasePageDto, Integer> {
    private final SplitReadService splitReadService;
    private final GoalReadService goalReadService;

    public PurchaseService(PurchaseMapper purchaseMapper, PurchaseRepository purchaseRepository, SplitReadService splitReadService,
            PurchasePermissionChecker purchasePermissionChecker, GoalReadService goalReadService) {

        this.mapper = purchaseMapper;
        this.repository = purchaseRepository;
        this.splitReadService = splitReadService;
        this.permissionChecker = purchasePermissionChecker;
        this.goalReadService = goalReadService;
        this.resourceName = "Purchase";
    }

    /**
     * Creates a new purchase and link it to a
     * specific goal.
     */
    @Transactional(rollbackFor = Exception.class)
    public PurchaseDto createCompletedGoalPurchase(PurchaseCreateDto createDto, Integer goalId) {
        doValidate(createDto);
        validateCompletedGoalPurchaseCreateDto(createDto, goalId);

        Purchase entity = convertToEntity(createDto, goalId);

        doCreate(entity);

        Purchase saved = repository.saveAndFlush(entity);

        logger.info("CreateCompletedGoalPurchase ::: Created new {} with id {}", resourceName, saved.getId());

        return convertToDto(saved);
    }

    @Override
    protected void validateCreateDto(PurchaseCreateDto createDto) {
        Integer jwtUserId = SecurityUtils.getJwtUserId();

        Split split = splitReadService.getByIdAndUserId(createDto.getSplitId(), jwtUserId);

        if (!split.getActive()) {
            Error error = new Error(InternalErrorCode.PARAMETER_INVALID, "The linked split is inactive. Reactivate it before proceeding.");
            throw new ValidationException(List.of(error));
        }
    }

    @Override
    protected void validateUpdateDto(PurchaseUpdateDto updateDto, Purchase existing) {
    }

    @Override
    protected void validateDelete(Integer id) {
    }

    @Override
    protected List<Purchase> doFilter(List<Purchase> entityPage) {
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

        // Returning only purchases which belong to the requesting user
        List<Purchase> filtered = entityPage.stream()
                .filter((entity) -> userSplitIds.contains(entity.getSplitId()))
                .collect(Collectors.toList());

        return filtered;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    protected void doCreate(Purchase toCreate) {
        if (toCreate.getGoalId() != null) {
            toCreate.setCategory(PurchaseCategory.COMPLETED_GOAL);
        } else {
            toCreate.setCategory(PurchaseCategory.PURCHASE);
        }

        Split split = splitReadService.getById(toCreate.getSplitId());
        toCreate.setAccountId(split.getAccountId());
    }

    private void validateCompletedGoalPurchaseCreateDto(PurchaseCreateDto createDto, Integer goalId) {
        List<Error> validationErrors = new ArrayList<>();

        Goal goal = goalReadService.getById(goalId);
        if (!goal.getSplitId().equals(createDto.getSplitId())) {
            Error error = new Error(InternalErrorCode.PARAMETER_INVALID, "The provided goal is not linked to the specified split");
            validationErrors.add(error);

            logger.info("ValidateCompletedGoalPurchaseCreateDto ::: The provided goal (id {}) is linked to the split with id {}, while the purchase specified split with id {}.", goal.getId(), goal.getSplitId(), createDto.getSplitId());
        }

        try {
            validateCreateDto(createDto);
        } catch (ValidationException e) {
            validationErrors.addAll(e.getErrors());
        }

        if (!validationErrors.isEmpty()) {
            throw new ValidationException(validationErrors);
        }
    }

    private Purchase convertToEntity(PurchaseCreateDto createDto, Integer goalId) {
        Purchase entity = mapper.mapCreateDtoToEntity(createDto);
        entity.setGoalId(goalId);

        return entity;
    }
}

