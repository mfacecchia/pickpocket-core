package com.feis.splitnings.features.issuedPaycheck.data.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class IssuedPaycheckCreateDto {
    @NotNull(message = "{field.required}")
    @DecimalMin(value = "1", message = "{field.min}")
    private Double issuedAmount;

    @NotNull(message = "{field.required}")
    private Integer accountId;

    private Integer splitId;
}
