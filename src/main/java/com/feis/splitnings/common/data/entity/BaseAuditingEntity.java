package com.feis.splitnings.common.data.entity;

import com.feis.splitnings.security.utils.SecurityUtils;

import java.time.Instant;

import org.hibernate.envers.Audited;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
@Audited
public class BaseAuditingEntity {
    @Column(name = "created_date", nullable = false)
    protected Instant createdDate;

    @Column(name = "created_by", nullable = false)
    protected String createdBy;

    @Column(name = "modified_date")
    private Instant modifiedDate;

    @Column(name = "modified_by")
    protected String modifiedBy;

    @PrePersist
    public void prePersist() {
        this.createdBy = SecurityUtils.getJwtUserId().toString();
        this.createdDate = Instant.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.modifiedBy = SecurityUtils.getJwtUserId().toString();
        this.modifiedDate = Instant.now();
    }
}

