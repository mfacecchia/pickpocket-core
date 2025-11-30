package com.feis.splitnings.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import com.feis.splitnings.common.exception.AuthenticationException;

public class SecurityUtils {

    private SecurityUtils() {
    }

    public static Jwt getCurrentJwt() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken jwtAuth) {
            return jwtAuth.getToken();
        }
        throw new AuthenticationException();
    }

    public static String getCurrentJwtValue() {
        Jwt jwt = getCurrentJwt();
        return jwt.getTokenValue();
    }
}
