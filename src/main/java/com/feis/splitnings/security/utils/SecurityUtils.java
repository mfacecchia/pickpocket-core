package com.feis.splitnings.security.utils;

import com.feis.splitnings.security.data.dto.response.JwtDto;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

public class SecurityUtils {

    private SecurityUtils() {
    }

    public static Jwt getJwt() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (Jwt) authentication.getCredentials();
    }

    public static JwtDto getJwtDto() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (JwtDto) authentication.getPrincipal();
    }

    public static String getJwtValue() {
        Jwt jwt = getJwt();
        return jwt.getTokenValue();
    }
}
