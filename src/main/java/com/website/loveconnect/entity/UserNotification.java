package com.website.loveconnect.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.website.loveconnect.entity.entityid.UserNotificationId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "user_notifications")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@IdClass(UserNotificationId.class)
public class UserNotification {

    @Id
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    @Id
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "notification_id",nullable = false)
    private Notification notification;


}
