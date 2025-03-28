package com.website.loveconnect.controller.admin;

import com.website.loveconnect.dto.request.PermissionRequest;
import com.website.loveconnect.entity.Permission;
import com.website.loveconnect.service.PermissionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminPermissionController {
    PermissionService permissionService;

    @PostMapping(value = "/permission/create")
    public void createPermission(@RequestBody PermissionRequest permissionRequest) {
        permissionService.createPermission(permissionRequest);
    }
}
