package com.website.loveconnect.service.impl;

import com.website.loveconnect.dto.request.PermissionRequest;
import com.website.loveconnect.dto.request.PermissionUpdateRequest;
import com.website.loveconnect.dto.response.PermissionGetResponse;
import com.website.loveconnect.dto.response.PermissionResponse;
import com.website.loveconnect.entity.Permission;
import com.website.loveconnect.exception.PermissionAlreadyExistException;
import com.website.loveconnect.exception.PermissionNotFoundException;
import com.website.loveconnect.exception.ResourceEmptyException;
import com.website.loveconnect.mapper.PermissionMapper;
import com.website.loveconnect.repository.PermissionRepository;
import com.website.loveconnect.service.PermissionService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
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
        //Không null có nghĩa là đã tồn tại loại permission này rồi
        if(permission != null ) {
            throw new PermissionAlreadyExistException("Permission already exist");
        }else {
            Permission permissionNew = new Permission();
            permissionNew.setPermissionName(permissionRequest.getPermissionName());
            permissionNew.setDescription(permissionRequest.getDescription());
            permissionNew.setCreated(new Timestamp(System.currentTimeMillis()));
            permissionRepository.save(permissionNew);
        }
    }

    @Override
    public void updatePermission(PermissionUpdateRequest permissionUpdateRequest) {
        try {
            Permission permission = permissionRepository.findById(permissionUpdateRequest.getPermissionId())
                    .orElseThrow(() -> new PermissionNotFoundException("Permission doesn't exist"));
            if (permission != null) {
                permission.setPermissionName(permissionUpdateRequest.getPermissionName());
                permission.setDescription(permissionUpdateRequest.getDescription());
                permission.setCreated(new Timestamp(System.currentTimeMillis()));
                permissionRepository.save(permission);
            }
        }catch (DataAccessException e){
            log.error("PermissionId not exist");
//            throw Exception("Permission doesn't exist",e);

        }
    }

    @Override
    public void deletePermission(String permissionName) {
        Permission permission = permissionRepository.findOneByPermissionName(permissionName)
                .orElseThrow(()-> new PermissionNotFoundException("Permission doesn't exist"));
        if(permission != null){
            permissionRepository.delete(permission);
        }
    }

    @Override
    public List<PermissionGetResponse> getPermissions() {
        List<Permission> permissions = permissionRepository.findAll();
        if(permissions.isEmpty()){
            throw new ResourceEmptyException("List permission is null");
        }
        //map dữ liệu từ list PermissionEntity qua list Dto
        return permissions.stream().map(permissionMapper::toPermissionResponse).toList();
    }


}
