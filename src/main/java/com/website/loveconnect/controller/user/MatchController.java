package com.website.loveconnect.controller.user;

import com.cloudinary.Api;
import com.website.loveconnect.dto.response.*;
import com.website.loveconnect.enumpackage.MatchStatus;
import com.website.loveconnect.service.MatchService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api")
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class MatchController {

    MatchService matchService;

    @Operation(summary = "Get all matches",description = "Get all friend matched")
    @GetMapping(value = "/matches")
    public ResponseEntity<ApiResponse<List<MatchBySenderResponse>>> getMatches(@AuthenticationPrincipal Jwt jwt,
                                                                               @RequestParam(defaultValue = "0") int page,
                                                                               @RequestParam(defaultValue = "10") int size) {
        int userId = Integer.parseInt(jwt.getSubject());
        return ResponseEntity.ok(new ApiResponse<>(true,"Get all match by sender id successful",
                matchService.getAllMatchBySenderId(userId, page, size)));
    }

    @Operation(summary = "Get one match", description = "Get one match by id")
    @GetMapping(value = "/matches/{matchId}")
    public ResponseEntity<ApiResponse<MatchMatchIdResponse>> getMatchBySenderId(@PathVariable int matchId){
        return ResponseEntity.ok(new ApiResponse<>(true,"Get match by match id successful",
                matchService.getMatchMatchId(matchId)));
    }

    @Operation(summary = "Get user matches",description = "Get all user's matches")
    @GetMapping(value = "/user-matches")
    public ResponseEntity<ApiResponse<List<UserMatchedResponse>>> getUserMatches(@AuthenticationPrincipal Jwt jwt,
                                                                                 @RequestParam(defaultValue = "0") int page,
                                                                                 @RequestParam(defaultValue = "10") int size) {
        int userId = Integer.parseInt(jwt.getSubject());
        return ResponseEntity.ok(new ApiResponse<>(true,"Get all match by sender id successful",
                matchService.getAllUserMatched(userId, page, size)));
    }

    @Operation(summary = "Create request friend", description = "User1 send a request friend to user2")
    @PostMapping(value = "/match/create")
    public ResponseEntity<ApiResponse<String>> createRequestFriend(@AuthenticationPrincipal Jwt jwt,
                                                                   @RequestParam("receiverId") Integer receiverId){
        Integer senderId = Integer.parseInt(jwt.getSubject());
        matchService.createRequestFriend(senderId,receiverId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true,"Create request friend successful", null));
    }
    @Operation(summary = "Update request friend", description = "User1 accept or reject a request friend from user2")
    @PutMapping(value = "/match/update")
    public ResponseEntity<ApiResponse<String>> updateRequestFriend(@AuthenticationPrincipal Jwt jwt,
                                                                   @RequestParam("receiverId") Integer receiverId,
                                                                   @RequestParam("matchStatus") MatchStatus matchStatus){
        Integer senderId = Integer.parseInt(jwt.getSubject());
        matchService.updateRequestFriend(senderId,receiverId,matchStatus);
        return ResponseEntity.ok(new ApiResponse<>(true,"Update request friend successful", null));
    }

    @Operation(summary = "Check match status",description = "Check status match between user and other")
    @GetMapping(value = "/match")
    public ResponseEntity<ApiResponse<MatchStatusResponse>> getMatchStatus(@AuthenticationPrincipal Jwt jwt,
                                                                           @RequestParam("otherUserId") Integer otherUserId){
        Integer userId = Integer.parseInt(jwt.getSubject());
        return ResponseEntity.ok(new ApiResponse<>(true,"Get match status successful",
                matchService.getMatchStatusType(userId, otherUserId)));
    }
}
