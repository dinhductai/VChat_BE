package com.website.loveconnect.controller.user;

import com.website.loveconnect.dto.request.UserCreateRequest;
import com.website.loveconnect.dto.response.ApiResponse;
import com.website.loveconnect.service.ImageService;
import com.website.loveconnect.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping(value = "/api")
public class UserProfileController {
    UserService userService;
    ImageService imageService;

    //tạo tài khoản người dùng
    @PostMapping(value = "/sign-up")
    public ResponseEntity<ApiResponse<String>> signUpAccount(@RequestBody UserCreateRequest userCreateRequest){
        userService.createUser(userCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true,"Create account successful", null));
    }

    //api dùng kèm với /sign-up
    //tạo ảnh profile với 1 file và user email
    @PostMapping(value = "/profile-image/upload",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<String>> upLoadProfileImage(@RequestParam("file") MultipartFile file,
                                                                  @RequestParam("userEmail") String userEmail) throws IOException {
        String urlImage = imageService.saveImageProfile(file,userEmail);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true,"Save profile image successful", urlImage));
    }

    @GetMapping(value = "/profile-image")
    public ResponseEntity<ApiResponse<String>> getProfileImage(@RequestParam("userid") Integer userid){
        return ResponseEntity.ok(new ApiResponse<>(true,"Get profile image successful",
                imageService.getProfileImage(userid)));
    }

    @GetMapping(value = "/photo-all")
    public ResponseEntity<ApiResponse<String>> getPhotoAll(@RequestParam("idUser") Integer idUser,
                                                           @RequestParam("index") Integer index){
        return ResponseEntity.ok(new ApiResponse<>(true,"Get all photo successful",
                imageService.getOwnedPhoto(idUser,index)));

    }

}
