package com.feis.splitnings.security.config;

import com.feis.splitnings.security.data.AuthenticationToken;
import com.feis.splitnings.security.data.dto.response.JwtDto;
import com.feis.splitnings.security.mapper.JwtMapper;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
public class JwtConverter implements Converter<Jwt, AuthenticationToken> {
    @Override
    public AuthenticationToken convert(Jwt jwt) {
        JwtDto jwtDto = JwtMapper.mapToDto(jwt);

        // No roles for now stored in token. It's ok to set null in this case
        return new AuthenticationToken(jwt, jwtDto, null);
    }
}
