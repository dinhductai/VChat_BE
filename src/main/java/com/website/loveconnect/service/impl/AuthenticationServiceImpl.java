package com.website.loveconnect.service.impl;

import com.website.loveconnect.dto.request.AuthenticationRequest;
import com.website.loveconnect.entity.User;
import com.website.loveconnect.entity.UserProfile;
import com.website.loveconnect.exception.UserNotFoundException;
import com.website.loveconnect.repository.UserProfileRepository;
import com.website.loveconnect.repository.UserRepository;
import com.website.loveconnect.service.AuthenticationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {
    UserProfileRepository userProfileRepository;
    UserRepository userRepository;

    @Override
    public boolean authenticate(AuthenticationRequest authenticationRequest) {
        User user = userRepository.getUserByEmail(authenticationRequest.getEmail())
                .orElseThrow(()->new UserNotFoundException("User not found with email: " +
                        authenticationRequest.getEmail()));

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        //check password vừa đc gửi về (raw pass) với password trong db đã được mã hóa
        return passwordEncoder.matches(authenticationRequest.getPassword(), user.getPassword());
    }
}
