package com.website.loveconnect.dto.request;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationLoadRequest {
    private int page=0;
    private int size=20;
}
