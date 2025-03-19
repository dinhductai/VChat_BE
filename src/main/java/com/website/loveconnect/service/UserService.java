package com.website.loveconnect.service;

import com.website.loveconnect.dto.request.UserUpdateRequest;
import com.website.loveconnect.dto.response.ListUserResponse;
import com.website.loveconnect.dto.response.UserUpdateResponse;
import com.website.loveconnect.dto.response.UserViewResponse;
import org.springframework.data.domain.Page;

public interface UserService {
    Page<ListUserResponse> getAllUser(int page, int size);
    UserViewResponse getUserById(int idUser);
    void blockUser(int idUser);
    void unblockUser(int idUser);
    UserUpdateResponse getUserUpdateById(int idUser);
    UserUpdateResponse updateUser(Integer idUser,UserUpdateRequest userRequest);
}
