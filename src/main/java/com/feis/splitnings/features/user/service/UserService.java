package com.feis.splitnings.features.user.service;

import com.feis.splitnings.common.enums.KeycloakUserAttribute;
import com.feis.splitnings.common.exception.AlreadyRegisteredException;
import com.feis.splitnings.common.service.AbstractService;
import com.feis.splitnings.common.service.KeycloakService;
import com.feis.splitnings.features.user.data.User;
import com.feis.splitnings.features.user.data.dto.request.UserCreateDto;
import com.feis.splitnings.features.user.data.dto.request.UserUpdateDto;
import com.feis.splitnings.features.user.data.dto.response.UserDto;
import com.feis.splitnings.features.user.data.dto.response.UserPageDto;
import com.feis.splitnings.features.user.mapper.UserMapper;
import com.feis.splitnings.features.user.repository.UserRepository;
import com.feis.splitnings.security.utils.SecurityUtils;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService extends AbstractService<User, UserDto, UserCreateDto, UserUpdateDto, UserPageDto, Integer> {
    @Autowired
    private KeycloakService keycloakService;

    public UserService(UserMapper userMapper, UserRepository userRepository) {
        this.mapper = userMapper;
        this.repository = userRepository;
        this.resourceName = "User";
    }

    @Transactional(rollbackFor = Exception.class)
    public UserDto createFromJwt() {
        UserCreateDto createDto = ((UserMapper) mapper).mapJwtDtoToCreateDto();

        UserDto dto = create(createDto);

        // Updating KC user with generated userId
        Map<KeycloakUserAttribute, List<String>> kcUserAttributes = Map.of(KeycloakUserAttribute.USER_ID, List.of(dto.getId().toString()));
        keycloakService.upsertUserAttributes(SecurityUtils.getJwtDto().getExternalId(), kcUserAttributes);

        return dto;
    }

    public KeycloakService getKeycloakService() {
        return keycloakService;
    }

    @Override
    protected void validateCreateDto(UserCreateDto createDto) {
        if (((UserRepository) repository).existsByEmail(createDto.getEmail())) {
            throw new AlreadyRegisteredException("Email already exists");
        }

        if (((UserRepository) repository).existsByExternalId(createDto.getExternalId())) {
            throw new AlreadyRegisteredException("ExternalId already exists");
        }
    }

    @Override
    protected void validateDelete(Integer id) {
    }

    @Override
    protected void validateUpdateDto(UserUpdateDto updateDto, User existing) {
    }

    @Override
    protected Integer getResourceId(User entity) {
        return entity.getId();
    }

    @Override
    protected void doCreate(User toCreate) {
        toCreate.setLastLogin(Instant.now());
    }
}
