package com.feis.splitnings.features.notification.data;

import com.feis.splitnings.common.data.entity.BaseEntity;
import com.feis.splitnings.features.user.data.User;

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
@Entity(name = "notification")
public class Notification extends BaseEntity {
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "public", nullable = false)
    private Boolean isPublic;

    @JoinColumn(name = "user_id", nullable = true)
    @ManyToOne
    private User user;
}

