package com.website.loveconnect.dto.response;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationResponse {
    private Integer notificationId;
    private String notificationType;
    private String content;
    private Timestamp createAt;
    private Integer senderId;
    private String profileUrl;
}
