package com.website.loveconnect.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
    private String userEmail;
    @NonNull
    private Boolean isPublic;

}
