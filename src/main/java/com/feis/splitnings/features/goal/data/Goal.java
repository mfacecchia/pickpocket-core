package com.feis.splitnings.features.goal.data;

import java.time.Instant;

import com.feis.splitnings.common.data.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "goal")
public class Goal extends BaseEntity {
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "target_amount", nullable = false)
    private Double targetAmount;

    @Column(name = "current_amount", nullable = false)
    private Double currentAmount;

    @Column(name = "completed_at", nullable = true)
    private Instant completedAt;

    @Column(name = "icon", nullable = false)
    private String icon;

    @Column(name = "icon_color", nullable = false)
    private String iconColor;
}

