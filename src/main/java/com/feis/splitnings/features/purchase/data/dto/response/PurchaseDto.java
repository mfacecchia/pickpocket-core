package com.feis.splitnings.features.purchase.data.dto.response;

import com.feis.splitnings.common.data.dto.response.BaseGetDto;
import com.feis.splitnings.features.purchase.data.enums.PurchaseCategory;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Getter;
import lombok.Setter;

@JsonPropertyOrder({"id", "name", "description", "amount", "category", "goalId", "splitId"})
@Getter
@Setter
public class PurchaseDto extends BaseGetDto {
    private String name;
    private String description;
    private Double amount;
    private PurchaseCategory category;
    private Integer goalId;
    private Integer splitId;
}

