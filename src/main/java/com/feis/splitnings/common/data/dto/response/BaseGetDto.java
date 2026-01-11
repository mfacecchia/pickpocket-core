package com.feis.splitnings.common.data.dto.response;

import com.feis.splitnings.common.data.entity.BaseAuditingEntity;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public class BaseGetDto extends BaseAuditingEntity {
    protected Integer id;
}
