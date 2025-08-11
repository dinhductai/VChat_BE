package com.website.loveconnect.controller.user;

import com.cloudinary.Api;
import com.website.loveconnect.dto.response.ApiResponse;
import com.website.loveconnect.entity.Video;
import com.website.loveconnect.service.VideoService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
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
    }

    @Operation(summary = "Get owner video",description = "Get all video of owner")
    @GetMapping(value = "/video")
    public ResponseEntity<ApiResponse<Page<Video>>> getVideo(@AuthenticationPrincipal Jwt jwt,
                                                             @RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "10") int size,
                                                             @RequestParam(name = "userId", required = false) Integer userId){
        Integer finalId;
        if(userId != null){
            finalId=userId;
        }else  finalId = Integer.parseInt(jwt.getSubject());
        return ResponseEntity.ok(new ApiResponse<>(true,"Get video successful",
                videoService.getOwnedVideos(finalId,page,size)));
    }

    @Operation(summary = "Delete video",description = "Delete owner video")
    @DeleteMapping(value = "/video/delete")
    public ResponseEntity<ApiResponse<String>> deleteVideo(@RequestParam("videoUrl") String videoUrl,
                                                           @AuthenticationPrincipal Jwt jwt){
        Integer userId = Integer.parseInt(jwt.getSubject());
        videoService.deleteVideo(userId,videoUrl);
        return ResponseEntity.ok(new ApiResponse<>(true,"Delete video successful",null));

    }
}


