package com.website.loveconnect.mapper;

import com.website.loveconnect.dto.request.UserUpdateRequest;
import com.website.loveconnect.dto.response.UserUpdateResponse;
import com.website.loveconnect.dto.response.UserViewResponse;
import com.website.loveconnect.entity.User;
import com.website.loveconnect.enumpackage.AccountStatus;
import com.website.loveconnect.enumpackage.Gender;
import jakarta.persistence.Tuple;
import org.springframework.stereotype.Component;

import java.security.Timestamp;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

@Component
public class UserMapper {
    //map dữ liệu cho hàm lấy thông tin chi tiết 1 người dùng bằng id  getUserById
    public UserViewResponse toUserViewResponse(Tuple tuple) {
        String interests = tuple.get("interests", String.class);
        return UserViewResponse.builder()
                .userId(tuple.get("userId", Integer.class))
                .photoUrl(tuple.get("photoUrl", String.class))
                .fullName(tuple.get("fullName", String.class))
                .email(tuple.get("email", String.class))
                .gender(tuple.get("gender", String.class) != null ?
                        Gender.valueOf(tuple.get("gender", String.class)) : null)
                .location(tuple.get("location", String.class))
                .description(tuple.get("description", String.class))
                .interestName(interests != null ?
                        //tiến hành tách chuỗi
                        Arrays.asList(interests.split(", ")) : Collections.emptyList())
                .registrationDate(tuple.get("registrationDate", java.sql.Timestamp.class))
                .birthDate(tuple.get("birthDate", Date.class))
                .phoneNumber(tuple.get("phoneNumber", String.class))
                .accountStatus(tuple.get("accountStatus", String.class) != null ?
                        AccountStatus.valueOf(tuple.get("accountStatus", String.class)) : null)
                .build();
    }

    public UserUpdateResponse toUserUpdateResponse(Tuple tuple) {
        String interests = tuple.get("interests", String.class);
        return UserUpdateResponse.builder()
                .userId(tuple.get("userId", Integer.class))
                .fullName(tuple.get("fullName", String.class))
                .birthDate(tuple.get("birthDate", Date.class))
                .location(tuple.get("location", String.class))
                .description(tuple.get("description", String.class))
                .interestName(interests != null ?
                        //tiến hành tách chuỗi
                        Arrays.asList(interests.split(", ")) : Collections.emptyList())
                .photoUrl(tuple.get("photoUrl", String.class))
                .phoneNumber(tuple.get("phoneNumber", String.class))
                .email(tuple.get("email", String.class))
                .gender(tuple.get("gender",String.class)!=null?
                        Gender.valueOf(tuple.get("gender",String.class)) :null)
                .accountStatus(tuple.get("accountStatus", String.class) != null ?
                        AccountStatus.valueOf(tuple.get("accountStatus", String.class)) : null)
                .build();
    }

    public UserUpdateResponse toUserUpdateResponseBuilder(Integer idUser, UserUpdateRequest userUpdateRequest) {
        return UserUpdateResponse.builder()
                .userId(idUser)
                .fullName(userUpdateRequest.getFullName())
                .birthDate(userUpdateRequest.getBirthDate())
                .location(userUpdateRequest.getLocation())
                .description(userUpdateRequest.getDescription())
                .interestName(userUpdateRequest.getInterestName())
                .photoUrl(userUpdateRequest.getPhotoUrl())
                .phoneNumber(userUpdateRequest.getPhoneNumber())
                .email(userUpdateRequest.getEmail())
                .gender(userUpdateRequest.getGender())
                .accountStatus(userUpdateRequest.getAccountStatus())
                .build();
    }
}
