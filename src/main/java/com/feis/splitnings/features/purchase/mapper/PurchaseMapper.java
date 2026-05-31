package com.feis.splitnings.features.purchase.mapper;

import com.feis.splitnings.common.mapper.BaseMapper;
import com.feis.splitnings.features.purchase.data.Purchase;
import com.feis.splitnings.features.purchase.data.dto.request.PurchaseCreateDto;
import com.feis.splitnings.features.purchase.data.dto.request.PurchaseUpdateDto;
import com.feis.splitnings.features.purchase.data.dto.response.PurchaseDto;
import com.feis.splitnings.features.purchase.data.dto.response.PurchasePageDto;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class PurchaseMapper implements BaseMapper<Purchase, PurchaseDto, PurchaseCreateDto, PurchaseUpdateDto, PurchasePageDto> {

    @Override
    public Purchase mapToEntity(PurchaseDto dto) {
        Purchase entity = new Purchase();

        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setAmount(dto.getAmount());
        entity.setCategory(dto.getCategory());
        entity.setGoalId(dto.getGoalId());
        entity.setSplitId(dto.getSplitId());

        // Auditing
        entity.setCreatedBy(dto.getCreatedBy());
        entity.setCreatedDate(dto.getCreatedDate());
        entity.setDeleted(dto.getDeleted());
        entity.setModifiedBy(dto.getModifiedBy());
        entity.setModifiedDate(dto.getModifiedDate());

        return entity;
    }

    @Override
    public PurchaseDto mapToDto(Purchase entity) {
        PurchaseDto dto = new PurchaseDto();

        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setAmount(entity.getAmount());
        dto.setCategory(entity.getCategory());
        dto.setGoalId(entity.getGoalId());
        dto.setSplitId(entity.getSplitId());

        // Auditing
        dto.setCreatedBy(entity.getCreatedBy());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setDeleted(entity.getDeleted());
        dto.setModifiedBy(entity.getModifiedBy());
        dto.setModifiedDate(entity.getModifiedDate());

        return dto;
    }

    @Override
    public PurchasePageDto mapPageToPageableDto(Page<PurchaseDto> page) {
        PurchasePageDto pageDto = new PurchasePageDto();

        pageDto.setContent(page.getContent());
        pageDto.setPageElements(page.getNumberOfElements());
        pageDto.setTotalCount(page.getTotalElements());

        return pageDto;
    }

    @Override
    public Purchase mapCreateDtoToEntity(PurchaseCreateDto createDto) {
        Purchase entity = new Purchase();

        entity.setName(createDto.getName());
        entity.setDescription(createDto.getDescription());
        entity.setAmount(createDto.getAmount());
        entity.setSplitId(createDto.getSplitId());

        return entity;
    }

    @Override
    public void mapUpdateDtoToEntity(PurchaseUpdateDto updateDto, Purchase toUpdate) {
        toUpdate.setName(updateDto.getName());
        toUpdate.setDescription(updateDto.getDescription());
    }

    @Override
    public PurchaseUpdateDto mapToUpdateDto(Purchase entity) {
        PurchaseUpdateDto updateDto = new PurchaseUpdateDto();

        updateDto.setName(entity.getName());
        updateDto.setDescription(entity.getDescription());

        return updateDto;
    }
}

