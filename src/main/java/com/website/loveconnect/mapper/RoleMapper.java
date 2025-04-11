package com.website.loveconnect.mapper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.website.loveconnect.dto.response.PermissionResponse;
import com.website.loveconnect.dto.response.RoleGetResponse;
import com.website.loveconnect.entity.Role;
import com.website.loveconnect.entity.User;
import com.website.loveconnect.entity.UserRole;
import com.website.loveconnect.enumpackage.RoleName;
import jakarta.persistence.Tuple;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class RoleMapper {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<RoleGetResponse> toRoleUserResponse(List<Tuple> listRoleUser) {

        List<RoleGetResponse> listRoleGetResponse = listRoleUser.stream()
                //để đơn giản có thể dùng vòng for
                .map(tuple -> {
                    //lấy dữ liệu JSON permissions từ Tuple, nếu null thì trả về "[]"
                    String permissionJson = tuple.get("permissions") != null ?
                            tuple.get("permissions").toString() : "[]";

                    return new RoleGetResponse(
                            RoleName.valueOf(tuple.get("roleName", String.class)),
                            tuple.get("roleDescription", String.class),
                            tuple.get("createTime", Timestamp.class),
                            toPermissionResponse(permissionJson)
                    );
                }).collect(Collectors.toList());
        return listRoleGetResponse;
    }

    //map dữ liệu từ mảng json từ query
    public List<PermissionResponse> toPermissionResponse(String permissionJson) {
        try {
            if (permissionJson == null || permissionJson.isEmpty()) {
                return Collections.emptyList(); // trả về danh sách rỗng nếu không có dữ liệu
            }
            //các thuộc tính trong dto phải giống alias từ query
            return objectMapper.readValue(permissionJson, new TypeReference<>() {});
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList(); // trả về danh sách rỗng nếu có lỗi
        }
    }

    public List<String> toListPermissionByRoleName(List<Tuple> listPermission) {
        List<String> listPermissionName = listPermission.stream()
                .map(tuple -> {
                   return tuple.get("permissionName", String.class);
                }).collect(Collectors.toList());
        return listPermissionName;
    }


}
