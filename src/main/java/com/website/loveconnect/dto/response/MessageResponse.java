package com.website.loveconnect.dto.response;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageResponse {
    private Integer senderId;
    private Integer receiverId;
    private Integer messageId;
    private String message;
    private Timestamp sentAt;
    private Boolean isDeleted;

}
