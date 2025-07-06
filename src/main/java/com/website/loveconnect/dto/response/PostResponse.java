package com.website.loveconnect.dto.response;

import lombok.*;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse {
    private Integer userId;
    private String fullName;
    private String bio;
    private String phoneNumber;
    private String profilePicture;
    private Integer postId;
    private String content;
    private Timestamp uploadDate;
    private String status;
    private Boolean isPublic;
    private List<String> photosUrl;
    private List<String> videosUrl;
}
