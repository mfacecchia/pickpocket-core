package com.feis.splitnings.features.cashTransfer.data;

import com.feis.splitnings.common.data.entity.BaseEntity;
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

    @JoinColumn(name = "from_split_id", nullable = false)
    @ManyToOne
    private Split fromSplit;

    @JoinColumn(name = "to_split_id", nullable = false)
    @ManyToOne
    private Split toSplit;
}

