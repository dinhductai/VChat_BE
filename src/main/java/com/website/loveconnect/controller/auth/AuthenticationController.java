package com.website.loveconnect.controller.auth;

import com.cloudinary.Api;
import com.nimbusds.jose.JOSEException;
import com.website.loveconnect.dto.request.AuthenticationRequest;
import com.website.loveconnect.dto.request.IntrospectRequest;
import com.website.loveconnect.dto.request.LogoutRequest;
import com.website.loveconnect.dto.request.UserCreateRequest;
import com.website.loveconnect.dto.response.ApiResponse;
import com.website.loveconnect.dto.response.AuthenticationResponse;
import com.website.loveconnect.dto.response.IntrospectResponse;
import com.website.loveconnect.service.AuthenticationService;
import com.website.loveconnect.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.core.Authentication;
import javax.naming.AuthenticationException;
import java.text.ParseException;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationService authenticationService;
    UserService userService;

    //tạo tài khoản người dùng
    @Operation(summary = "Create account",description = "User create a new account")
    @PostMapping(value = "/sign-up")
    public ResponseEntity<ApiResponse<String>> signUpAccount(@Valid @RequestBody UserCreateRequest userCreateRequest){
        userService.createUser(userCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true,"Create account successful", null));
    }

    //đăng nhập với email và password
    @PostMapping(value = "/log-in")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> authenticate(
            @Valid @RequestBody AuthenticationRequest authenticationRequest) {
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

    //xác thực token
    @PostMapping(value = "/introspect")
    public ResponseEntity<ApiResponse<IntrospectResponse>> authenticate(@RequestBody IntrospectRequest introspectRequest) {
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

    //logout token
    @PostMapping(value = "/logout")
    public ResponseEntity<ApiResponse<String>> logout(@RequestBody LogoutRequest logoutRequest) {
        try {
            authenticationService.logout(logoutRequest);
            return ResponseEntity.ok(new ApiResponse<>(true,"Logout successful", null));
        } catch (AuthenticationException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
    @GetMapping("/token-data")
    public ResponseEntity<?> getProfile(@AuthenticationPrincipal Jwt jwt) {
        String email = jwt.getClaimAsString("email");
        String idUser = jwt.getSubject();
        return ResponseEntity.ok("Your email: " + email + "  Your id:" +idUser);
    }
}
