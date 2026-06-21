package com.feis.splitnings.features.goal.mapper;

import com.feis.splitnings.common.mapper.BaseMapper;
import com.feis.splitnings.features.goal.data.Goal;
import com.feis.splitnings.features.goal.data.dto.request.GoalCreateDto;
import com.feis.splitnings.features.goal.data.dto.request.GoalUpdateDto;
import com.feis.splitnings.features.goal.data.dto.response.GoalDto;
import com.feis.splitnings.features.goal.data.dto.response.GoalPageDto;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class GoalMapper implements BaseMapper<Goal, GoalDto, GoalCreateDto, GoalUpdateDto, GoalPageDto> {

    @Override
    public Goal mapToEntity(GoalDto dto) {
        Goal entity = new Goal();

        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setTargetAmount(dto.getTargetAmount());
        entity.setCurrentAmount(dto.getCurrentAmount());
        entity.setCompletedAt(dto.getCompletedAt());
        entity.setSplitId(dto.getSplitId());

        // Auditing
        entity.setCreatedBy(dto.getCreatedBy());
        entity.setCreatedDate(dto.getCreatedDate());
        entity.setModifiedBy(dto.getModifiedBy());
        entity.setModifiedDate(dto.getModifiedDate());

        return entity;
    }

    @Override
    public GoalDto mapToDto(Goal entity) {
        GoalDto dto = new GoalDto();

        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setTargetAmount(entity.getTargetAmount());
        dto.setCurrentAmount(entity.getCurrentAmount());
        dto.setCompletedAt(entity.getCompletedAt());
        dto.setCompleted(entity.getCompleted());
        dto.setSplitId(entity.getSplitId());

        // Auditing
        dto.setCreatedBy(entity.getCreatedBy());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setModifiedBy(entity.getModifiedBy());
        dto.setModifiedDate(entity.getModifiedDate());

        return dto;
    }

    @Override
    public GoalPageDto mapPageToPageableDto(Page<GoalDto> page) {
        GoalPageDto pageDto = new GoalPageDto();

        pageDto.setContent(page.getContent());
        pageDto.setPageElements(page.getNumberOfElements());
        pageDto.setTotalCount(page.getTotalElements());

        return pageDto;
    }

    @Override
    public Goal mapCreateDtoToEntity(GoalCreateDto createDto) {
        Goal entity = new Goal();

        entity.setName(createDto.getName());
        entity.setTargetAmount(createDto.getTargetAmount());
        entity.setSplitId(createDto.getSplitId());

        return entity;
    }

    @Override
    public void mapUpdateDtoToEntity(GoalUpdateDto updateDto, Goal toUpdate) {
        toUpdate.setName(updateDto.getName());
        toUpdate.setSplitId(updateDto.getSplitId());
        toUpdate.setTargetAmount(updateDto.getTargetAmount());
    }

    @Override
    public GoalUpdateDto mapToUpdateDto(Goal entity) {
        GoalUpdateDto updateDto = new GoalUpdateDto();

        updateDto.setName(entity.getName());
        updateDto.setTargetAmount(entity.getTargetAmount());
        updateDto.setSplitId(entity.getSplitId());

        return updateDto;
    }
}
