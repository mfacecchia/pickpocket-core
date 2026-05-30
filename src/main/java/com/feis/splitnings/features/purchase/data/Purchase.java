package com.feis.splitnings.features.purchase.data;

import com.feis.splitnings.common.data.entity.BaseEntity;
import com.feis.splitnings.features.goal.data.Goal;
import com.feis.splitnings.features.split.data.Split;

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
@Entity(name = "purchase")
public class Purchase extends BaseEntity {
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = true)
    private String description;

    @Column(name = "amount", nullable = false)
    private Double amount;

    @Column(name = "goal_id", nullable = true)
    private Integer goalId;

    @Column(name = "split_id", nullable = false)
    private Integer splitId;

    // TODO: Purchase category

    /* ---RELATIONSHIPS--- */

    @JoinColumn(name = "goal_id", nullable = true, insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Goal goal;

    @JoinColumn(name = "split_id", nullable = false, insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Split split;
}

