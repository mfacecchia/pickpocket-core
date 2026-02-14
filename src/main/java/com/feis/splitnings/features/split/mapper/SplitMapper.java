package com.feis.splitnings.features.split.mapper;

import com.feis.splitnings.common.mapper.BaseMapper;
import com.feis.splitnings.features.split.data.Split;
import com.feis.splitnings.features.split.data.dto.request.SplitCreateDto;
import com.feis.splitnings.features.split.data.dto.request.SplitUpdateDto;
import com.feis.splitnings.features.split.data.dto.response.SplitDto;
import com.feis.splitnings.features.split.data.dto.response.SplitPageDto;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class SplitMapper implements BaseMapper<Split, SplitDto, SplitCreateDto, SplitUpdateDto, SplitPageDto> {

    @Override
    public Split mapToEntity(SplitDto dto) {
        Split entity = new Split();

        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setTheoreticalAmount(dto.getTheoreticalAmount());
        entity.setAvailableAmount(dto.getAvailableAmount());
        entity.setSplitPercentage(dto.getSplitPercentage());
        entity.setActive(dto.getActive());
        entity.setIsDefault(dto.getIsDefault());
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
    public SplitDto mapToDto(Split entity) {
        SplitDto dto = new SplitDto();

        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setTheoreticalAmount(entity.getTheoreticalAmount());
        dto.setAvailableAmount(entity.getAvailableAmount());
        dto.setSplitPercentage(entity.getSplitPercentage());
        dto.setActive(entity.getActive());
        dto.setIsDefault(entity.getIsDefault());
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
    public SplitPageDto mapPageToPageableDto(Page<SplitDto> page) {
        SplitPageDto pageDto = new SplitPageDto();

        pageDto.setContent(page.getContent());
        pageDto.setPageElements(page.getNumberOfElements());
        pageDto.setTotalCount(page.getTotalElements());

        return pageDto;
    }

    @Override
    public Split mapCreateDtoToEntity(SplitCreateDto createDto) {
        Split entity = new Split();

        entity.setName(createDto.getName());
        entity.setSplitPercentage(createDto.getSplitPercentage());
        entity.setAccountId(createDto.getAccountId());

        return entity;
    }

    @Override
    public void mapUpdateDtoToEntity(SplitUpdateDto updateDto, Split toUpdate) {
        toUpdate.setName(updateDto.getName());
        toUpdate.setSplitPercentage(updateDto.getSplitPercentage());
    }

    @Override
    public SplitUpdateDto mapToUpdateDto(Split entity) {
        SplitUpdateDto updateDto = new SplitUpdateDto();

        updateDto.setName(entity.getName());
        updateDto.setSplitPercentage(entity.getSplitPercentage());

        return updateDto;
    }
}
