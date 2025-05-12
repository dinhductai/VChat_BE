package com.website.loveconnect.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserAndPhotosResponse {
    private Integer userId;
    private String fullName;
    private String location;
    private String gender;
    private List<String> listPhotoUrl;

}
