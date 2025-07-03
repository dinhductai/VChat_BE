package com.website.loveconnect.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostRequest {
    private List<MultipartFile> listImage;
    private List<MultipartFile> listVideo;
    private String content;
    @NotBlank(message = "Email cannot be blank")
    private String userEmail;
    @NotBlank(message = "Post can be public or private")
    private Boolean isPublic;

}
