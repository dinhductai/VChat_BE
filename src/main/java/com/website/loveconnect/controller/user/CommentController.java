package com.website.loveconnect.controller.user;

import com.website.loveconnect.dto.request.CommentRequest;
import com.website.loveconnect.dto.response.ApiResponse;
import com.website.loveconnect.entity.Comment;
import com.website.loveconnect.service.CommentService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api")
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class CommentController {
    CommentService commentService;

    @Operation(summary = "Create comment",description = "Create 1 comment on a post, a comment")
    @PostMapping(value = "/comment/create")
    public ResponseEntity<ApiResponse<String>> createComment(@Valid @RequestBody CommentRequest commentRequest,
                                                             @AuthenticationPrincipal Jwt jwt) {
        Integer userId = Integer.parseInt(jwt.getSubject());
        commentService.createComment(commentRequest,userId);
        return ResponseEntity.ok(new ApiResponse<>(true,"Create comment successful",null));
    }

}
