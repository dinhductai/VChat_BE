package com.website.loveconnect.dto.request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReelRequest {

    private MultipartFile video;
    private String content;
    private String userEmail;
    @NonNull
    private Boolean isPublic;

}
