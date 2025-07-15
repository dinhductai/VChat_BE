package com.website.loveconnect.controller.user.socket;

import com.website.loveconnect.dto.request.CommentRequest;
import com.website.loveconnect.dto.response.CommentResponse;
import com.website.loveconnect.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class CommentSocketController {

    CommentService commentService;
    SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/comments.add/{postId}")
    public void addComment(@DestinationVariable Integer postId,
                           @Payload CommentRequest request,
                           @AuthenticationPrincipal Jwt jwt) {
        Integer userId = Integer.parseInt(jwt.getSubject());
        CommentResponse newComment;

        if (request.getParentCommentId() == null) {
            newComment = commentService.createComment(request, userId);
        } else {
            newComment = commentService.repComment(request, userId);
        }

        // Broadcast comment mới đến topic công khai của bài viết
        messagingTemplate.convertAndSend("/topic/posts/" + postId + "/comments", newComment);
    }


    @MessageMapping("/comments.fetchAll/{postId}")
    public void fetchAllComments(@DestinationVariable Integer postId,
                                 @AuthenticationPrincipal Jwt jwt) {
        List<CommentResponse> commentTree = commentService.getCommentTreeByPostId(postId);

        // Gửi cây bình luận về hàng đợi riêng của người dùng
        messagingTemplate.convertAndSendToUser(
                jwt.getSubject(),
                "/queue/comments",
                commentTree
        );
    }
}