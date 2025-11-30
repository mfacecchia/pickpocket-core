package com.feis.splitnings.common.data.entity;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.envers.Audited;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
@Audited
public class BaseAuditingEntity {
    @CreationTimestamp
    @Column(name = "created_date")
    protected Instant createdDate;

    @Column(name = "created_by")
    protected String createdBy;

    @UpdateTimestamp
    @Column(name = "modified_date")
    private Instant modifiedDate;

    @Column(name = "modified_by")
    protected Integer modifiedBy;

    @Column(name = "deleted")
    protected Boolean deleted;
}
