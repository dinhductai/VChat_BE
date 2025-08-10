package com.website.loveconnect.controller.user;

import com.cloudinary.Api;
import com.website.loveconnect.dto.request.PostRequest;
import com.website.loveconnect.dto.request.PostShareSaveRequest;
import com.website.loveconnect.dto.request.PostUpdateRequest;
import com.website.loveconnect.dto.request.ReelRequest;
import com.website.loveconnect.dto.response.ApiResponse;
import com.website.loveconnect.dto.response.PostResponse;
import com.website.loveconnect.dto.response.ReelResponse;
import com.website.loveconnect.dto.response.UserAndPhotosResponse;
import com.website.loveconnect.service.PostService;
import com.website.loveconnect.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
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
public class PostController {
    PostService postService;
    UserService userService;


    @Operation(summary = "Create post",description = "Create a post with text, image or post")
    @PostMapping(value = "/post/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<PostResponse>> createPost(@Valid  @ModelAttribute PostRequest postRequest,
                                                          @AuthenticationPrincipal Jwt jwt){
        String userEmail = jwt.getClaimAsString("email");
        postRequest.setUserEmail(userEmail);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true,"Create post successful",postService.savePost(postRequest)));
    }

    @Operation(summary = "Get random post",
                description = "Get all random post with page index automatically increase")
    @GetMapping(value = "/post")
    public ResponseEntity<ApiResponse<Page<PostResponse>>> getRandomPost(@RequestParam(defaultValue = "0") int page,
                                                                         @RequestParam(defaultValue = "3") int size){
        return ResponseEntity.ok(new ApiResponse<>(true,"Get random post successful",
                postService.getRandom(page,size)));
    }

    @Operation(summary = "Get one post",description = "Get one post by postId")
    @GetMapping(value = "/post/{postId}")
    public ResponseEntity<ApiResponse<PostResponse>> getPostById(@PathVariable Integer postId){
        return ResponseEntity.ok(new ApiResponse<>(true,"Get one post successful",
                postService.getPostById(postId)));
    }

    @Operation(summary = "Get own posts",description = "Get all post of owner")
    @GetMapping(value = "/post/owner")
    public ResponseEntity<ApiResponse<Page<PostResponse>>> getOwnPosts(@RequestParam(defaultValue = "0") int page,
                                                                       @RequestParam(defaultValue = "3") int size,
                                                                       @AuthenticationPrincipal Jwt jwt,
                                                                       @RequestParam(name = "userId", required = false) Integer userId){
        Integer finalId;
        if(userId != null){
            finalId=userId;
        }else  finalId = Integer.parseInt(jwt.getSubject());
        return ResponseEntity.ok(new ApiResponse<>(true,"Get own posts successful",
                postService.getOwnPost(finalId,page,size)));
    }

    @Operation(summary = "Create reel",description = "Create a reel with text and post")
    @PostMapping(value = "/reel/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<ReelResponse>> createPost(@Valid  @ModelAttribute ReelRequest reelRequest,
                                                                @AuthenticationPrincipal Jwt jwt){
        String userEmail = jwt.getClaimAsString("email");
        reelRequest.setUserEmail(userEmail);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true,"Create reel successful",postService.createReel(reelRequest)));
    }

    @Operation(summary = "Get random reel",
            description = "Get all random reel with page index automatically increase")
    @GetMapping(value = "/reel")
    public ResponseEntity<ApiResponse<Page<ReelResponse>>> getRandomReel(@RequestParam(defaultValue = "0") int page,
                                                                         @RequestParam(defaultValue = "10") int size){
        return ResponseEntity.ok(new ApiResponse<>(true,"Get random post successful",
                postService.getReelRandom(page,size)));
    }

    @Operation(summary = "Share post",description = "User share a post to profile")
    @PostMapping(value = "/post/share-save")
    public ResponseEntity<ApiResponse<String>> sharePost(@RequestBody PostShareSaveRequest request,
                                                         @AuthenticationPrincipal Jwt jwt){
        Integer userId = Integer.parseInt(jwt.getSubject());
        postService.shareOrSavePost(request,userId);
        return ResponseEntity.ok(new ApiResponse<>(true,"Share or save post successful", null));
    }


    @Operation(summary = "Delete post",description = "Delete a post by postId")
    @DeleteMapping(value = "/post/delete")
    public ResponseEntity<ApiResponse<String>> deletePost(@RequestParam("postId") Integer postId,
                                                          @AuthenticationPrincipal Jwt jwt){
        Integer userId = Integer.parseInt(jwt.getSubject());
        postService.deletePost(postId,userId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Update post",description = "Update content post by postId")
    @PatchMapping(value = "/post/update")
    public ResponseEntity<ApiResponse<PostResponse>> updatePost(@RequestBody PostUpdateRequest request,
                                                                @AuthenticationPrincipal Jwt jwt){
        Integer userId = Integer.parseInt(jwt.getSubject());
        return ResponseEntity.ok(new ApiResponse<>(true,"Update post successful",
                postService.updatePostById(request,userId)));
    }

    @Operation(summary = "Get own posts shared",description = "Get all post shared of owner")
    @GetMapping(value = "/post/share")
    public ResponseEntity<ApiResponse<Page<PostResponse>>> getOwnPostsShared(@RequestParam(defaultValue = "0") int page,
                                                                       @RequestParam(defaultValue = "3") int size,
                                                                       @AuthenticationPrincipal Jwt jwt){
        Integer userId = Integer.parseInt(jwt.getSubject());
        return ResponseEntity.ok(new ApiResponse<>(true,"Get own posts successful",
                postService.getOwnPostShared(userId,page,size)));
    }

    @Operation(summary = "Get own posts saved",description = "Get all post saved of owner")
    @GetMapping(value = "/post/save")
    public ResponseEntity<ApiResponse<Page<PostResponse>>> getOwnPostsSaved(@RequestParam(defaultValue = "0") int page,
                                                                       @RequestParam(defaultValue = "3") int size,
                                                                       @AuthenticationPrincipal Jwt jwt){
            Integer userId = Integer.parseInt(jwt.getSubject());
        return ResponseEntity.ok(new ApiResponse<>(true,"Get own posts successful",
                postService.getOwnPostSaved(userId,page,size)));
    }

}
