package com.website.loveconnect.mapper;

import com.website.loveconnect.dto.request.ProfileDetailRequest;
import com.website.loveconnect.dto.request.UserCreateRequest;
import com.website.loveconnect.dto.request.UserUpdateRequest;
import com.website.loveconnect.dto.response.ProfileDetailResponse;
import com.website.loveconnect.entity.Interest;
import com.website.loveconnect.entity.User;
import com.website.loveconnect.entity.UserProfile;
import com.website.loveconnect.enumpackage.Gender;
import org.hibernate.annotations.Comment;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

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
    //map dữ liệu cho profile detail
    public ProfileDetailResponse toProfileDetailResponse(User user,UserProfile userProfile, List<Interest> listInterests) {
        ProfileDetailResponse profileDetailResponse = ProfileDetailResponse.builder()
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .fullName(userProfile.getFullName())
                .birthDate(userProfile.getBirthDate())
                .gender(userProfile.getGender())
                .lookingFor(userProfile.getLookingFor())
                .bio(userProfile.getBio())
                .height(userProfile.getHeight())
                .weight(userProfile.getWeight())
                .location(userProfile.getLocation())
                .jobTitle(userProfile.getJobTitle())
                .company(userProfile.getCompany())
                .education(userProfile.getEducation())
                .description(userProfile.getDescription())
                .interestName(listInterests.stream().map(Interest::getInterestName).collect(Collectors.toList()))
                .build();
        return profileDetailResponse;
    }

    //map dữ liệu cho profile detail
    public ProfileDetailResponse toUpdateProfileDetailResponse(User user, UserProfile userProfile, List<Interest> listInterests, ProfileDetailRequest profileDetailRequest) {
        ProfileDetailResponse profileDetailResponse = ProfileDetailResponse.builder()
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .fullName(userProfile.getFullName())
                .birthDate(userProfile.getBirthDate())
                .gender(userProfile.getGender())
                .lookingFor(userProfile.getLookingFor())
                .bio(userProfile.getBio())
                .height(userProfile.getHeight())
                .weight(userProfile.getWeight())
                .location(userProfile.getLocation())
                .jobTitle(userProfile.getJobTitle())
                .company(userProfile.getCompany())
                .education(userProfile.getEducation())
                .description(userProfile.getDescription())
                .interestName(listInterests.stream().map(Interest::getInterestName).collect(Collectors.toList()))
                .build();
        return profileDetailResponse;
    }

    public UserProfile toUpdateUserProfile(UserProfile userProfile, ProfileDetailRequest profileDetailRequest) {
            userProfile.setFullName(profileDetailRequest.getFullName());
            userProfile.setBirthDate(profileDetailRequest.getBirthDate());
            userProfile.setGender(profileDetailRequest.getGender());
            userProfile.setLookingFor(profileDetailRequest.getLookingFor());
            userProfile.setBio(profileDetailRequest.getBio());
            userProfile.setHeight(profileDetailRequest.getHeight());
            userProfile.setWeight(profileDetailRequest.getWeight());
            userProfile.setLocation(profileDetailRequest.getLocation());
            userProfile.setJobTitle(profileDetailRequest.getJobTitle());
            userProfile.setCompany(profileDetailRequest.getCompany());
            userProfile.setEducation(profileDetailRequest.getEducation());
            userProfile.setDescription(profileDetailRequest.getDescription());
        return userProfile;
    }
}
