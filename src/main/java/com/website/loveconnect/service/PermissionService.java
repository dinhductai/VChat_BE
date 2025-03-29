package com.website.loveconnect.service;

import com.website.loveconnect.dto.request.PermissionRequest;
import com.website.loveconnect.dto.request.PermissionUpdateRequest;
import com.website.loveconnect.dto.response.PermissionGetResponse;
import com.website.loveconnect.dto.response.PermissionResponse;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

public interface PermissionService {
    void createPermission(PermissionRequest permissionRequest);
    void updatePermission(PermissionUpdateRequest permissionRequest);
    void deletePermission(String permissionName);
    List<PermissionGetResponse> getPermissions();

}
