package com.feis.splitnings.features.purchase.data.dto.request;

import com.feis.splitnings.common.data.dto.request.BaseUpdateDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PurchaseUpdateDto extends BaseUpdateDto {
    @NotBlank(message = "{field.blank}")
    @Size(max = 255, message = "{field.max}")
    private String name;

    @Size(max = 255, message = "{field.max}")
    private String description;
}
