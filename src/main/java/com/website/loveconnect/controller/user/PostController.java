package com.website.loveconnect.controller.user;

import com.cloudinary.Api;
import com.website.loveconnect.dto.request.PostRequest;
import com.website.loveconnect.dto.response.ApiResponse;
import com.website.loveconnect.dto.response.PostResponse;
import com.website.loveconnect.service.PostService;
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

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping(value = "/api")
public class PostController {
    PostService postService;

    @Operation(summary = "Create post",description = "Create a post with text, image or post")
    @PostMapping(value = "/post/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<String>> createPost(@ModelAttribute PostRequest postRequest){
        postService.savePost(postRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true,"Create post successful", null));
    }

    @Operation(summary = "Get random post",
                description = "Get all random post with page index automatically increase")
    @GetMapping(value = "/post")
    public ResponseEntity<ApiResponse<Page<PostResponse>>> getRandomPost(@RequestParam(defaultValue = "0") int page,
                                                                         @RequestParam(defaultValue = "3") int size){
        return ResponseEntity.ok(new ApiResponse<>(true,"Get random post successful",
                postService.getRandom(page,size)));
    }

}
