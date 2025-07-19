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
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class CommentSocketController {

    private final CommentService commentService;
    private final SimpMessagingTemplate messagingTemplate;
    private final JwtDecoder jwtDecoder;
    @MessageMapping("/comments.add/{postId}")
    public void addComment(@DestinationVariable Integer postId,
                           @Payload CommentRequest request) {

        // Lấy userId từ Principal đã được xác thực an toàn
//        Integer userId = Integer.parseInt(principal.getName());
        Jwt jwt = jwtDecoder.decode(request.getToken());
        Integer userId = Integer.parseInt(jwt.getSubject());
        CommentResponse newComment;

        if (request.getParentCommentId() == null) {
            newComment = commentService.createComment(request, userId);
        } else {
            newComment = commentService.repComment(request, userId);
        }

        messagingTemplate.convertAndSend("/topic/posts/" + postId + "/comments", newComment);
    }


    @MessageMapping("/comments.fetchAll/{postId}")
    public void fetchAllComments(@DestinationVariable Integer postId, Principal principal) {
        // Gọi service để lấy toàn bộ cây bình luận
        List<CommentResponse> commentTree = commentService.getCommentTreeByPostId(postId);

        // Gửi cây bình luận về hàng đợi (queue) riêng của người dùng
        messagingTemplate.convertAndSendToUser(
                principal.getName(),      // Tên định danh của user, Spring sẽ tự tìm đúng session
                "/queue/comments",      // Đích đến riêng tư
                commentTree               // Dữ liệu cần gửi
        );
    }
}