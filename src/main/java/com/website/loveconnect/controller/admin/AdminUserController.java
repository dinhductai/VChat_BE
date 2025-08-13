package com.website.loveconnect.controller.admin;

import com.website.loveconnect.dto.request.UserCreateRequest;
import com.website.loveconnect.dto.request.UserUpdateRequest;
import com.website.loveconnect.dto.response.ApiResponse;
import com.website.loveconnect.dto.response.ListUserResponse;
import com.website.loveconnect.dto.response.UserUpdateResponse;
import com.website.loveconnect.dto.response.UserViewResponse;
import com.website.loveconnect.service.PhotoService;
import com.website.loveconnect.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/admin")
//@CrossOrigin(origins = "http://127.0.0.1:5500")  chi ap dung local
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AdminUserController {

    UserService userService;
    PhotoService imageService;

    //lấy tất cả người dùng hoặc search
    @GetMapping(value = "/users")
    public ResponseEntity<ApiResponse<Page<ListUserResponse>>> getAllUser(
            @RequestParam(name = "status",required = false) String status,
            @RequestParam(name = "gender",required = false) String gender,
            //key sort: newest, oldest, name_asc, name_desc
            @RequestParam(name = "sort", required = false) String sort,
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        if (status != null || gender != null || keyword != null || sort != null) {
            return ResponseEntity.ok(new ApiResponse<>(true,
                    "Get list user by filters successful",
                    userService.getAllUserByFilters(status, gender, sort, keyword, page, size)));
        } else {
            return ResponseEntity.ok(new ApiResponse<>(true,
                    "Get list user successful",
                    userService.getAllUser(page, size)));
        }
    }

    //lấy thông tin chi tiết người dùng cho detail
    @GetMapping(value = "/users/{userId}")
    public ResponseEntity<ApiResponse<UserViewResponse>> getUserById(@PathVariable int userId) {
            UserViewResponse user = userService.getUserById(userId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Get user detail successful", user));

    }

    //block hoặc unlock người dùng bằng id
    @Operation(summary = "Block and active",description = "Block or reactive user account")
    @PatchMapping(value = "/users/{userId}")
    public ResponseEntity<ApiResponse<?>> blockOrActiveUser(@PathVariable int userId,
                                                         @RequestParam(defaultValue = "block") String status) {
        if("active".equalsIgnoreCase(status)) {
            userService.unblockUser(userId);
            return ResponseEntity.ok(new ApiResponse<>(true,"User unblocked successfully", null));
        }else {
            userService.blockUser(userId);
            return ResponseEntity.ok(new ApiResponse<>(true, "User blocked successfully", null));
        }
    }


    //lấy thông tin người dùng cần sửa
    @GetMapping(value = "/users/{userId}/edit")
    public ResponseEntity<ApiResponse<UserUpdateResponse>> getUserUpdateById(@PathVariable int userId) {
        UserUpdateResponse user = userService.getUserUpdateById(userId);
        return ResponseEntity.ok(new ApiResponse<>(true,"Get user successful",user));
    }


    //cập nhật người dùng
    @PutMapping(value = "/users/{userId}")
    public ResponseEntity<ApiResponse<UserUpdateResponse>> updateUserById(@PathVariable int userId,
                                                                          @Valid @RequestBody UserUpdateRequest userUpdateRequest) {
        UserUpdateResponse user = userService.updateUser(userId,userUpdateRequest);
        return ResponseEntity.ok(new ApiResponse<>(true,"Get user successful",user));
    }

    //xóa người dùng
    @DeleteMapping(value = "/users/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable int userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    //tạo người dùng
    @PostMapping(value = "/users")
    public ResponseEntity<ApiResponse<String>> createUser(@RequestBody UserCreateRequest userCreateRequest){
        userService.createUser(userCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true,"Create user successful", null));
    }

    //lấy danh sách người dùng bằng filter
//    @GetMapping(value = "/users/search")
//    public ResponseEntity<ApiResponse<Page<ListUserResponse>>> getAllUserByFilters(
//            @RequestParam(name = "status",required = false) String status,
//            @RequestParam(name = "gender",required = false) String gender,
//            //key sort: newest, oldest, name_asc, name_desc
//            @RequestParam(name = "sort", required = false) String sort,
//            @RequestParam(name = "keyword", required = false) String keyword,
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size){
//        return ResponseEntity.ok(new ApiResponse<>(true,"Get list user by filters successful",
//                userService.getAllUserByFilters(status,gender,sort,keyword,page,size)));
//    }


}
