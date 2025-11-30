package com.feis.splitnings.security.annotations;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.NotImplementedException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class HasRoleAspect {

    @Around("@annotation(com.feis.splitnings.security.annotations.HasRole)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();

        HasRole annotation = method.getAnnotation(HasRole.class);

        List<String> rolesToCheck = List.of(annotation.rolesToCheck());

        if (rolesToCheck == null || rolesToCheck.isEmpty()) {
            return joinPoint.proceed();
        }

        Set<String> userRoles = getUserRoles();

        boolean containsRole = rolesToCheck.stream().anyMatch((role) -> userRoles.contains(role));

        if (!containsRole) {
            throw new AuthorizationDeniedException("You are not authorized to access this resource");
        }

        return joinPoint.proceed();
    }

    private Set<String> getUserRoles() {
        throw new NotImplementedException("Implement this logic.");
    }
}
