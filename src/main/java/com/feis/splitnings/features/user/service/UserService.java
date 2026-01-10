package com.feis.splitnings.features.user.service;

import com.feis.splitnings.common.exception.AlreadyRegisteredException;
import com.feis.splitnings.common.service.AbstractService;
import com.feis.splitnings.features.user.data.User;
import com.feis.splitnings.features.user.data.dto.request.UserCreateDto;
import com.feis.splitnings.features.user.data.dto.request.UserUpdateDto;
import com.feis.splitnings.features.user.data.dto.response.UserDto;
import com.feis.splitnings.features.user.data.dto.response.UserPageDto;
import com.feis.splitnings.features.user.mapper.UserMapper;
import com.feis.splitnings.features.user.repository.UserRepository;
import com.feis.splitnings.security.utils.SecurityUtils;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService extends AbstractService<User, UserDto, UserCreateDto, UserUpdateDto, UserPageDto, Integer> {
    @Autowired
    private Keycloak keycloak;

    public UserService(UserMapper userMapper, UserRepository userRepository) {
        this.mapper = userMapper;
        this.repository = userRepository;
        this.resourceName = "User";
    }

    @Transactional
    public UserDto createFromJwt() {
        UserCreateDto createDto = ((UserMapper) mapper).mapJwtDtoToCreateDto();

        UserDto dto = create(createDto);

        // Updating KC user with provided userId
        // TODO: Place this in a KeycloakService or some'
        UserRepresentation kcUser = keycloak.realm("splitnings").users().get("899a3748-a7f5-4146-b8c6-026680d3f300").toRepresentation();
        Map<String, List<String>> kcUserAttributes = kcUser.getAttributes();
        if (kcUserAttributes == null) {
            kcUserAttributes = new  HashMap<>();
        }
        kcUserAttributes.put("userId", List.of(dto.getId().toString()));
        kcUser.setAttributes(kcUserAttributes);
        keycloak.realm("splitnings").users().get(SecurityUtils.getJwtDto().getExternalId()).update(kcUser);

        return dto;
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
