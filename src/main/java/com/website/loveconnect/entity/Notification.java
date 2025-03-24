package com.website.loveconnect.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.website.loveconnect.enumpackage.NotificationType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Table(name = "notifications")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Integer notificationId;

    @Enumerated(EnumType.STRING)
    @Column(name = "notification_type",nullable = false)
    private NotificationType notificationType;

    @Column(name = "content",nullable = false)
    private String content;

    @Column(name = "created_at")
    private Timestamp createdAt = new Timestamp(System.currentTimeMillis());

    @JsonIgnore
    @OneToMany(mappedBy = "notification" ,fetch = FetchType.LAZY , cascade = CascadeType.ALL)
    private List<UserNotification> userNotifications = new ArrayList<>();
}
