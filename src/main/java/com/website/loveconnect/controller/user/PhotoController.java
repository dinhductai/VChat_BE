package com.website.loveconnect.controller.user;

import com.cloudinary.Api;
import com.website.loveconnect.dto.response.ApiResponse;
import com.website.loveconnect.dto.response.PhotoStoryResponse;
import com.website.loveconnect.service.PhotoService;
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

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping(value = "/api")
public class PhotoController {

    PhotoService imageService;

    //api dùng kèm với /sign-up
    //tạo ảnh với 1 file và user email
    //api upload ảnh bình thường
    @PostMapping(value = "/photo/upload",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<String>> upLoadImage(@RequestParam("file") MultipartFile file,
                                                                  @RequestParam("userEmail") String userEmail) throws IOException {
        String urlImage = imageService.uploadImage(file,userEmail);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true,"Save profile image successful", urlImage));
    }
    //api upload ảnh profile
    @PostMapping(value = "/profile-photo/upload",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<String>> upLoadProfileImage(@RequestParam("file") MultipartFile file,
                                                                  @RequestParam("userEmail") String userEmail) throws IOException {
        String urlImage = imageService.uploadImageProfile(file,userEmail);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true,"Save profile image successful", urlImage));
    }

    //lấy ảnh profile của user
    @GetMapping(value = "/profile-photo")
    public ResponseEntity<ApiResponse<String>> getProfileImage(@RequestParam("userid") Integer userid){
        return ResponseEntity.ok(new ApiResponse<>(true,"Get profile image successful",
                imageService.getProfileImage(userid)));
    }

    //lấy toàn bộ ảnh của người dùng
    @GetMapping(value = "/photo")
    public ResponseEntity<ApiResponse<List<String>>> getPhotoAll(@RequestParam("idUser") Integer idUser){
        List<String> urlPhotos = imageService.getOwnedPhotos(idUser);
        return ResponseEntity.ok(new ApiResponse<>(true,"Get photos successful",urlPhotos));
    }

    //xóa ảnh
    @DeleteMapping(value = "/photo/delete")
    public ResponseEntity<ApiResponse<String>> deletePhoto(@RequestParam("userId") Integer userId,
                                                           @RequestParam("photoUrl") String photoUrl) {
        imageService.deleteImageProfile(userId,photoUrl);
        return ResponseEntity.ok(new ApiResponse<>(true,"Delete photo successful",null));
    }

    @Operation(summary = "Get stories of your friends, at least a day")
    @GetMapping(value = "/story")
    public ResponseEntity<ApiResponse<Page<PhotoStoryResponse>>> getPhotoStory(@RequestParam("idUser") Integer idUser,
                                                                               @RequestParam(defaultValue = "0") int page,
                                                                               @RequestParam(defaultValue = "4") int size){
        return ResponseEntity.ok(new ApiResponse<>(true,"Get story successful",
                imageService.photoStories(idUser,page,size)));
    }
    @Operation(summary = "Create a story image")
    @PostMapping(value = "/story/create",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<String>> upLoadStory(@RequestParam("file") MultipartFile file,
                                                           @RequestParam("userEmail") String userEmail) throws IOException {
        String urlImage = imageService.uploadStory(file,userEmail);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true,"Save story successful", urlImage));
    }
    //xóa story
    @Operation(summary = "Delete story image")
    @DeleteMapping(value = "/story/delete")
    public ResponseEntity<ApiResponse<String>> deleteStory(@RequestParam("userId") Integer userId,
                                                           @RequestParam("photoUrl") String photoUrl) {
        imageService.deleteImageProfile(userId,photoUrl);
        return ResponseEntity.ok(new ApiResponse<>(true,"Delete photo successful",null));
    }
}
