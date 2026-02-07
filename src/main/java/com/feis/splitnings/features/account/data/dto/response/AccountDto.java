package com.feis.splitnings.features.account.data.dto.response;

import com.feis.splitnings.common.data.dto.response.BaseGetDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountDto extends BaseGetDto {
    private String name;
    private String description;
    private Double wealth;
    private Integer userId;
}
