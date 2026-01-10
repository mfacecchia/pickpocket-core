package com.feis.splitnings.features.user.mapper;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.feis.splitnings.common.mapper.BaseMapper;
import com.feis.splitnings.features.user.data.User;
import com.feis.splitnings.features.user.data.dto.request.UserCreateDto;
import com.feis.splitnings.features.user.data.dto.request.UserUpdateDto;
import com.feis.splitnings.features.user.data.dto.response.UserDto;
import com.feis.splitnings.features.user.data.dto.response.UserPageDto;
import com.feis.splitnings.security.data.dto.response.JwtDto;
import com.feis.splitnings.security.utils.SecurityUtils;

@Component
public class UserMapper implements BaseMapper<User, UserDto, UserCreateDto, UserUpdateDto, UserPageDto> {

    @Override
    public User mapToEntity(UserDto dto) {
        User entity = new User();

        entity.setId(dto.getId());
        entity.setFirstName(dto.getFirstName());
        entity.setMiddleName(dto.getMiddleName());
        entity.setLastName(dto.getLastName());
        entity.setEmail(dto.getEmail());

        // Auditing
        entity.setCreatedBy(dto.getCreatedBy());
        entity.setCreatedDate(dto.getCreatedDate());
        entity.setDeleted(dto.getDeleted());
        entity.setModifiedBy(dto.getModifiedBy());
        entity.setModifiedDate(dto.getModifiedDate());

        return entity;
    }

    @Override
    public UserDto mapToDto(User entity) {
        UserDto dto = new UserDto();

        dto.setId(entity.getId());
        dto.setFirstName(entity.getFirstName());
        dto.setMiddleName(entity.getMiddleName());
        dto.setLastName(entity.getLastName());
        dto.setEmail(entity.getEmail());

        // Auditing
        dto.setCreatedBy(entity.getCreatedBy());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setDeleted(entity.getDeleted());
        dto.setModifiedBy(entity.getModifiedBy());
        dto.setModifiedDate(entity.getModifiedDate());

        return dto;
    }

    @Override
    public UserPageDto mapPageToPageableDto(Page<UserDto> page) {
        UserPageDto pageDto = new UserPageDto();

        pageDto.setContent(page.getContent());
        pageDto.setPageElements(page.getNumberOfElements());
        pageDto.setTotalCount(page.getTotalElements());

        return pageDto;
    }

    @Override
    public User mapCreateDtoToEntity(UserCreateDto createDto) {
        User entity = new User();

        entity.setFirstName(createDto.getFirstName());
        entity.setMiddleName(createDto.getMiddleName());
        entity.setLastName(createDto.getLastName());
        entity.setEmail(createDto.getEmail());
        entity.setExternalId(createDto.getExternalId());

        return entity;
    }

    @Override
    public void mapUpdateDtoToEntity(UserUpdateDto updateDto, User toUpdate) {
        toUpdate.setFirstName(updateDto.getFirstName());
        toUpdate.setMiddleName(updateDto.getMiddleName());
        toUpdate.setLastName(updateDto.getLastName());
        toUpdate.setEmail(updateDto.getEmail());
    }

    @Override
    public UserUpdateDto mapToUpdateDto(User entity) {
        UserUpdateDto updateDto = new UserUpdateDto();

        updateDto.setFirstName(entity.getFirstName());
        updateDto.setMiddleName(entity.getMiddleName());
        updateDto.setLastName(entity.getLastName());
        updateDto.setEmail(entity.getEmail());

        return updateDto;
    }

    public UserCreateDto mapJwtDtoToCreateDto() {
        JwtDto jwtDto = SecurityUtils.getJwtDto();

        UserCreateDto createDto = new UserCreateDto();
        createDto.setEmail(jwtDto.getEmail());
        createDto.setFirstName(jwtDto.getFirstName());
        createDto.setMiddleName(jwtDto.getMiddleName());
        createDto.setLastName(jwtDto.getLastName());
        createDto.setExternalId(jwtDto.getExternalId());

        return createDto;
    }
}
