package com.website.loveconnect.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserFriendResponse {
    private Integer userId;
    private String fullName;
    private String bio;
    private String phoneNumber;
    private String photoProfile;
}
