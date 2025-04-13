package com.website.loveconnect.service;

import com.website.loveconnect.dto.request.ProfileDetailRequest;
import com.website.loveconnect.dto.response.ProfileDetailResponse;

public interface UserProfileService {
    ProfileDetailResponse getProfileDetail(Integer idUser);
    void updateProfileDetail(Integer idUser,ProfileDetailRequest profileDetailRequest);
}
