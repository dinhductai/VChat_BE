package com.website.loveconnect.service;

import com.website.loveconnect.dto.request.UserCreateRequest;
import com.website.loveconnect.dto.request.UserUpdateRequest;
import com.website.loveconnect.dto.response.*;
import org.springframework.data.domain.Page;

public interface UserService {
    Page<ListUserResponse> getAllUser(int page, int size);
    UserViewResponse getUserById(int idUser);
    void blockUser(int idUser);
    void unblockUser(int idUser);
    UserUpdateResponse getUserUpdateById(int idUser);
    UserUpdateResponse updateUser(Integer idUser,UserUpdateRequest userRequest);
    void deleteUser(Integer idUser);
    void createUser(UserCreateRequest userRequest);
    Page<ListUserResponse> getAllUserByFilters(String status, String gender, String sortType, String keyword, int page, int size);
    Page<UserSearchResponse> getAllUserByKeyword(String keyword,int page,int size);
    Page<UserAndPhotosResponse> getAllUsersAndPhotos(int page,int size,Integer userId);
    Page<UserFriendResponse> getAllFriendsMatched(int page,int size,Integer userId);
    Page<UserFriendResponse> getFiendsFriendsMatched(int page,int size,Integer userId);
    Page<UserFriendResponse> getRandomFriends(int page,int size,Integer userId);
    Page<UserFriendResponse> getAllFriendsPending(int page,int size,Integer userId);

}
