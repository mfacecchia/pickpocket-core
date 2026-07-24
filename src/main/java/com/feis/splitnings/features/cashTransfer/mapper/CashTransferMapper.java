package com.feis.splitnings.features.cashTransfer.mapper;

import com.feis.splitnings.common.mapper.BaseMapper;
import com.feis.splitnings.features.cashTransfer.data.CashTransfer;
import com.feis.splitnings.features.cashTransfer.data.dto.request.CashTransferCreateDto;
import com.feis.splitnings.features.cashTransfer.data.dto.request.CashTransferUpdateDto;
import com.feis.splitnings.features.cashTransfer.data.dto.response.CashTransferDto;
import com.feis.splitnings.features.cashTransfer.data.dto.response.CashTransferPageDto;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class CashTransferMapper implements BaseMapper<CashTransfer, CashTransferDto, CashTransferCreateDto, CashTransferUpdateDto, CashTransferPageDto> {

    @Override
    public CashTransfer mapToEntity(CashTransferDto dto) {
        CashTransfer entity = new CashTransfer();

        entity.setId(dto.getId());
        entity.setDescription(dto.getDescription());
        entity.setAmount(dto.getAmount());
        entity.setFromSplitId(dto.getFromSplitId());
        entity.setToSplitId(dto.getToSplitId());
        entity.setAccountId(dto.getAccountId());

        // Auditing
        entity.setCreatedBy(dto.getCreatedBy());
        entity.setCreatedDate(dto.getCreatedDate());
        entity.setModifiedBy(dto.getModifiedBy());
        entity.setModifiedDate(dto.getModifiedDate());

        return entity;
    }

    @Override
    public CashTransferDto mapToDto(CashTransfer entity) {
        CashTransferDto dto = new CashTransferDto();

        dto.setId(entity.getId());
        dto.setDescription(entity.getDescription());
        dto.setAmount(entity.getAmount());
        dto.setFromSplitId(entity.getFromSplitId());
        dto.setToSplitId(entity.getToSplitId());
        dto.setAccountId(entity.getAccountId());

        // Auditing
        dto.setCreatedBy(entity.getCreatedBy());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setModifiedBy(entity.getModifiedBy());
        dto.setModifiedDate(entity.getModifiedDate());

        return dto;
    }

    @Override
    public CashTransferPageDto mapPageToPageableDto(Page<CashTransferDto> page) {
        CashTransferPageDto pageDto = new CashTransferPageDto();

        pageDto.setContent(page.getContent());
        pageDto.setPageElements(page.getNumberOfElements());
        pageDto.setTotalCount(page.getTotalElements());

        return pageDto;
    }

    @Override
    public CashTransfer mapCreateDtoToEntity(CashTransferCreateDto createDto) {
        CashTransfer entity = new CashTransfer();

        entity.setDescription(createDto.getDescription());
        entity.setAmount(createDto.getAmount());
        entity.setFromSplitId(createDto.getFromSplitId());
        entity.setToSplitId(createDto.getToSplitId());

        return entity;
    }

    @Override
    public void mapUpdateDtoToEntity(CashTransferUpdateDto updateDto, CashTransfer toUpdate) {
        toUpdate.setDescription(updateDto.getDescription());
    }

    @Override
    public CashTransferUpdateDto mapToUpdateDto(CashTransfer entity) {
        CashTransferUpdateDto updateDto = new CashTransferUpdateDto();

        updateDto.setDescription(entity.getDescription());

        return updateDto;
    }
}

