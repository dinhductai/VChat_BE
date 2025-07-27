package com.website.loveconnect.controller.user.socket;

import com.website.loveconnect.dto.request.NotificationLoadRequest;
import com.website.loveconnect.dto.response.NotificationResponse;
import com.website.loveconnect.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class NotificationSocketController {
    private final NotificationService notificationService;
    private final SimpMessagingTemplate messagingTemplate;
    private final JwtDecoder jwtDecoder;

    @MessageMapping(value = "/notification.history")
    public void getAllNotification(Principal principal, @Payload NotificationLoadRequest request) {
        Integer userId =  Integer.parseInt(principal.getName());
        Page<NotificationResponse> responses = notificationService.getNewNotifications(userId,request.getPage()
                ,request.getSize());
        messagingTemplate.convertAndSendToUser(
                principal.getName(),
                "/queue/notification.history",
                responses
        );

    }
}
