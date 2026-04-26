package com.feis.splitnings.features.purchase.service;

import com.feis.splitnings.common.exception.ForbiddenOperationException;
import com.feis.splitnings.common.service.AbstractService;
import com.feis.splitnings.features.purchase.data.Purchase;
import com.feis.splitnings.features.purchase.data.dto.request.PurchaseCreateDto;
import com.feis.splitnings.features.purchase.data.dto.request.PurchaseUpdateDto;
import com.feis.splitnings.features.purchase.data.dto.response.PurchaseDto;
import com.feis.splitnings.features.purchase.data.dto.response.PurchasePageDto;
import com.feis.splitnings.features.purchase.mapper.PurchaseMapper;
import com.feis.splitnings.features.purchase.repository.PurchaseRepository;
import com.feis.splitnings.features.split.data.Split;
import com.feis.splitnings.features.split.service.SplitReadService;
import com.feis.splitnings.security.utils.SecurityUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PurchaseService extends AbstractService<Purchase, PurchaseDto, PurchaseCreateDto, PurchaseUpdateDto, PurchasePageDto, Integer> {
    private final SplitReadService splitReadService;

    public PurchaseService(PurchaseMapper purchaseMapper, PurchaseRepository purchaseRepository, SplitReadService splitReadService,
            PurchaseReadService purchaseReadService) {

        this.mapper = purchaseMapper;
        this.repository = purchaseRepository;
        this.splitReadService = splitReadService;
        this.resourceName = "Purchase";
    }

    @Override
    protected void validateCreateDto(PurchaseCreateDto createDto) {
        Split split = splitReadService.getById(createDto.getSplitId());

        if (!split.getActive()) {
            throw new ForbiddenOperationException("The linked split is inactive. Reactivate it before proceeding.");
        }
    }

    @Override
    protected void validateUpdateDto(PurchaseUpdateDto updateDto, Purchase existing) {
    }

    @Override
    protected void validateDelete(Integer id) {
    }

    @Override
    protected Integer getResourceId(Purchase entity) {
        return entity.getId();
    }

    @Override
    protected Page<Purchase> doFilter(Page<Purchase> entityPage) {
        if (entityPage == null || entityPage.isEmpty()) {
            return entityPage;
        }

        Integer jwtUserId = SecurityUtils.getJwtUserId();

        Set<Integer> userSplitIds = splitReadService.getAllByUserId(jwtUserId).stream()
                .map(Split::getId)
                .collect(Collectors.toSet());

        // Returning only purchases which belong to the requesting user
        List<Purchase> filtered = entityPage.get()
                .filter((entity) -> userSplitIds.contains(entity.getSplitId()))
                .collect(Collectors.toList());

        return new PageImpl<>(filtered, entityPage.getPageable(), filtered.size());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    protected void doCreate(Purchase toCreate) {
        Integer jwtUserId = SecurityUtils.getJwtUserId();

        toCreate.setCreatedBy(jwtUserId.toString());
        toCreate.setModifiedBy(jwtUserId.toString());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    protected void doUpdate(Purchase toUpdate, PurchaseUpdateDto updateDto) {
        Integer jwtUserId = SecurityUtils.getJwtUserId();

        toUpdate.setModifiedBy(jwtUserId.toString());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    protected void doDelete(Purchase entity) {
        Integer jwtUserId = SecurityUtils.getJwtUserId();

        entity.setModifiedBy(jwtUserId.toString());
    }
}

