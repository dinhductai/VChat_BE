package com.website.loveconnect.controller.auth;

import com.cloudinary.Api;
import com.nimbusds.jose.JOSEException;
import com.website.loveconnect.dto.request.AuthenticationRequest;
import com.website.loveconnect.dto.request.IntrospectRequest;
import com.website.loveconnect.dto.response.ApiResponse;
import com.website.loveconnect.dto.response.AuthenticationResponse;
import com.website.loveconnect.dto.response.IntrospectResponse;
import com.website.loveconnect.service.AuthenticationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationService authenticationService;

    //đăng nhập với email và password
    @PostMapping(value = "/log-in")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> authenticate(
            @RequestBody AuthenticationRequest authenticationRequest) {
        AuthenticationResponse checkAuthenticate = null;
        try {
            checkAuthenticate = authenticationService.authenticate(authenticationRequest);
        } catch (AuthenticationException e) {
            throw new RuntimeException(e);
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(ApiResponse.<AuthenticationResponse>builder()
                .success(true)
                .message("Log in successful")
                .data(checkAuthenticate)
                .build());
    }

    @PostMapping(value = "/introspect")
    public ResponseEntity<ApiResponse<IntrospectResponse>> authenticate(
            @RequestBody IntrospectRequest introspectRequest) {
        try {
            IntrospectResponse introspectResponse = authenticationService.introspect(introspectRequest);
            return ResponseEntity.ok(ApiResponse.<IntrospectResponse>builder()
                    .success(true)
                    .message("Verified successful")
                    .data(introspectResponse)
                    .build());
        } catch (AuthenticationException e) {
            throw new RuntimeException(e);
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }
}
