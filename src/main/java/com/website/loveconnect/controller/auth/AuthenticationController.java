package com.website.loveconnect.controller.auth;

import com.cloudinary.Api;
import com.website.loveconnect.dto.request.AuthenticationRequest;
import com.website.loveconnect.dto.response.ApiResponse;
import com.website.loveconnect.dto.response.AuthenticationResponse;
import com.website.loveconnect.service.AuthenticationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationService authenticationService;

    @PostMapping(value = "/log-in")
    ResponseEntity<ApiResponse<AuthenticationResponse>> authenticate(
            @RequestBody AuthenticationRequest authenticationRequest) {
        boolean checkAuthenticate = authenticationService.authenticate(authenticationRequest);
        return ResponseEntity.ok(ApiResponse.<AuthenticationResponse>builder()
                .success(AuthenticationResponse.builder().authenticated(checkAuthenticate).build().isAuthenticated())
                .message("Log in successful")
                .data(null)
                .build());
    }
}
