package com.website.loveconnect.controller.user;

import com.website.loveconnect.dto.response.ApiResponse;
import com.website.loveconnect.service.ImageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping(value = "/api")
public class PhotoController {

    ImageService imageService;

    //xóa ảnh
    @DeleteMapping(value = "/photo-delete")
    public ResponseEntity<ApiResponse<String>> deletePhoto(@RequestParam("userId") Integer userId,
                                                           @RequestParam("photoUrl") String photoUrl) {
        imageService.deleteImageProfile(userId,photoUrl);
        return ResponseEntity.ok(new ApiResponse<>(true,"Delete photo successful",null));
    }

}
