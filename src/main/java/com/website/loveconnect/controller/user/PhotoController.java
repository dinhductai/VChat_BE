package com.website.loveconnect.controller.user;

import com.website.loveconnect.dto.response.ApiResponse;
import com.website.loveconnect.service.PhotoService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
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
    @GetMapping(value = "/photos")
    public ResponseEntity<ApiResponse<List<String>>> getPhotoAll(@RequestParam("idUser") Integer idUser){
        List<String> urlPhotos = imageService.getOwnedPhotos(idUser);
        return ResponseEntity.ok(new ApiResponse<>(true,"Get photos successful",urlPhotos));
    }

    //xóa ảnh
    @DeleteMapping(value = "/photo-delete")
    public ResponseEntity<ApiResponse<String>> deletePhoto(@RequestParam("userId") Integer userId,
                                                           @RequestParam("photoUrl") String photoUrl) {
        imageService.deleteImageProfile(userId,photoUrl);
        return ResponseEntity.ok(new ApiResponse<>(true,"Delete photo successful",null));
    }



}
