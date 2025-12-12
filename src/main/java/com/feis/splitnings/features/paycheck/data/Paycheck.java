package com.feis.splitnings.features.paycheck.data;

import com.feis.splitnings.common.data.entity.BaseEntity;
import com.feis.splitnings.features.account.data.Account;

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
@Entity(name = "paycheck")
public class Paycheck extends BaseEntity {
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = true)
    private String description;

    @Column(name = "issue_day_of_month", nullable = false)
    private Short issueDayOfMonth;

    @Column(name = "amount", nullable = false)
    private Double amount;

    @JoinColumn(name = "account_id", nullable = false, unique = true)
    @ManyToOne
    private Account account;
}

