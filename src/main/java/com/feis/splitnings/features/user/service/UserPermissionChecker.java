package com.feis.splitnings.features.user.service;

import com.feis.splitnings.common.service.AbstractPermissionChecker;
import com.feis.splitnings.features.user.data.User;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserPermissionChecker extends AbstractPermissionChecker<User> {

    @Override
    protected Boolean isResourceOwner(Integer userId, User entity) {
        return userId.equals(entity.getId());
    }
}
