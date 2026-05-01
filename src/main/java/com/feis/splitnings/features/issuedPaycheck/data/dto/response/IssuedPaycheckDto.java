package com.feis.splitnings.features.issuedPaycheck.data.dto.response;

import com.feis.splitnings.common.data.dto.response.BaseGetDto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;

@JsonPropertyOrder({"id", "issuedAmount", "issuedAt", "successful", "paycheckId", "accountId", "splitId"})
@Getter
@Setter
public class IssuedPaycheckDto extends BaseGetDto {
    private Double issuedAmount;
    private Instant issuedAt;
    private Boolean successful;
    private Integer paycheckId;
    private Integer accountId;
    private Integer splitId;
}
