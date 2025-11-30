package com.feis.splitnings.features.user.service;

import org.springframework.stereotype.Service;

import com.feis.splitnings.common.exception.ConflictException;
import com.feis.splitnings.common.service.AbstractService;
import com.feis.splitnings.features.user.data.User;
import com.feis.splitnings.features.user.data.dto.request.UserCreateDto;
import com.feis.splitnings.features.user.data.dto.request.UserUpdateDto;
import com.feis.splitnings.features.user.data.dto.response.UserDto;
import com.feis.splitnings.features.user.data.dto.response.UserPageDto;
import com.feis.splitnings.features.user.mapper.UserMapper;
import com.feis.splitnings.features.user.repository.UserRepository;

@Service
public class UserService extends AbstractService<User, UserDto, UserCreateDto, UserUpdateDto, UserPageDto, Integer> {

    public UserService(UserMapper userMapper, UserRepository userRepository) {
        this.mapper = userMapper;
        this.repository = userRepository;
        this.resourceName = "User";
    }

    @Override
    protected void validateCreateDto(UserCreateDto createDto) {
        if (((UserRepository) repository).existsByEmail(createDto.getEmail())) {
            throw new ConflictException(resourceName, "Email", createDto.getEmail());
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
}
