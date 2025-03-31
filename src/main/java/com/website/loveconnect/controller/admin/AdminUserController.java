package com.website.loveconnect.controller.admin;

import com.cloudinary.Api;
import com.website.loveconnect.dto.request.UserCreateRequest;
import com.website.loveconnect.dto.request.UserUpdateRequest;
import com.website.loveconnect.dto.response.ApiResponse;
import com.website.loveconnect.dto.response.ListUserResponse;
import com.website.loveconnect.dto.response.UserUpdateResponse;
import com.website.loveconnect.dto.response.UserViewResponse;
import com.website.loveconnect.service.ImageService;
import com.website.loveconnect.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;

@RestController
@RequestMapping("/api/admin")
//@CrossOrigin(origins = "http://127.0.0.1:5500")  chi ap dung local
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AdminUserController {

    UserService userService;
    ImageService imageService;

    //lấy tất cả người dùng
    @GetMapping(value = "/users")
    public ResponseEntity<ApiResponse<Page<ListUserResponse>>> getAllUser(@RequestParam(defaultValue = "0") int page,
                                                                          @RequestParam(defaultValue = "10") int size) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info(authentication.getName());
        authentication.getAuthorities().forEach(grantedAuthority -> log.info(grantedAuthority.getAuthority()));

        return  ResponseEntity.ok(new ApiResponse<>(true,"get list user successful",
                userService.getAllUser(page,size)));
    }

    //lấy thông tin chi tiết người dùng
    @GetMapping(value = "/users/{userId}")
    public ResponseEntity<ApiResponse<UserViewResponse>> getUserById(@PathVariable int userId) {

        var authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info(authentication.getName());;
        authentication.getAuthorities().forEach(grantedAuthority -> log.info(grantedAuthority.getAuthority()));

        UserViewResponse user = userService.getUserById(userId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Get user successful", user));
    }

    //block người dùng bằng id
    @PutMapping(value = "/users/{userId}/block")
    public ResponseEntity<ApiResponse<String>> blockUser(@PathVariable int userId) {
        userService.blockUser(userId);
        return ResponseEntity.ok(new ApiResponse<>(true,"User blocked successfully", null));
    }

    //unblock người dùng bằng id
    @PutMapping(value = "/users/{userId}/unblock")
    public ResponseEntity<ApiResponse<String>> unblockUser(@PathVariable int userId) {
        userService.unblockUser(userId);
        return ResponseEntity.ok(new ApiResponse<>(true,"User unblocked successfully", null));
    }

    @GetMapping(value = "/users/{userId}/update")
    public ResponseEntity<ApiResponse<UserUpdateResponse>> getUserUpdateById(@PathVariable int userId) {
        UserUpdateResponse user = userService.getUserUpdateById(userId);
        return ResponseEntity.ok(new ApiResponse<>(true,"Get user successful",user));
    }


    @PutMapping(value = "/users/{userId}/update")
    public ResponseEntity<ApiResponse<UserUpdateResponse>> updateUserById(@PathVariable int userId,
                                                                          @RequestBody UserUpdateRequest userUpdateRequest) {
        UserUpdateResponse user = userService.updateUser(userId,userUpdateRequest);
        return ResponseEntity.ok(new ApiResponse<>(true,"Get user successful",user));
    }

    @DeleteMapping(value = "/users/{userId}/delete")
    public ResponseEntity<Void> deleteUser(@PathVariable int userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }


    @PostMapping(value = "/users/create")
    public ResponseEntity<ApiResponse<String>> createUser(@RequestBody UserCreateRequest userCreateRequest){
        userService.createUser(userCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true,"Create user successful", null));
    }

    //api dùng kèm với user create
    //tạo ảnh profile với 1 file và user email
    @PostMapping(value = "/users/profile-image/create",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<String>> createProfileImage(@RequestParam("file")MultipartFile file,
                                                   @RequestParam("userEmail") String userEmail) throws IOException {
        String urlImage = imageService.saveImageProfile(file,userEmail);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true,"Save profile image successful", urlImage));
    }

    //lấy danh sách người dùng bằng filter
    @GetMapping(value = "/users/search")
    public ResponseEntity<ApiResponse<Page<ListUserResponse>>> getAllUserByFilters(
            @RequestParam(name = "status",required = false) String status,
            @RequestParam(name = "gender",required = false) String gender,
            //key sort: newest, oldest, name_asc, name_desc
            @RequestParam(name = "sort", required = false) String sort,
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size){
        return ResponseEntity.ok(new ApiResponse<>(true,"Get list user by filters successful",
                userService.getAllUserByFilters(status,gender,sort,keyword,page,size)));
    }


}
