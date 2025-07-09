package com.website.loveconnect.controller.user;

import com.website.loveconnect.dto.response.ApiResponse;
import com.website.loveconnect.service.VideoService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping(value = "/api")
public class VideoController {
    VideoService videoService;

    @PostMapping(value = "/video/upload",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<String>> upLoadImage(@RequestParam("file") MultipartFile file,
                                                           @AuthenticationPrincipal Jwt jwt) throws IOException {
        String userEmail = jwt.getClaimAsString("email");
        String urlImage = videoService.uploadVideo(file,userEmail);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true,"Save profile video successful", urlImage));
    }}
