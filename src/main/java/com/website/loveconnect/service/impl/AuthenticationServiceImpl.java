package com.website.loveconnect.service.impl;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.website.loveconnect.dto.request.AuthenticationRequest;
import com.website.loveconnect.dto.request.IntrospectRequest;
import com.website.loveconnect.dto.response.AuthenticationResponse;
import com.website.loveconnect.dto.response.IntrospectResponse;
import com.website.loveconnect.entity.User;
import com.website.loveconnect.entity.UserProfile;
import com.website.loveconnect.exception.UserNotFoundException;
import com.website.loveconnect.repository.UserProfileRepository;
import com.website.loveconnect.repository.UserRepository;
import com.website.loveconnect.service.AuthenticationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import javax.naming.AuthenticationException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {
    UserProfileRepository userProfileRepository;
    UserRepository userRepository;

    @NonFinal
    @Value("${jwt.secret}")
    protected String SIGNED_KEY;


    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        User user = userRepository.getUserByEmail(authenticationRequest.getEmail())
                .orElseThrow(()->new UserNotFoundException("User not found with email: " +
                        authenticationRequest.getEmail()));

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        //check password vừa đc gửi về (raw pass) với password trong db đã được mã hóa
        boolean checkAuthenticate = passwordEncoder.matches(authenticationRequest.getPassword(),
                user.getPassword());
        if(!checkAuthenticate) {
            throw new BadCredentialsException("Incorrect email or password");
        }

        UserProfile userProfile = userProfileRepository.findByUser_UserId(user.getUserId())
                .orElseThrow(()-> new UserNotFoundException("User not found with id: " + user.getUserId()));

        String token = generateToken(userProfile.getFullName());
        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .build();
    }

    @Override
    public IntrospectResponse introspect(IntrospectRequest introspectRequest) throws AuthenticationException {
        String token = introspectRequest.getToken();
        try {
            JWSVerifier verifier = new MACVerifier(SIGNED_KEY.getBytes());
            SignedJWT signedJWT = SignedJWT.parse(token);
            //kiểm tra xác thực
            boolean checkVerified = signedJWT.verify(verifier);
            //lấy thời gian hết hạn
            Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();
            return IntrospectResponse.builder()
                    //check token sau tg hiện tại và đc xác thực
                    .valid(checkVerified && expiryTime.after(new Date()))
                    .build();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

    }

    private String generateToken(String userName) throws JOSEException {
        //thuật toán mã hóa header
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        //set claim
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(userName)//đại diện cho user đăng nhập
                .issuer("website.com")//xác định token issue từ đâu ra
                .issueTime(new Date())
                .expirationTime(new Date(
                        //set thời gian token hết hạn là 1 giờ
                        Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()
                ))
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header,payload);
        try {
            jwsObject.sign(new MACSigner(SIGNED_KEY.getBytes(StandardCharsets.UTF_8)));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Cannot create token", e);
            throw e;
        }
    }
}
