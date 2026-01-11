package com.feis.splitnings.features.user.data.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserCreateDto {
    @NotBlank(message = "#{ValidationMessage.NOT_BLANK.getMessage()}")
    @Email(message = "#{ValidationMessage.VALID_EMAIL.getMessage()}")
    private String email;

    @NotBlank(message = "#{ValidationMessage.NOT_BLANK.getMessage()}")
    private String firstName;

    private String middleName;

    @NotBlank(message = "#{ValidationMessage.NOT_BLANK.getMessage()}")
    private String lastName;

    @NotBlank(message = "#{ValidationMessage.NOT_BLANK.getMessage()}")
    private String externalId;
}
