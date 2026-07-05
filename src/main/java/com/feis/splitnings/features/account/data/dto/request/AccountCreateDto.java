package com.feis.splitnings.features.account.data.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

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

    @NotNull(message = "{field.required}")
    @DecimalMin(value = "0.00", message = "{field.min}")
    private Double initialAmount;
}
