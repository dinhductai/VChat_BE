package com.website.loveconnect.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageResponse {
    private Integer senderId;
    private Integer receiverId;
    private String message;

}
