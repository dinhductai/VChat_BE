package com.website.loveconnect.service;

import com.website.loveconnect.dto.response.RoleGetResponse;

import java.util.List;

public interface RoleService {
    List<RoleGetResponse> getAllRoleAndPermission();
}
