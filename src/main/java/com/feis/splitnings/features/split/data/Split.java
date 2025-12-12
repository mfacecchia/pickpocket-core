package com.feis.splitnings.features.split.data;

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
@Entity(name = "split")
public class Split extends BaseEntity {
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "theoretical_amuont", nullable = false)
    private Double theoreticalAmount;

    @Column(name = "available_amuont", nullable = false)
    private Double availableAmount;

    @Column(name = "split_percentage", nullable = false)
    private Short splitPercentage;

    @Column(name = "active", nullable = false)
    private Boolean active;

    @Column(name = "icon", nullable = false)
    private String icon;

    @Column(name = "icon_color", nullable = false)
    private String iconColor;

    @JoinColumn(name = "account_id", nullable = false)
    @ManyToOne
    private Account account;
}

