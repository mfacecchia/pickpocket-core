package com.feis.splitnings.security.data.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtDto {
    private String userId;
    private String externalId;
    private String firstName;
    private String middleName;
    private String lastName;
    private String fullName;
    private String email;
}
