package com.website.loveconnect.controller.admin;

import com.website.loveconnect.dto.request.PermissionRequest;
import com.website.loveconnect.entity.Permission;
import com.website.loveconnect.service.PermissionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping(value = "/permission/update")
    public void updatePermission(@RequestBody PermissionRequest permissionRequest) {
        permissionService.updatePermission(permissionRequest);
    }

    @DeleteMapping(value = "/permission/del/{permissionName}")
    public void deletePermission(@PathVariable("permissionName") String permissionName) {
        permissionService.deletePermission(permissionName);
    }
}
