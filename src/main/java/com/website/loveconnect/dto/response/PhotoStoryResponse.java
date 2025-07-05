package com.website.loveconnect.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PhotoStoryResponse {
    private Integer userId;
    private String fullName;
    private String profileUrl;
    private List<String> listStoryPhoto;
    private List<String> listDateUpload;
}
