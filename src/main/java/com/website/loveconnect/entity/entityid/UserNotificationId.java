package com.website.loveconnect.entity.entityid;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserNotificationId implements Serializable {
    private Integer user;
    private Integer notification;
}
