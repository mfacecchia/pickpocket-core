package com.feis.splitnings.features.split.data;

import com.feis.splitnings.common.data.entity.BaseEntity;
import com.feis.splitnings.features.account.data.Account;

import org.hibernate.annotations.SQLRestriction;
import org.hibernate.envers.Audited;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Audited
@SQLRestriction("deleted=false")
@Entity(name = "split")
public class Split extends BaseEntity {
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "theoretical_amount", nullable = false)
    private Double theoreticalAmount;

    @Column(name = "available_amount", nullable = false)
    private Double availableAmount;

    @Column(name = "split_percentage", nullable = false)
    private Short splitPercentage;

    @Column(name = "active", nullable = false)
    private Boolean active;

    @Column(name = "is_default", nullable = false)
    private Boolean isDefault;

    @Column(name = "account_id", nullable = false)
    private Integer accountId;

    /* ---RELATIONSHIPS--- */

    @JoinColumn(name = "account_id", nullable = false, insertable = false, updatable = false)
    @ManyToOne
    private Account account;
}

