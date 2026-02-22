package com.feis.splitnings.features.issuedPaycheck.data.dto.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.feis.splitnings.common.data.dto.response.BaseGetDto;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;

@JsonPropertyOrder({"issuedAmount", "issuedAt", "successful", "paycheckId", "accountId"})
@Getter
@Setter
public class IssuedPaycheckDto extends BaseGetDto {
    private Double issuedAmount;
    private Instant issuedAt;
    private Boolean successful;
    private Integer paycheckId;
    private Integer accountId;
}
