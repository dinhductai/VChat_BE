package com.website.loveconnect.controller.user;

import com.website.loveconnect.dto.request.UserCreateRequest;
import com.website.loveconnect.dto.response.ApiResponse;
import com.website.loveconnect.dto.response.UserFriendResponse;
import com.website.loveconnect.dto.response.UserSearchResponse;
import com.website.loveconnect.service.LikeService;
import com.website.loveconnect.service.PhotoService;
import com.website.loveconnect.service.UserProfileService;
import com.website.loveconnect.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
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


    @Operation(summary = "Get friends by type", description = "Get friends list based on type")
    @GetMapping(value = "/user/friends")
    public ResponseEntity<ApiResponse<Page<UserFriendResponse>>> getFriendsByType(
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam(defaultValue = "matched") String type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Integer userId = Integer.parseInt(jwt.getSubject());
        Page<UserFriendResponse> result;

        switch (type.toLowerCase()) {
            case "matched":
                result = userService.getAllFriendsMatched(page, size, userId);
                break;
            case "friends_of_friends":
                result = userService.getFiendsFriendsMatched(page, size, userId);
                break;
            case "random":
                result = userService.getRandomFriends(page, size, userId);
                break;
            case "pending":
                result = userService.getAllFriendsPending(page, size, userId);
                break;
            default:
                throw new IllegalArgumentException("Invalid type: " + type);
        }

        return ResponseEntity.ok(new ApiResponse<>(true, "Get friends successful", result));
    }

//
//    @Operation(summary = "Get friends",description = "Get all friend matched with owner")
//    @GetMapping(value = "/user/friends")
//    public ResponseEntity<ApiResponse<Page<UserFriendResponse>>> getFriends(@AuthenticationPrincipal Jwt jwt,
//                                                                            @RequestParam(defaultValue = "0") int page,
//                                                                            @RequestParam(defaultValue = "10") int size){
//        Integer userId = Integer.parseInt(jwt.getSubject());
//        return ResponseEntity.ok(new ApiResponse<>(true,"Get friends successful",
//                userService.getAllFriendsMatched(page,size,userId)));
//    }
//
//    @Operation(summary = "Get friends friends",description = "Get all friend matched with owner friends")
//    @GetMapping(value = "/user/friends-friends")
//    public ResponseEntity<ApiResponse<Page<UserFriendResponse>>> getFriendsFriends(@AuthenticationPrincipal Jwt jwt,
//                                                                            @RequestParam(defaultValue = "0") int page,
//                                                                            @RequestParam(defaultValue = "10") int size){
//        Integer userId = Integer.parseInt(jwt.getSubject());
//        return ResponseEntity.ok(new ApiResponse<>(true,"Get friends friends successful",
//                userService.getFiendsFriendsMatched(page,size,userId)));
//    }
//
//    @Operation(summary = "Get random friends",description = "Get random friends")
//    @GetMapping(value = "/user/random-friends")
//    public ResponseEntity<ApiResponse<Page<UserFriendResponse>>> getRandomFriends(@AuthenticationPrincipal Jwt jwt,
//                                                                                   @RequestParam(defaultValue = "0") int page,
//                                                                                   @RequestParam(defaultValue = "10") int size){
//        Integer userId = Integer.parseInt(jwt.getSubject());
//        return ResponseEntity.ok(new ApiResponse<>(true,"Get friends friends successful",
//                userService.getRandomFriends(page,size,userId)));
//    }
//
//    @Operation(summary = "Get friends",description = "Get all friend matched with owner")
//    @GetMapping(value = "/user/friends-pending")
//    public ResponseEntity<ApiResponse<Page<UserFriendResponse>>> getFriendsPending(@AuthenticationPrincipal Jwt jwt,
//                                                                            @RequestParam(defaultValue = "0") int page,
//                                                                            @RequestParam(defaultValue = "10") int size){
//        Integer userId = Integer.parseInt(jwt.getSubject());
//        return ResponseEntity.ok(new ApiResponse<>(true,"Get friends successful",
//                userService.getAllFriendsPending(page,size,userId)));
//    }
}
