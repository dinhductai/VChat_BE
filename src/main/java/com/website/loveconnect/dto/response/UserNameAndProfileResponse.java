package com.website.loveconnect.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserNameAndProfileResponse {
    private String fullName;
    private String profileUrl;
}
