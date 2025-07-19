package com.website.loveconnect.dto.response;


import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorDetailResponse {
    private LocalDateTime timestamp;
    private String message;
    private String details;
}
