package com.website.loveconnect.dto.request;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageUpdateRequest {
    private String token;
    private Integer receiverId;
    private Integer messageId;
    private String message;

}
