package com.feis.splitnings.security.mapper;

import org.springframework.security.oauth2.jwt.Jwt;

import com.feis.splitnings.security.data.dto.response.JwtDto;

public class JwtMapper {

    public static JwtDto mapToDto(Jwt jwt) {
        JwtDto dto = new JwtDto();

        dto.setUserId(jwt.getClaimAsString("userId"));
        dto.setFirstName(jwt.getClaimAsString("firstName"));
        dto.setMiddleName(jwt.getClaimAsString("middleName"));
        dto.setLastName(jwt.getClaimAsString("lastName"));
        dto.setFullName(jwt.getClaimAsString("name"));
        dto.setEmail(jwt.getClaimAsString("email"));

        return dto;
    }
}
