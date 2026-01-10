package com.feis.splitnings.security.data;

import com.feis.splitnings.security.data.dto.response.JwtDto;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

public class AuthenticationToken extends AbstractAuthenticationToken {
    private Jwt jwt;
    private JwtDto jwtDto;

    public AuthenticationToken(Jwt jwt, JwtDto jwtDto, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.jwtDto = jwtDto;
        this.jwt = jwt;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return jwt;
    }

    @Override
    public Object getPrincipal() {
        return jwtDto;
    }
}
