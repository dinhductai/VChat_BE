package com.website.loveconnect.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.website.loveconnect.enumpackage.NotificationType;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Table(name = "notifications")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
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


    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "sender_id",nullable = false)
    private User sender;
}
