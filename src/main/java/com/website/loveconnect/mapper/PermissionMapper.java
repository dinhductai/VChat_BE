package com.website.loveconnect.mapper;

import com.website.loveconnect.dto.request.PermissionRequest;
import com.website.loveconnect.dto.response.PermissionGetResponse;
import com.website.loveconnect.dto.response.PermissionResponse;
import com.website.loveconnect.entity.Permission;
import org.mapstruct.Mapper;

// sử dụng mapstruct (một thư viện của java) giúp map object tự động
//thường thì chỉ dùng cho dto ko có thuộc tính nằm ngoài entity,tránh trường hợp thiếu data khi map entity vào dto
@Mapper(componentModel = "spring") // đánh dấu đây là 1 class mapper, mapstruct sẽ tự tạo ra implementation
// và spring có thể inject như một bean bởi vì componentModel = spring
public interface PermissionMapper {
//    Permission toPermission(PermissionRequest permissionRequest);
    PermissionGetResponse toPermissionResponse(Permission permission); //tự động map từ entity qua dto


}
