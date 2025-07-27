package com.website.loveconnect.dto.request;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageRequest {
    @NonNull
    private String token;
    @NonNull
    private Integer receiverId;
    @NonNull
    private String message;

}
