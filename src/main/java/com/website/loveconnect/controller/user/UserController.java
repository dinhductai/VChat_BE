package com.website.loveconnect.controller.user;

import com.website.loveconnect.dto.request.UserCreateRequest;
import com.website.loveconnect.dto.response.ApiResponse;
import com.website.loveconnect.dto.response.UserSearchResponse;
import com.website.loveconnect.service.LikeService;
import com.website.loveconnect.service.PhotoService;
import com.website.loveconnect.service.UserProfileService;
import com.website.loveconnect.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping(value = "/api")
public class UserController {
    UserService userService;
    PhotoService imageService;
    UserProfileService userProfileService;
    LikeService likeService;

    //tạo tài khoản người dùng
    @Operation(summary = "Create account",description = "User create a new account")
    @PostMapping(value = "/sign-up")
    public ResponseEntity<ApiResponse<String>> signUpAccount(@RequestBody UserCreateRequest userCreateRequest){
        userService.createUser(userCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true,"Create account successful", null));
    }

    //search người dùng khác
    @Operation(summary = "Search other user", description = "User find other by keyword")
    @GetMapping(value = "/user/search")
    public ResponseEntity<ApiResponse<Page<UserSearchResponse>>> searchUserByKeyword(
            @RequestParam(name = "keyword") String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size){
        return ResponseEntity.ok(new ApiResponse<>(true,"Search users successful",
                userService.getAllUserByKeyword(keyword,page,size)));
    }
}
