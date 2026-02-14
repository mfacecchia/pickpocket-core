package com.feis.splitnings.features.split.data.dto.request;

import com.feis.splitnings.common.data.dto.request.BaseUpdateDto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SplitUpdateDto extends BaseUpdateDto {
    @NotBlank(message = "{field.blank}")
    private String name;

    @NotNull(message = "{field.required}")
    @Min(value = 1, message = "{field.min}")
    @Max(value = 100, message = "{field.max}")
    private Short splitPercentage;
}
