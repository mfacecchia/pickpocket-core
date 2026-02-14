package com.feis.splitnings.features.split.data.dto.response;

import com.feis.splitnings.common.data.dto.response.BaseGetDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SplitDto extends BaseGetDto {
    private String name;
    private Double theoreticalAmount;
    private Double availableAmount;
    private Short splitPercentage;
    private Boolean active;
    private Boolean isDefault;
    private Integer accountId;
}
