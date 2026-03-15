package com.feis.splitnings.features.goal.data.dto.response;

import java.time.Instant;

import com.feis.splitnings.common.data.dto.response.BaseGetDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GoalDto extends BaseGetDto {
    private String name;
    private Double targetAmount;
    private Double currentAmount;
    private Boolean isCompleted;
    private Instant completedAt;
    private Integer splitId;
}
