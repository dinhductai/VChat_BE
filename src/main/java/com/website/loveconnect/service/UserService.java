package com.website.loveconnect.service;

import com.website.loveconnect.dto.response.UserResponse;
import org.springframework.data.domain.Page;

public interface UserService {
    Page<UserResponse> getAllUser(int page, int size);
}
