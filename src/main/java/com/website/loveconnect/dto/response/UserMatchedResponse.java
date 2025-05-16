package com.website.loveconnect.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserMatchedResponse {
    private Integer matchId;
    private Integer receiverId;
    private String fullName;
    private String photoUrl;
}
