package com.website.loveconnect.controller.user;

import com.cloudinary.Api;
import com.website.loveconnect.dto.response.ApiResponse;
import com.website.loveconnect.dto.response.PhotoStoryResponse;
import com.website.loveconnect.dto.response.UserAndPhotosResponse;
import com.website.loveconnect.service.PhotoService;
import com.website.loveconnect.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.core.Authentication;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping(value = "/api")
public class PhotoController {

    PhotoService imageService;
    UserService userService;
    //api dùng kèm với /sign-up
    //tạo ảnh với 1 file và user email
    //api upload ảnh bình thường
    @Operation(summary = "Create photo",description = "Upload 1 photo at a time")
    @PostMapping(value = "/photo/upload",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<String>> upLoadImage(@RequestParam("file") MultipartFile file,
                                                           @AuthenticationPrincipal Jwt jwt) throws IOException {
        String userEmail = jwt.getClaimAsString("email");
        String urlImage = imageService.uploadImage(file,userEmail);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true,"Save profile image successful", urlImage));
    }
    //api upload ảnh profile
    @Operation(summary = "Create profile photo",description = "Upload 1 photo profile at a time")
    @PostMapping(value = "/profile-photo/upload",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<String>> upLoadProfileImage(@RequestParam("file") MultipartFile file,
                                                                  @AuthenticationPrincipal Jwt jwt) throws IOException {
        String userEmail = jwt.getClaimAsString("email");
        String urlImage = imageService.uploadImageProfile(file,userEmail);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true,"Save profile image successful", urlImage));
    }

    //lấy ảnh profile của user
    @Operation(summary = "Get profile photo",description = "Get newest profile picture of user")
    @GetMapping(value = "/profile-photo")
    public ResponseEntity<ApiResponse<String>> getProfileImage(@AuthenticationPrincipal Jwt jwt,
                                                               @RequestParam(name = "userId", required = false) Integer userId){
        Integer finalId;
        if(userId != null){
            finalId=userId;
        }else  finalId = Integer.parseInt(jwt.getSubject());
        return ResponseEntity.ok(new ApiResponse<>(true,"Get profile image successful",
                imageService.getProfileImage(finalId)));
    }

    //lấy toàn bộ ảnh của người dùng
    @Operation(summary = "Get photos of owner",description = "Get all photo of owner")
    @GetMapping(value = "/photo")
    public ResponseEntity<ApiResponse<Page<String>>> getPhotoAll(@AuthenticationPrincipal Jwt jwt,
                                                                 @RequestParam(defaultValue = "0") int page,
                                                                 @RequestParam(defaultValue = "10") int size,
                                                                 @RequestParam(name = "userId", required = false) Integer userId){
        Integer finalId;
        if(userId != null){
            finalId=userId;
        }else  finalId = Integer.parseInt(jwt.getSubject());
        Page<String> urlPhotos = imageService.getOwnedPhotos(finalId,page,size);
        return ResponseEntity.ok(new ApiResponse<>(true,"Get photos successful",urlPhotos));
    }

    //xóa ảnh
    @Operation(summary = "Delete a photo",description = "User delete a photo by photo url")
    @DeleteMapping(value = "/photo/delete")
    public ResponseEntity<ApiResponse<String>> deletePhoto(@RequestParam("photoUrl") String photoUrl,
                                                           @AuthenticationPrincipal Jwt jwt) {
        Integer userId = Integer.parseInt(jwt.getSubject());
        imageService.deleteImageProfile(userId,photoUrl);
        return ResponseEntity.ok(new ApiResponse<>(true,"Delete photo successful",null));
    }

    //lấy toàn bộ story
    @Operation(summary = "Get all story", description = "Get stories of your friends, in a day")
    @GetMapping(value = "/story")
    public ResponseEntity<ApiResponse<Page<PhotoStoryResponse>>> getPhotoStory(@AuthenticationPrincipal Jwt jwt,
                                                                               @RequestParam(defaultValue = "0") int page,
                                                                               @RequestParam(defaultValue = "4") int size){
        Integer userId= Integer.parseInt(jwt.getSubject());
        return ResponseEntity.ok(new ApiResponse<>(true,"Get story successful",
                imageService.photoStories(userId,page,size)));
    }

    //tạo story
    @Operation(summary = "Create a story image",description = "User create a story image, 1 image at a time")
    @PostMapping(value = "/story/create",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<String>> upLoadStory(@RequestParam("file") MultipartFile file,
                                                           @AuthenticationPrincipal Jwt jwt) throws IOException {
        String userEmail = jwt.getClaimAsString("email");
        String urlImage = imageService.uploadStory(file,userEmail);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true,"Save story successful", urlImage));
    }
    //xóa story
    @Operation(summary = "Delete story image",description = "User delete a story image after upload it")
    @DeleteMapping(value = "/story/delete")
    public ResponseEntity<ApiResponse<String>> deleteStory(@AuthenticationPrincipal Jwt jwt,
                                                           @RequestParam("photoUrl") String photoUrl) {
        Integer userId = Integer.parseInt(jwt.getSubject());
        imageService.deleteImageProfile(userId,photoUrl);
        return ResponseEntity.ok(new ApiResponse<>(true,"Delete photo successful",null));
    }

    //lấy ds ảnh của user random ( phụ thuộc vào looking for)
    @Operation(summary = "Get all love partner",description = "Get all picture of users, depend on LookingFor")
    @GetMapping(value = "/random-user-photos")
    public ResponseEntity<ApiResponse<Page<UserAndPhotosResponse>>> getUserAndPhotos(@AuthenticationPrincipal Jwt jwt,
                                                                                     @RequestParam(defaultValue = "0") int page,
                                                                                     @RequestParam(defaultValue = "10") int size){
        Integer userId = Integer.parseInt(jwt.getSubject());
        return ResponseEntity.ok(new ApiResponse<>(true,"Get page user and photos successful",
                userService.getAllUsersAndPhotos(page,size,userId)));
    }

    //lấy toàn bộ story
    @Operation(summary = "Get owner story", description = "Get owner stories , in a day")
    @GetMapping(value = "/story/owner")
    public ResponseEntity<ApiResponse<List<PhotoStoryResponse>>> getOwnerStories(@AuthenticationPrincipal Jwt jwt,
                                                                                 @RequestParam(name = "userId", required = false) Integer userId){
        Integer finalId;
        if(userId != null){
            finalId=userId;
        }else  finalId = Integer.parseInt(jwt.getSubject());
        return ResponseEntity.ok(new ApiResponse<>(true,"Get owner story successful",
                imageService.getOwnerStories(finalId)));
    }

}
