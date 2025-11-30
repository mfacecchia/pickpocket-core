package com.feis.splitnings.common.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public class BaseLookupTable extends BaseEntity {
    @Column(name = "name")
    private String name;
}
