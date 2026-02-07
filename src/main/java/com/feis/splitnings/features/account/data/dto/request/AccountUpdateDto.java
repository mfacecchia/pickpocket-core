package com.feis.splitnings.features.account.data.dto.request;

import com.feis.splitnings.common.data.dto.request.BaseUpdateDto;

import jakarta.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AccountUpdateDto extends BaseUpdateDto {
    @NotBlank(message = "{field.blank}")
    private String name;

    private String description;
}
