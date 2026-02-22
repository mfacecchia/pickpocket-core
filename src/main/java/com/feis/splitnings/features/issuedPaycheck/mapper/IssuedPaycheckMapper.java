package com.feis.splitnings.features.issuedPaycheck.mapper;

import com.feis.splitnings.common.mapper.BaseMapper;
import com.feis.splitnings.features.issuedPaycheck.data.IssuedPaycheck;
import com.feis.splitnings.features.issuedPaycheck.data.dto.request.IssuedPaycheckCreateDto;
import com.feis.splitnings.features.issuedPaycheck.data.dto.request.IssuedPaycheckUpdateDto;
import com.feis.splitnings.features.issuedPaycheck.data.dto.response.IssuedPaycheckDto;
import com.feis.splitnings.features.issuedPaycheck.data.dto.response.IssuedPaycheckPageDto;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class IssuedPaycheckMapper implements BaseMapper<IssuedPaycheck, IssuedPaycheckDto, IssuedPaycheckCreateDto, IssuedPaycheckUpdateDto, IssuedPaycheckPageDto> {

    @Override
    public IssuedPaycheck mapToEntity(IssuedPaycheckDto dto) {
        IssuedPaycheck entity = new IssuedPaycheck();

        entity.setId(dto.getId());
        entity.setIssuedAmount(dto.getIssuedAmount());
        entity.setIssuedAt(dto.getIssuedAt());
        entity.setSuccessful(dto.getSuccessful());
        entity.setPaycheckId(dto.getPaycheckId());
        entity.setAccountId(dto.getAccountId());

        // Auditing
        entity.setCreatedBy(dto.getCreatedBy());
        entity.setCreatedDate(dto.getCreatedDate());
        entity.setDeleted(dto.getDeleted());
        entity.setModifiedBy(dto.getModifiedBy());
        entity.setModifiedDate(dto.getModifiedDate());

        return entity;
    }

    @Override
    public IssuedPaycheckDto mapToDto(IssuedPaycheck entity) {
        IssuedPaycheckDto dto = new IssuedPaycheckDto();

        dto.setId(entity.getId());
        dto.setIssuedAmount(entity.getIssuedAmount());
        dto.setIssuedAt(entity.getIssuedAt());
        dto.setSuccessful(entity.getSuccessful());
        dto.setPaycheckId(entity.getPaycheckId());
        dto.setAccountId(entity.getAccountId());

        // Auditing
        dto.setCreatedBy(entity.getCreatedBy());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setDeleted(entity.getDeleted());
        dto.setModifiedBy(entity.getModifiedBy());
        dto.setModifiedDate(entity.getModifiedDate());

        return dto;
    }

    @Override
    public IssuedPaycheckPageDto mapPageToPageableDto(Page<IssuedPaycheckDto> page) {
        IssuedPaycheckPageDto pageDto = new IssuedPaycheckPageDto();

        pageDto.setContent(page.getContent());
        pageDto.setPageElements(page.getNumberOfElements());
        pageDto.setTotalCount(page.getTotalElements());

        return pageDto;
    }

    @Override
    public IssuedPaycheck mapCreateDtoToEntity(IssuedPaycheckCreateDto createDto) {
        IssuedPaycheck entity = new IssuedPaycheck();

        entity.setIssuedAmount(createDto.getIssuedAmount());
        entity.setAccountId(createDto.getAccountId());

        return entity;
    }

    @Override
    public void mapUpdateDtoToEntity(IssuedPaycheckUpdateDto updateDto, IssuedPaycheck toUpdate) {
    }

    @Override
    public IssuedPaycheckUpdateDto mapToUpdateDto(IssuedPaycheck entity) {
        IssuedPaycheckUpdateDto updateDto = new IssuedPaycheckUpdateDto();

        return updateDto;
    }
}
