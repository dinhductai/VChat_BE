package com.website.loveconnect.controller.user;

import com.website.loveconnect.dto.response.ApiResponse;
import com.website.loveconnect.service.LikeService;
import com.website.loveconnect.service.PhotoService;
import com.website.loveconnect.service.UserProfileService;
import com.website.loveconnect.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping(value = "/api")
public class LikeController {

    UserService userService;
    PhotoService imageService;
    UserProfileService userProfileService;
    LikeService likeService;

    //Tạo like
    @Operation(summary = "")
    @PostMapping(value = "/{senderId}/like/{receivedId}")
    public ResponseEntity<ApiResponse<String>> likeUser(@PathVariable int senderId,
                                                        @PathVariable int receivedId){
        likeService.likeUserById(senderId,receivedId);
        return ResponseEntity.ok(new ApiResponse<>(true,"Like user successful",null));
    }

    //
    @PostMapping(value = "/{senderId}/dislike/{receivedId}")
    public ResponseEntity<ApiResponse<String>> dislikeUser(@PathVariable int senderId,
                                                           @PathVariable int receivedId){
        likeService.dislikeUserById(senderId,receivedId);
        return ResponseEntity.ok(new ApiResponse<>(true,"Dislike user successful",null));
    }

}
