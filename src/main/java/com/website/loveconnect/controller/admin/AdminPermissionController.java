package com.website.loveconnect.controller.admin;

import com.website.loveconnect.dto.request.PermissionRequest;
import com.website.loveconnect.dto.request.PermissionUpdateRequest;
import com.website.loveconnect.dto.response.PermissionGetResponse;
import com.website.loveconnect.entity.Permission;
import com.website.loveconnect.service.PermissionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminPermissionController {
    PermissionService permissionService;

    @PostMapping(value = "/permissions/create")
    public void createPermission(@RequestBody PermissionRequest permissionRequest) {
        permissionService.createPermission(permissionRequest);
    }

    @PutMapping(value = "/permissions/update")
    public void updatePermission(@RequestBody PermissionUpdateRequest permissionRequest) {
        permissionService.updatePermission(permissionRequest);
    }

    @DeleteMapping(value = "/permissions/del/{permissionName}")
    public void deletePermission(@PathVariable("permissionName") String permissionName) {
        permissionService.deletePermission(permissionName);
    }

    @GetMapping(value = "/permissions")
    public List<PermissionGetResponse> getPermissions() {
        return permissionService.getPermissions();
    }

}
