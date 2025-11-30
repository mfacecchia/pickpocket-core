package com.feis.splitnings.features.user.data.dto.request;

import com.feis.splitnings.common.data.dto.BaseUpdateDto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserUpdateDto extends BaseUpdateDto {
    @NotBlank(message = "#{ValidationMessage.NOT_BLANK.getMessage()}")
    @Email(message = "#{ValidationMessage.VALID_EMAIL.getMessage()}")
    private String email;

    @NotBlank(message = "#{ValidationMessage.NOT_BLANK.getMessage()}")
    private String firstName;

    @NotBlank(message = "#{ValidationMessage.NOT_BLANK.getMessage()}")
    private String middleName;

    @NotBlank(message = "#{ValidationMessage.NOT_BLANK.getMessage()}")
    private String lastName;
}
