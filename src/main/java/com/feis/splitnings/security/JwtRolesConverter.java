package com.feis.splitnings.security;

import java.util.Collection;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

public class JwtRolesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {
    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        // TODO: Implement roles obtaining
        throw new NotImplementedException("Implement this");
    }
}
