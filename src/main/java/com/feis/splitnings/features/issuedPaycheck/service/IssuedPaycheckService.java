package com.feis.splitnings.features.issuedPaycheck.service;

import com.feis.splitnings.common.service.AbstractService;
import com.feis.splitnings.features.account.service.AccountReadService;
import com.feis.splitnings.features.issuedPaycheck.data.IssuedPaycheck;
import com.feis.splitnings.features.issuedPaycheck.data.dto.request.IssuedPaycheckCreateDto;
import com.feis.splitnings.features.issuedPaycheck.data.dto.request.IssuedPaycheckUpdateDto;
import com.feis.splitnings.features.issuedPaycheck.data.dto.response.IssuedPaycheckDto;
import com.feis.splitnings.features.issuedPaycheck.data.dto.response.IssuedPaycheckPageDto;
import com.feis.splitnings.features.issuedPaycheck.mapper.IssuedPaycheckMapper;
import com.feis.splitnings.features.issuedPaycheck.repository.IssuedPaycheckRepository;
import com.feis.splitnings.security.utils.SecurityUtils;

import java.time.Instant;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class IssuedPaycheckService extends AbstractService<IssuedPaycheck, IssuedPaycheckDto, IssuedPaycheckCreateDto, IssuedPaycheckUpdateDto, IssuedPaycheckPageDto, Integer> {
    private final AccountReadService accountReadService;

    public IssuedPaycheckService(IssuedPaycheckMapper issuedPaycheckMapper, IssuedPaycheckRepository issuedPaycheckRepository, AccountReadService accountReadService) {
        this.mapper = issuedPaycheckMapper;
        this.repository = issuedPaycheckRepository;
        this.resourceName = "Issued Paycheck";
        this.accountReadService = accountReadService;
    }

    @Override
    protected void validateCreateDto(IssuedPaycheckCreateDto createDto) {
        Integer jwtUserId = SecurityUtils.getJwtUserId();

        accountReadService.getByIdAndUserId(createDto.getAccountId(), jwtUserId);
    }

    @Override
    protected void validateDelete(Integer id) {
    }

    @Override
    protected void validateUpdateDto(IssuedPaycheckUpdateDto updateDto, IssuedPaycheck existing) {
    }

    @Override
    protected Integer getResourceId(IssuedPaycheck entity) {
        return entity.getId();
    }

    @Override
    protected void doCreate(IssuedPaycheck toCreate) {
        Integer jwtUserId = SecurityUtils.getJwtUserId();

        toCreate.setIssuedAt(Instant.now());
        toCreate.setSuccessful(true);

        toCreate.setCreatedBy(jwtUserId.toString());
        toCreate.setModifiedBy(jwtUserId.toString());
    }

    // NOTE: This operation is not expected to be triggered as
    // it's not intended to be updated. Implemented this just for
    // consistency.
    @Transactional(rollbackFor = Exception.class)
    @Override
    protected void doUpdate(IssuedPaycheck toUpdate, IssuedPaycheckUpdateDto updateDto) {
        Integer jwtUserId = SecurityUtils.getJwtUserId();
        toUpdate.setModifiedBy(jwtUserId.toString());
    }

    // NOTE: This operation is not expected to be triggered as
    // it's not intended to be deleted. Implemented this just for
    // consistency.
    @Transactional(rollbackFor = Exception.class)
    @Override
    protected void doDelete(IssuedPaycheck entity) {
        Integer jwtUserId = SecurityUtils.getJwtUserId();

        entity.setModifiedBy(jwtUserId.toString());
    }
}

