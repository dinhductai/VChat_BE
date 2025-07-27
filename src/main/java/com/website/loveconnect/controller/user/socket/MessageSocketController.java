package com.website.loveconnect.controller.user.socket;

import com.website.loveconnect.dto.request.MessageLoadRequest;
import com.website.loveconnect.dto.request.MessageRequest;
import com.website.loveconnect.dto.response.MessageResponse;
import com.website.loveconnect.service.MessageService;
import com.website.loveconnect.util.MessageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class MessageSocketController {
    private final MessageService messageService;
    private final SimpMessagingTemplate messagingTemplate;
    private final JwtDecoder jwtDecoder;

    @MessageMapping(value = "/message.private")
    public void createMessage(@Payload MessageRequest messageRequest,
                                         @AuthenticationPrincipal Jwt jwt) {
        Integer senderId=  Integer.parseInt(jwt.getSubject());
        MessageResponse messageResponse=  messageService.createMessage(messageRequest,senderId);
        String chatChanel = MessageUtil.createChatChannel(senderId,messageRequest.getReceiverId());
        messagingTemplate.convertAndSend(chatChanel,messageResponse);
    }

    @MessageMapping(value = "/message.history")
    public void fetchAllMessages(@AuthenticationPrincipal Jwt jwt,
                                 @Payload MessageLoadRequest messageLoadRequest) {
        Integer senderId=  Integer.parseInt(jwt.getSubject());
        Page<MessageResponse> messageResponses = messageService
                .getAllMessageBySenderIdAndReceiverId(senderId, messageLoadRequest);

        messagingTemplate.convertAndSendToUser(
                jwt.getSubject(),
                "/queue/messages.history",
                messageResponses
        );
    }
}
