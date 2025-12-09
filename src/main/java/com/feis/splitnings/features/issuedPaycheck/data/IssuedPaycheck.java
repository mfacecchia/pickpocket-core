package com.feis.splitnings.features.issuedPaycheck.data;

import java.time.Instant;

import com.feis.splitnings.common.data.entity.BaseEntity;
import com.feis.splitnings.features.account.data.Account;
import com.feis.splitnings.features.paycheck.data.Paycheck;

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
@Entity(name = "issued_paycheck")
public class IssuedPaycheck extends BaseEntity {
    @Column(name = "issued_amount", nullable = false)
    private Double issuedAmount;

    @Column(name = "issued_at", nullable = false)
    private Instant issuedAt;

    @Column(name = "successful", nullable = false)
    private Boolean successful;

    @JoinColumn(name = "paycheck_id", nullable = false)
    @ManyToOne
    private Paycheck paycheck;

    @JoinColumn(name = "account_id", nullable = false)
    @ManyToOne
    private Account account;
}

