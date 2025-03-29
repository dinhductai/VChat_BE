package com.website.loveconnect.mapper;

import com.website.loveconnect.dto.request.PermissionRequest;
import com.website.loveconnect.dto.response.PermissionGetResponse;
import com.website.loveconnect.dto.response.PermissionResponse;
import com.website.loveconnect.entity.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
//    Permission toPermission(PermissionRequest permissionRequest);
    PermissionGetResponse toPermissionResponse(Permission permission);
}
