package com.feis.splitnings.features.goal.data;

import com.feis.splitnings.common.data.entity.BaseEntity;
import com.feis.splitnings.features.split.data.Split;

import org.hibernate.envers.Audited;

import java.time.Instant;

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
@Entity(name = "goal")
public class Goal extends BaseEntity {
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "target_amount", nullable = false)
    private Double targetAmount;

    @Column(name = "current_amount", nullable = false)
    private Double currentAmount;

    @Column(name = "completed", nullable = false)
    private Boolean completed;

    @Column(name = "completed_at", nullable = true)
    private Instant completedAt;

    @Column(name = "split_id", nullable = false)
    private Integer splitId;

    /* ---RELATIONSHIPS--- */

    @JoinColumn(name = "split_id", insertable = false, updatable = false, nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Split split;
}

