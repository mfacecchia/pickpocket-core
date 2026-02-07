package com.feis.splitnings.common.data.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.feis.splitnings.common.data.entity.BaseAuditingEntity;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public class BaseGetDto extends BaseAuditingEntity {
    @JsonProperty(index = 0)
    protected Integer id;
}
