package com.feis.splitnings.features.cashTransfer.data;

import com.feis.splitnings.common.data.entity.BaseEntity;
import com.feis.splitnings.features.account.data.Account;
import com.feis.splitnings.features.split.data.Split;

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
@Entity(name = "cash_transfer")
public class CashTransfer extends BaseEntity {
    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "amount", nullable = false)
    private Double amount;

    @Column(name = "from_split_id", nullable = true)
    private Integer fromSplitId;

    @Column(name = "to_split_id", nullable = true)
    private Integer toSplitId;

    @Column(name = "account_id", nullable = false)
    private Integer accountId;

    /* ---RELATIONSHIPS--- */

    @JoinColumn(name = "from_split_id", nullable = true, insertable = false, updatable = false)
    @ManyToOne
    private Split fromSplit;

    @JoinColumn(name = "to_split_id", nullable = true, insertable = false, updatable = false)
    @ManyToOne
    private Split toSplit;

    @JoinColumn(name = "account_id", nullable = false, insertable = false, updatable = false)
    @ManyToOne
    private Account account;
}

