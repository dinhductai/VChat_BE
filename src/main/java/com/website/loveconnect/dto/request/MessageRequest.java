package com.website.loveconnect.dto.request;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageRequest {
    private Integer senderId;
    private Integer receiverId;
    private String message;
    private Timestamp sendAt;
}
