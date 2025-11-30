package com.feis.splitnings.common.mapper;

import org.springframework.data.domain.Page;

public interface BaseMapper<ENTITY, GET_DTO, CREATE_DTO, UPDATE_DTO, PAGEABLE_DTO> {

    ENTITY mapToEntity(GET_DTO dto);

    GET_DTO mapToDto(ENTITY entity);

    PAGEABLE_DTO mapPageToPageableDto(Page<GET_DTO> entity);

    ENTITY mapCreateDtoToEntity(CREATE_DTO dto);

    void mapUpdateDtoToEntity(UPDATE_DTO updateDto, ENTITY toUpdate);

    UPDATE_DTO mapToUpdateDto(ENTITY entity);
}
