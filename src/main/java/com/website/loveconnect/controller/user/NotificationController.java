package com.website.loveconnect.controller.user;

import com.website.loveconnect.dto.response.ApiResponse;
import com.website.loveconnect.dto.response.NotificationResponse;
import com.website.loveconnect.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api")
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class NotificationController {
    NotificationService notificationService;

    @Operation(summary = "Get notification")
    @GetMapping(value = "/notification")
    public ResponseEntity<ApiResponse<Page<NotificationResponse>>> getNotification(@AuthenticationPrincipal Jwt jwt,
                                                                                   @RequestParam(defaultValue = "0") int page,
                                                                                   @RequestParam(defaultValue = "10") int size){
        Integer userId = Integer.parseInt(jwt.getSubject());
        return ResponseEntity.ok(new ApiResponse<>(true,"Get notification successful",
                notificationService.getNewNotifications(userId,page,size)));
    }
}
