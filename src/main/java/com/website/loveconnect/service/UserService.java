package com.website.loveconnect.service;

import com.website.loveconnect.dto.response.ListUserResponse;
import com.website.loveconnect.dto.response.UserResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService {
    Page<ListUserResponse> getAllUser(int page, int size);
    UserResponse getUserById(int idUser);
    void blockUser(int idUser);
    void unblockUser(int idUser);
}
