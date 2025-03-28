package com.website.loveconnect.service;

import com.website.loveconnect.dto.request.PermissionRequest;
import com.website.loveconnect.dto.response.PermissionResponse;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

public interface PermissionService {
    void createPermission(PermissionRequest permissionRequest);
    void updatePermission(PermissionRequest permissionRequest);
    void deletePermission(String permissionName);
    List<PermissionResponse> getPermissions();

}
