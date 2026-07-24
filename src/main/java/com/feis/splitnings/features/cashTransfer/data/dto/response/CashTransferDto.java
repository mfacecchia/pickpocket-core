package com.feis.splitnings.features.cashTransfer.data.dto.response;

import com.feis.splitnings.common.data.dto.response.BaseGetDto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Getter;
import lombok.Setter;

@JsonPropertyOrder({"id", "description", "amount", "fromSplitId", "toSplitId", "accountId"})
@Getter
@Setter
public class CashTransferDto extends BaseGetDto {
    private String description;
    private Double amount;
    private Integer fromSplitId;
    private Integer toSplitId;
    private Integer accountId;
}
