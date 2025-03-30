package com.website.loveconnect.service.impl;

import com.website.loveconnect.dto.response.RoleGetResponse;
import com.website.loveconnect.mapper.RoleMapper;
import com.website.loveconnect.repository.RoleRepository;
import com.website.loveconnect.service.RoleService;
import jakarta.persistence.Tuple;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
public class RoleServiceImpl implements RoleService {
    RoleRepository roleRepository;
    RoleMapper roleMapper;

    @Override
    public List<RoleGetResponse> getAllRoleAndPermission() {
        List<Tuple> listRolePermission = roleRepository.getALlRoleAndPermission();
        return roleMapper.toRoleUserResponse(listRolePermission);
    }

    @Override
    public List<String> getAllPermissionByRoleName(String roleName) {
        List<Tuple> listPermissionByRoleName = roleRepository.getPermissionByRoleName(roleName);
        return roleMapper.toListPermissionByRoleName(listPermissionByRoleName);
    }
}
