package com.feis.splitnings.features.account.data;

import com.feis.splitnings.common.data.entity.BaseEntity;
import com.feis.splitnings.features.user.data.User;

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
@Entity(name = "account")
public class Account extends BaseEntity {
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = true)
    private String description;

    @Column(name = "wealth", nullable = false)
    private Double wealth;

    @Column(name = "icon", nullable = false)
    private String icon;

    @Column(name = "icon_color", nullable = false)
    private String iconColor;

    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne
    private User user;
}

