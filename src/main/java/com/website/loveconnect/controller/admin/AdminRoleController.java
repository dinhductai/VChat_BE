package com.website.loveconnect.controller.admin;

import com.website.loveconnect.dto.response.ApiResponse;
import com.website.loveconnect.dto.response.RoleGetResponse;
import com.website.loveconnect.service.RoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AdminRoleController {
    RoleService roleService;

    //lấy ra danh sách role
    @GetMapping(value = "/roles")
    public ResponseEntity<ApiResponse<List<RoleGetResponse>>> getAllRole() {
        List<RoleGetResponse> roleGetResponseList =  roleService.getAllRoleAndPermission();
        return ResponseEntity.ok(new ApiResponse<>(true,"Get list role successful", roleGetResponseList));
    }
    //lấy danh sách role cùng với các permission đc thuộc role
    @GetMapping(value = "/roles/permissions")
    public ResponseEntity<ApiResponse<List<String>>> getAllRolePermission(@RequestParam String roleName) {
        List<String> listRolePermission = roleService.getAllPermissionByRoleName(roleName);
        return ResponseEntity.ok(new ApiResponse<>(true,"Get list and permission successful", listRolePermission));
    }

}
