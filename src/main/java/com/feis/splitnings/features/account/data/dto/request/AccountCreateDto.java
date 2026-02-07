package com.feis.splitnings.features.account.data.dto.request;

import jakarta.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AccountCreateDto {
    @NotBlank(message = "{field.blank}")
    private String name;

    private String description;
}
