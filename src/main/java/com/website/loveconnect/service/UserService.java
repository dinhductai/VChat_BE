package com.website.loveconnect.service;

import com.website.loveconnect.dto.request.InterestDTO;
import com.website.loveconnect.dto.request.UserCreateRequest;
import com.website.loveconnect.dto.request.UserUpdateRequest;
import com.website.loveconnect.dto.response.ListUserResponse;
import com.website.loveconnect.dto.response.UserUpdateResponse;
import com.website.loveconnect.dto.response.UserViewResponse;
import com.website.loveconnect.entity.Interest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface UserService {
    Page<ListUserResponse> getAllUser(int page, int size);
    UserViewResponse getUserById(int idUser);
    void blockUser(int idUser);
    void unblockUser(int idUser);
    UserUpdateResponse getUserUpdateById(int idUser);
    UserUpdateResponse updateUser(Integer idUser,UserUpdateRequest userRequest);
    void deleteUser(int idUser);
    void createUser(UserCreateRequest userRequest);
    Page<ListUserResponse> getAllUserByFilters(String status, String gender, String sortType, String keyword, int page, int size);
}
