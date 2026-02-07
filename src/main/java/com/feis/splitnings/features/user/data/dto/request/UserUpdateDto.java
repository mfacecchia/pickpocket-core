package com.feis.splitnings.features.user.data.dto.request;

import com.feis.splitnings.common.data.dto.request.BaseUpdateDto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserUpdateDto extends BaseUpdateDto {
    @NotBlank(message = "{field.blank}")
    @Email(message = "{field.email}")
    private String email;

    @NotBlank(message = "{field.blank}")
    private String firstName;

    private String middleName;

    @NotBlank(message = "{field.blank}")
    private String lastName;
}
