package com.website.loveconnect.service.impl;

import com.website.loveconnect.dto.request.PermissionRequest;
import com.website.loveconnect.dto.response.PermissionResponse;
import com.website.loveconnect.entity.Permission;
import com.website.loveconnect.mapper.PermissionMapper;
import com.website.loveconnect.repository.PermissionRepository;
import com.website.loveconnect.service.PermissionService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
public class PermissionServiceImpl implements PermissionService {
    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;

    @Override
    public void createPermission(PermissionRequest permissionRequest) {
        Permission permission = permissionRepository.findOneByPermissionName(permissionRequest.getPermissionName())
                .orElse(null);
        if(permission == null){
            Permission permissionNew = permissionMapper.toPermission(permissionRequest);
            permissionRepository.save(permissionNew);
        }
    }

    @Override
    public void updatePermission(PermissionRequest permissionRequest) {
        Permission permission = permissionRepository.findOneByPermissionName(permissionRequest.getPermissionName())
                .orElseThrow(()-> new RuntimeException("Permission doesn't exist"));
        if(permission != null){
            permission = permissionMapper.toPermission(permissionRequest);
            permissionRepository.save(permission);
        }
    }

    @Override
    public void deletePermission(String permissionName) {
        Permission permission = permissionRepository.findOneByPermissionName(permissionName)
                .orElseThrow(()-> new RuntimeException("Permission doesn't exist"));
        if(permission != null){
            permissionRepository.delete(permission);
        }
    }

    @Override
    public List<PermissionResponse> getPermissions() {
        return List.of();
    }
}
