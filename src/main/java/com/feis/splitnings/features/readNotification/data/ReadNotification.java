package com.feis.splitnings.features.readNotification.data;

import com.feis.splitnings.common.data.entity.BaseEntity;
import com.feis.splitnings.features.notification.data.Notification;
import com.feis.splitnings.features.user.data.User;

import java.time.Instant;

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
@Entity(name = "read_notification")
public class ReadNotification extends BaseEntity {
    @Column(name = "read_at", nullable = false)
    private Instant readAt;

    @JoinColumn(name = "notification_id", nullable = false)
    @ManyToOne
    private Notification notification;

    @JoinColumn(name = "user_id", nullable = true)
    @ManyToOne
    private User user;
}

