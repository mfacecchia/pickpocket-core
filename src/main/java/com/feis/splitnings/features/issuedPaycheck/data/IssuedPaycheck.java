package com.feis.splitnings.features.issuedPaycheck.data;

import com.feis.splitnings.common.data.entity.BaseEntity;
import com.feis.splitnings.features.account.data.Account;
import com.feis.splitnings.features.paycheck.data.Paycheck;
import com.feis.splitnings.features.split.data.Split;

import java.time.Instant;

import org.hibernate.envers.Audited;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
@Entity(name = "issued_paycheck")
public class IssuedPaycheck extends BaseEntity {
    @Column(name = "issued_amount", nullable = false)
    private Double issuedAmount;

    @Column(name = "issued_at", nullable = false)
    private Instant issuedAt;

    @Column(name = "successful", nullable = false)
    private Boolean successful;

    @Column(name = "paycheck_id")
    private Integer paycheckId;

    @Column(name = "account_id", nullable = false)
    private Integer accountId;

    @Column(name = "split_id", nullable = true)
    private Integer splitId;

    /* ---RELATIONSHIPS--- */

    @JoinColumn(name = "paycheck_id", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Paycheck paycheck;

    @JoinColumn(name = "account_id", nullable = false, insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Account account;

    @JoinColumn(name = "split_id", nullable = true, insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Split split;
}

