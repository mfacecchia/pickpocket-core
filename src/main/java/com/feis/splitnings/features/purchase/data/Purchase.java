package com.feis.splitnings.features.purchase.data;

import com.feis.splitnings.common.data.entity.BaseEntity;
import com.feis.splitnings.features.goal.data.Goal;
import com.feis.splitnings.features.purchaseCategory.data.PurchaseCategory;
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
@Entity(name = "purchase")
public class Purchase extends BaseEntity {
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "amount", nullable = false)
    private Double amount;

    @JoinColumn(name = "goal_id", nullable = true)
    @ManyToOne
    private Goal goal;

    @JoinColumn(name = "split_id", nullable = false)
    @ManyToOne
    private Split split;

    @JoinColumn(name = "category_id", nullable = false)
    @ManyToOne
    private PurchaseCategory purchaseCategory;
}

