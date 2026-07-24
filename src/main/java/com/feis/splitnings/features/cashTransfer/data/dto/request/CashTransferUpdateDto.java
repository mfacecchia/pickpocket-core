package com.feis.splitnings.features.cashTransfer.data.dto.request;

import jakarta.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CashTransferUpdateDto {
    @NotBlank(message = "{field.blank}")
    private String description;
}
