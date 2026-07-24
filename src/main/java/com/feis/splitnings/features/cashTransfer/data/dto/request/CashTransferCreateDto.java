package com.feis.splitnings.features.cashTransfer.data.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CashTransferCreateDto {
    @NotBlank(message = "{field.blank}")
    private String description;

    @NotNull(message = "{field.required}")
    @DecimalMin(value = "0.01", message = "{field.min}")
    private Double amount;

    @NotNull(message = "{field.required}")
    private Integer fromSplitId;

    @NotNull(message = "{field.required}")
    private Integer toSplitId;
}
