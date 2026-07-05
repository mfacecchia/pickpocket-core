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

@Service
public class IssuedPaycheckService extends AbstractService<IssuedPaycheck, IssuedPaycheckDto, IssuedPaycheckCreateDto, IssuedPaycheckUpdateDto, IssuedPaycheckPageDto, Integer> {
    private final AccountReadService accountReadService;

    public IssuedPaycheckService(IssuedPaycheckMapper issuedPaycheckMapper, IssuedPaycheckRepository issuedPaycheckRepository,
            AccountReadService accountReadService, IssuedPaycheckPermissionChecker issuedPaycheckPermissionChecker) {

        this.mapper = issuedPaycheckMapper;
        this.repository = issuedPaycheckRepository;
        this.accountReadService = accountReadService;
        this.permissionChecker = issuedPaycheckPermissionChecker;
        this.resourceName = "Issued Paycheck";
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
    protected void doCreate(IssuedPaycheck toCreate) {
        toCreate.setIssuedAt(Instant.now());
        toCreate.setSuccessful(true);
    }

    // TODO: What about overriding the update method
    // to completely disable it?
}

