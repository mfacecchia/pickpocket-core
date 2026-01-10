package com.feis.splitnings.security.mapper;

import org.springframework.security.oauth2.jwt.Jwt;

import com.feis.splitnings.security.data.dto.response.JwtDto;

public class JwtMapper {

    public static JwtDto mapToDto(Jwt jwt) {
        JwtDto dto = new JwtDto();

        dto.setUserId(jwt.getClaimAsString("user_id"));
        dto.setExternalId(jwt.getClaimAsString("sub"));
        dto.setFirstName(jwt.getClaimAsString("given_name"));
        dto.setMiddleName(jwt.getClaimAsString("middle_name"));
        dto.setLastName(jwt.getClaimAsString("family_name"));
        dto.setFullName(jwt.getClaimAsString("name"));
        dto.setEmail(jwt.getClaimAsString("email"));

        return dto;
    }
}
