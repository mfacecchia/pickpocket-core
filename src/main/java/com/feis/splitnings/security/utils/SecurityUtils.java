package com.feis.splitnings.security.utils;

import com.feis.splitnings.security.data.dto.response.JwtDto;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

public class SecurityUtils {

    public static Jwt getCurrentJwt() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (Jwt) authentication.getCredentials();
    }

    public static JwtDto getCurrentJwtDto() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (JwtDto) authentication.getPrincipal();
    }

    public static String getCurrentJwtValue() {
        Jwt jwt = getCurrentJwt();
        return jwt.getTokenValue();
    }
}
