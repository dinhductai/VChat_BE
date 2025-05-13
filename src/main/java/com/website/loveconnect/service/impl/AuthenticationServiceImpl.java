package com.website.loveconnect.service.impl;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.website.loveconnect.dto.request.AuthenticationRequest;
import com.website.loveconnect.dto.request.IntrospectRequest;
import com.website.loveconnect.dto.request.LogoutRequest;
import com.website.loveconnect.dto.response.AuthenticationResponse;
import com.website.loveconnect.dto.response.IntrospectResponse;
import com.website.loveconnect.entity.InvalidatedToken;
import com.website.loveconnect.entity.User;
import com.website.loveconnect.entity.UserProfile;
import com.website.loveconnect.exception.ExpiredJwtException;
import com.website.loveconnect.exception.UserNotFoundException;
import com.website.loveconnect.mapper.RoleMapper;
import com.website.loveconnect.repository.InvalidatedTokenRepository;
import com.website.loveconnect.repository.RoleRepository;
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
import java.util.*;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {
    UserProfileRepository userProfileRepository;
    UserRepository userRepository;
    RoleRepository roleRepository;
    RoleMapper roleMapper;
    InvalidatedTokenRepository invalidatedTokenRepository;


    @NonFinal
    @Value("${jwt.secret}")
    protected String SIGNED_KEY;


    //hàm xác thực tài khoản người dùng bằng email và password
    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) throws JOSEException {
        //tìm thông tin người dùng bằng email
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

        //dựa vào iduser,tìm userprofile để lấy tên tài khoản cho token payload
        UserProfile userProfile = userProfileRepository.findByUser_UserId(user.getUserId())
                .orElseThrow(()-> new UserNotFoundException("User not found with id: " + user.getUserId()));

        //lấy các role thuộc user đang đăng nhập để phân quyền
        List<String> listRoleUser = userRepository.getUserRoleByUserId(user.getUserId());
        StringBuilder scopeBuilder = new StringBuilder();
        // Thêm role vào đầu tiên
        for (String role : listRoleUser) {
            if (scopeBuilder.length() > 0) {
                scopeBuilder.append(" "); // thêm dấu cách nếu không phải phần tử đầu
            }
            scopeBuilder.append(role);
        }

        //gộp permission từ tất cả role, loại bỏ trùng lặp
        Set<String> uniquePermissions = new HashSet<>(); //set để loại bỏ trùng lặp
        for (String role : listRoleUser) {
            List<String> listPermissionByRole = roleMapper
                    .toListPermissionByRoleName(roleRepository.getPermissionByRoleName(role));
            uniquePermissions.addAll(listPermissionByRole); //thêm tất cả permission, tự động loại trùng
        }

        //thêm permission vào scope, tách permission riêng ra
        for (String permission : uniquePermissions) {
            if (scopeBuilder.length() > 0) {
                scopeBuilder.append(" "); // Thêm dấu cách
            }
            scopeBuilder.append(permission);
        }

        String scope = scopeBuilder.toString();
        String token = generateToken(user.getUserId(),scope,user.getEmail());
        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .build();
    }


    //hàm kiểm tra token hợp lệ,trạng thái của token
    @Override
    public IntrospectResponse introspect(IntrospectRequest introspectRequest) throws AuthenticationException {
        String token = introspectRequest.getToken();
        boolean isInvalid = true;
        try {
            verifyToken(token);
        }catch (ExpiredJwtException e) {
            //nếu gặp trường hợp token đã bị logout thì chỉ cần đổi thành false là đc,ko cần trả exception
            isInvalid = false;
        }
        return IntrospectResponse.builder()
                //check token sau tg hiện tại và đc xác thực
                .valid(isInvalid)
                .build();
    }

    @Override
    public void logout(LogoutRequest logoutRequest) throws ParseException {
        SignedJWT signedJWT = verifyToken(logoutRequest.getToken());
        //lấy claim set ra
        String jwtTokenId = signedJWT.getJWTClaimsSet().getJWTID();
        Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                .token(jwtTokenId)
                .expiryTime(expirationTime)
                .build();
        invalidatedTokenRepository.save(invalidatedToken);
    }

    private SignedJWT verifyToken(String token) {
        try {
            JWSVerifier verifier = new MACVerifier(SIGNED_KEY.getBytes());
            SignedJWT signedJWT = SignedJWT.parse(token);
            //kiểm tra xác thực
            boolean checkVerified = signedJWT.verify(verifier);
            //lấy thời gian hết hạn
            Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();
            if (!checkVerified && expiryTime.after(new Date())) {
                throw new ExpiredJwtException("Token expired");
            }else if (invalidatedTokenRepository.existsByToken(signedJWT.getJWTClaimsSet().getJWTID())){
                throw new ExpiredJwtException("Token expired");
            }else return signedJWT;

        } catch (JOSEException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

    }

    //hàm sinh token bằng HS512 và secret key
    private String generateToken(Integer userId,String roleString,String email) throws JOSEException {
        //thuật toán mã hóa header
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        //set claim
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(String.valueOf(userId))//đại diện cho user đăng nhập
                .issuer("website.com")//xác định token issue từ đâu ra, xác định nguồn gốc của token
                .issueTime(new Date()) //thời gian tạo
                .expirationTime(new Date(
                        //set thời gian token hết hạn là 1 giờ sau đó
                        Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()
                ))
                .jwtID(UUID.randomUUID().toString())
                .claim("email",email)
                .claim("scope",roleString)
                .build();

        //set claim cho payload
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header,payload);
        try {
            //ký với secret key
            jwsObject.sign(new MACSigner(SIGNED_KEY.getBytes(StandardCharsets.UTF_8)));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Cannot create token", e);
            throw e;
        }
    }
}
