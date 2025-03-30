package com.website.loveconnect.service;

import com.website.loveconnect.dto.response.RoleGetResponse;
import jakarta.persistence.Tuple;

import java.util.List;

public interface RoleService {
    List<RoleGetResponse> getAllRoleAndPermission();
    List<String> getAllPermissionByRoleName(String roleName);
}
