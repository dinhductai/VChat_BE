package com.website.loveconnect.mapper;

import com.website.loveconnect.dto.response.NotificationResponse;
import jakarta.persistence.Tuple;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
public class NotificationMapper {
    public NotificationResponse toNotificationResponse(Tuple tuple) {
        return NotificationResponse.builder()
                .notificationId(tuple.get("notificationId",Integer.class))
                .notificationType(tuple.get("notificationType", String.class))
                .content(tuple.get("content", String.class))
                .createAt(tuple.get("createAt", Timestamp.class))
                .senderId(tuple.get("senderId",Integer.class))
                .profileUrl(tuple.get("profilePicture", String.class))
                .build();
    }

}
