package com.feis.splitnings.features.goal.data.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GoalCreateDto {
    @NotBlank(message = "{field.blank}")
    private String name;

    @NotNull(message = "{field.required}")
    @DecimalMin(value = "0.01", message = "{field.min}")
    private Double targetAmount;

    @NotNull(message = "{field.required}")
    private Integer splitId;
}
