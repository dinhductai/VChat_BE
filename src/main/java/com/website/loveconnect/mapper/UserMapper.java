package com.website.loveconnect.mapper;

import com.website.loveconnect.dto.request.ProfileDetailRequest;
import com.website.loveconnect.dto.request.UserCreateRequest;
import com.website.loveconnect.dto.request.UserUpdateRequest;
import com.website.loveconnect.dto.response.*;
import com.website.loveconnect.entity.User;
import com.website.loveconnect.enumpackage.AccountStatus;
import com.website.loveconnect.enumpackage.Gender;
import com.website.loveconnect.exception.PasswordIncorrectException;
import jakarta.persistence.Tuple;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.security.Timestamp;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

@Component
public class UserMapper {
    // mã hóa password với độ phức tạp 10
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

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
                .phoneNumber(userUpdateRequest.getPhoneNumber())
                .email(userUpdateRequest.getEmail())
                .gender(userUpdateRequest.getGender())
                .accountStatus(userUpdateRequest.getAccountStatus())
                .build();
    }

    public ListUserResponse toUserViewByFilters(Tuple tuple) {
        return ListUserResponse.builder()
                .userId(tuple.get("userId", Integer.class))
                .fullName(tuple.get("fullName", String.class))
                .email(tuple.get("email", String.class))
                .phone(tuple.get("phone", String.class))
                .registrationDate(tuple.get("registrationDate", java.sql.Timestamp.class))
                .accountStatus(tuple.get("accountStatus", String.class) != null ?
                        AccountStatus.valueOf(tuple.get("accountStatus", String.class)) : null)
                .build();
    }

    public User toCreateNewUser(UserCreateRequest userCreateRequest) {
        User newUser = new User();
        newUser.setEmail(userCreateRequest.getEmail());
        if(userCreateRequest.getPassword().equals(userCreateRequest.getPasswordConfirm())) {
            //mã hóa mật khẩu
            String passwordEncoded = passwordEncoder.encode(userCreateRequest.getPassword());
            newUser.setPassword(passwordEncoded);
        }else{
            throw new PasswordIncorrectException("Password does not match");
        }
        newUser.setPhoneNumber(userCreateRequest.getPhoneNumber());
        newUser.setAccountStatus(AccountStatus.ACTIVE);
        return newUser;
    }

    public User toUpdateUserEmailAndPhoneNumber(User user, ProfileDetailRequest profileDetailRequest) {
        user.setEmail(profileDetailRequest.getEmail());
        user.setPhoneNumber(profileDetailRequest.getPhoneNumber());
        return user;
    }

    public UserSearchResponse toProfileDetailResponse(Tuple tuple) {
        return UserSearchResponse.builder()
                .email(tuple.get("email", String.class))
                .phoneNumber(tuple.get("phoneNumber", String.class))
                .fullName(tuple.get("fullName", String.class))
                .birthDate(tuple.get("birthDate", Date.class))
                .gender(tuple.get("gender", String.class) != null
                        ? Gender.valueOf(tuple.get("gender", String.class).toUpperCase())
                        : null)
                .lookingFor(tuple.get("lookingFor", String.class) != null
                        ? Gender.valueOf(tuple.get("lookingFor", String.class).toUpperCase())
                        : null)
                .bio(tuple.get("bio", String.class))
                .height(tuple.get("height", Integer.class))
                .weight(tuple.get("weight", Integer.class))
                .location(tuple.get("location", String.class))
                .jobTitle(tuple.get("jobTitle", String.class))
                .company(tuple.get("company", String.class))
                .education(tuple.get("education", String.class))
                .description(tuple.get("description", String.class))
                .interestName(tuple.get("interestName", String.class) != null
                        ? Arrays.asList(tuple.get("interestName", String.class).split(","))
                        : Collections.emptyList())
                .build();
    }

    public UserAndPhotosResponse toUserAndPhotosResponse(Tuple tuple) {
        return UserAndPhotosResponse.builder()
                .userId(tuple.get("userId",Integer.class))
                .fullName(tuple.get("fullName", String.class))
                .location(tuple.get("location", String.class))
                .listPhotoUrl(tuple.get("photos",String.class) != null ?
                    Arrays.asList(tuple.get("photos",String.class).split(","))
                    : Collections.emptyList())
                .build();
    }

    public UserFriendResponse toUserFriendResponse(Tuple tuple){
        return UserFriendResponse.builder()
                .userId(tuple.get("userId", Integer.class))
                .fullName(tuple.get("fullName", String.class))
                .bio(tuple.get("bio", String.class))
                .phoneNumber(tuple.get("phoneNumber", String.class))
                .photoProfile(tuple.get("photoProfile", String.class))
                .build();
    }
}
