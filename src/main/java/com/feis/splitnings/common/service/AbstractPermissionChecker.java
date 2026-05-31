package com.feis.splitnings.common.service;

import com.feis.splitnings.common.exception.ForbiddenOperationException;

public abstract class AbstractPermissionChecker<ENTITY> {

    protected abstract Boolean isResourceOwner(Integer userId, ENTITY entity);

    public void checkOwnership(Integer userId, ENTITY entity) {
        if (!isResourceOwner(userId, entity)) {
            throw new ForbiddenOperationException();
        }
    }

    public void checkReadPermission(Integer userId, ENTITY entity) {
        checkOwnership(userId, entity);
    }

    public void checkUpdatePermission(Integer userId, ENTITY entity) {
        checkOwnership(userId, entity);
    }

    public void checkDeletePermission(Integer userId, ENTITY entity) {
        checkOwnership(userId, entity);
    }
}

