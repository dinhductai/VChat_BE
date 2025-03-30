package com.website.loveconnect.controller.admin;

import com.website.loveconnect.dto.request.PermissionAttachRequest;
import com.website.loveconnect.dto.request.PermissionRequest;
import com.website.loveconnect.dto.request.PermissionUpdateRequest;
import com.website.loveconnect.dto.response.ApiResponse;
import com.website.loveconnect.dto.response.PermissionGetResponse;
import com.website.loveconnect.entity.Permission;
import com.website.loveconnect.service.PermissionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminPermissionController {
    PermissionService permissionService;

    @PostMapping(value = "/permissions/create")
    public ResponseEntity<ApiResponse<String>> createPermission(@RequestBody PermissionRequest permissionRequest) {
        permissionService.createPermission(permissionRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true,"Create permission successful",null));
    }

    @PutMapping(value = "/permissions/update")
    public ResponseEntity<ApiResponse<String>> updatePermission(@RequestBody PermissionUpdateRequest permissionRequest) {
        permissionService.updatePermission(permissionRequest);
        return ResponseEntity.ok(new ApiResponse<>(true,"Update permission successful",null));
    }

    @DeleteMapping(value = "/permissions/del/{permissionName}")
    public ResponseEntity<ApiResponse<String>> deletePermission(@PathVariable("permissionName") String permissionName) {
        permissionService.deletePermission(permissionName);
        return ResponseEntity.ok(new ApiResponse<>(true,"Delete permission successful",null));
    }

    @GetMapping(value = "/permissions")
    public ResponseEntity<ApiResponse<List<PermissionGetResponse>>> getPermissions() {
        List<PermissionGetResponse> listPermission = permissionService.getPermissions();
        return ResponseEntity.ok(new ApiResponse<>(true,"Get list permission successful",listPermission));

    }

    @PostMapping(value = "/permissions/attach")
    public ResponseEntity<ApiResponse<String>> attachPermission(@RequestBody PermissionAttachRequest permissionAttachRequest) {
        permissionService.attachPermission(permissionAttachRequest);
        return ResponseEntity.ok(new ApiResponse<>(true,"Attach permission successful",null));
    }

}
