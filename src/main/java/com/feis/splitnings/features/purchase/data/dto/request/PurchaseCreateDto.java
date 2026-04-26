package com.feis.splitnings.features.purchase.data.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class PurchaseCreateDto {
    @NotBlank(message = "{field.blank}")
    @Size(max = 255, message = "{field.max}")
    private String name;

    @Size(max = 255, message = "{field.max}")
    private String description;

    @NotNull(message = "{field.required}")
    @DecimalMin(value = "0.01", message = "{field.min}")
    private Double amount;

    @NotNull(message = "{field.required}")
    private Integer splitId;
}
