package com.website.loveconnect.controller.user.socket;

import com.website.loveconnect.dto.request.MessageDeleteRequest;
import com.website.loveconnect.dto.request.MessageLoadRequest;
import com.website.loveconnect.dto.request.MessageRequest;
import com.website.loveconnect.dto.request.MessageUpdateRequest;
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

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class MessageSocketController {
    private final MessageService messageService;
    private final SimpMessagingTemplate messagingTemplate;
    private final JwtDecoder jwtDecoder;

    @MessageMapping(value = "/message.private")
    public void createMessage(@Payload MessageRequest messageRequest) {
        Jwt jwt = jwtDecoder.decode(messageRequest.getToken());
        Integer senderId = Integer.parseInt(jwt.getSubject());
        MessageResponse messageResponse=  messageService.createMessage(messageRequest,senderId);
        String chatChanel = MessageUtil.createChatChannel(senderId,messageRequest.getReceiverId());
        messagingTemplate.convertAndSend(chatChanel,messageResponse);
    }

    @MessageMapping(value = "/message.edit")
    public void editMessage(@Payload MessageUpdateRequest request) {
        Jwt jwt = jwtDecoder.decode(request.getToken());
        Integer senderId = Integer.parseInt(jwt.getSubject());
        MessageResponse updatedMessageResponse = messageService.updateMessage(request,senderId);
        String chatChannel = MessageUtil.createChatChannel(
                updatedMessageResponse.getSenderId(),
                updatedMessageResponse.getReceiverId()
        );
        messagingTemplate.convertAndSend(chatChannel, updatedMessageResponse);
    }

    @MessageMapping(value = "/message.delete")
    public void deleteMessage(@Payload MessageDeleteRequest request) {
        Jwt jwt = jwtDecoder.decode(request.getToken());
        Integer senderId = Integer.parseInt(jwt.getSubject());
        MessageResponse deletedMessageResponse = messageService.deleteMessage(request, senderId);
        String chatChannel = MessageUtil.createChatChannel(
                deletedMessageResponse.getSenderId(),
                deletedMessageResponse.getReceiverId()
        );
        messagingTemplate.convertAndSend(chatChannel, deletedMessageResponse);
    }

//    @MessageMapping(value = "/message.history")
//    public void fetchAllMessages(Principal principal,
//                                 @Payload MessageLoadRequest messageLoadRequest) {
//        Integer senderId=  Integer.parseInt(principal.getName());
//        Page<MessageResponse> messageResponses = messageService
//                .getAllMessageBySenderIdAndReceiverId(senderId, messageLoadRequest);
//
//        messagingTemplate.convertAndSendToUser(
//                principal.getName(),
//                "/queue/messages.history",
//                messageResponses
//        );
//    }
}
