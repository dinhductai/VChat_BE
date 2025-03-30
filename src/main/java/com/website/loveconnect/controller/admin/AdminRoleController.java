package com.website.loveconnect.controller.admin;

import com.website.loveconnect.dto.response.RoleGetResponse;
import com.website.loveconnect.service.RoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
//@CrossOrigin(origins = "http://127.0.0.1:5500")  chi ap dung local
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AdminRoleController {
    RoleService roleService;

    @GetMapping(value = "/roles")
    public List<RoleGetResponse> getAllRole() {
        return roleService.getAllRoleAndPermission();
    }

    @GetMapping(value = "/roles/permissions")
    public List<String> getAllRolePermission(@RequestParam String roleName) {
        return roleService.getAllPermissionByRoleName(roleName);
    }

}
