package com.website.loveconnect.controller.user;

import com.website.loveconnect.dto.request.ProfileDetailRequest;
import com.website.loveconnect.dto.request.UserCreateRequest;
import com.website.loveconnect.dto.response.*;
import com.website.loveconnect.service.PhotoService;
import com.website.loveconnect.service.LikeService;
import com.website.loveconnect.service.UserProfileService;
import com.website.loveconnect.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping(value = "/api")
public class UserProfileController {
    UserService userService;
    PhotoService imageService;
    UserProfileService userProfileService;
    LikeService likeService;


    //lấy thông tin chi tiết người dùng
    @Operation(summary = "Get user profile",description = "Get all information detail of user")
    @GetMapping(value = "/user-profile")
    public ResponseEntity<ApiResponse<ProfileDetailResponse>> getUserProfile(@AuthenticationPrincipal Jwt jwt,
                                                                             @RequestParam(name = "userId", required = false) Integer userId){
        Integer finalId;
        if(userId != null){
            finalId=userId;
        }else  finalId = Integer.parseInt(jwt.getSubject());
        return ResponseEntity.ok(new ApiResponse<>(true,"Get user profile successful",
                userProfileService.getProfileDetail(finalId)));
    }

    //cập nhật thông tin chi tiết người dùng
    @Operation(summary = "Update user profile",description = "Update info detail of user profile")
    @PutMapping(value = "/user-profile/update")
    public ResponseEntity<ApiResponse<String>> updateUserProfile(@AuthenticationPrincipal Jwt jwt,
                                                                 @RequestBody ProfileDetailRequest profileDetailRequest){
        Integer userId = Integer.parseInt(jwt.getSubject());
        userProfileService.updateProfileDetail(userId,profileDetailRequest);
        return ResponseEntity.ok(new ApiResponse<>(true,"Update user profile successful",null));
    }

    //xóa người dùng
    @Operation(summary = "Delete account",description = "User want to delete account")
    @DeleteMapping(value = "/user-profile/delete")
    public ResponseEntity<ApiResponse<String>> deleteUser(@AuthenticationPrincipal Jwt jwt) {
        Integer userId = Integer.parseInt(jwt.getSubject());
        userService.deleteUser(userId);
        return ResponseEntity.ok(new ApiResponse<>(true,"Delete account successful",null));
    }

    //lấy ra tên và ảnh profile
    @Operation(summary = "Get name and profile picture",description = "Get full name and profile picture of user")
    @GetMapping(value = "/user-name-profile")
    public ResponseEntity<ApiResponse<UserNameAndProfileResponse>> getUserNameAndProfilePhoto(@AuthenticationPrincipal Jwt jwt){
        Integer userId = Integer.parseInt(jwt.getSubject());
        return ResponseEntity.ok(new ApiResponse<>(true,"Get full name and profile photo successful",
                userProfileService.getUserNameAndProfile(userId)));
    }



}
