package com.website.loveconnect.exception;

import com.website.loveconnect.dto.response.ApiResponse;
import com.website.loveconnect.dto.response.ErrorDetailResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.net.SocketException;
import java.security.Principal;
import java.time.LocalDateTime;

@ControllerAdvice
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class SocketExceptionHandler {
    SimpMessagingTemplate simpMessagingTemplate;
    JwtDecoder jwtDecoder;
    @MessageExceptionHandler(Exception.class)
    public void handleSocketException(Exception e, Principal principal) {
        ErrorDetailResponse detailResponse = new ErrorDetailResponse(LocalDateTime.now(),"Websocket error",e.getMessage());
        // Gửi thông báo lỗi về kênh riêng của người dùng gây ra lỗi
        simpMessagingTemplate.convertAndSendToUser(
                principal.getName(),
                "/queue/errors",
                detailResponse
        );
    }


}
