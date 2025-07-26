package com.website.loveconnect.dto.request;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageLoadRequest {
    private Integer receiverId;
    private int page=0;
    private int size=20;
}
