package com.website.loveconnect.mapper;

import com.website.loveconnect.dto.request.UserCreateRequest;
import com.website.loveconnect.entity.User;
import com.website.loveconnect.entity.UserProfile;
import com.website.loveconnect.enumpackage.Gender;
import org.hibernate.annotations.Comment;
import org.springframework.stereotype.Component;

@Component
public class UserProfileMapper {

    //hàm map dữ liệu để tạo một user account mới
    public UserProfile toCreateNewUserProfile(UserCreateRequest userCreateRequest, User newUser) {
        UserProfile newUserProfile = new UserProfile();
        newUserProfile.setFullName(userCreateRequest.getFullName());
        newUserProfile.setBirthDate(userCreateRequest.getBirthDate());
        newUserProfile.setGender(userCreateRequest.getGender());
        newUserProfile.setLocation(userCreateRequest.getLocation());
        newUserProfile.setDescription(userCreateRequest.getDescription());
        newUserProfile.setLookingFor(Gender.FEMALE); //tạm thời set cứng
        newUserProfile.setUser(newUser);
        return newUserProfile;
    }
}
