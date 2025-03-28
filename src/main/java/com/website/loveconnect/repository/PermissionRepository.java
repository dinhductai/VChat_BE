package com.website.loveconnect.repository;

import com.website.loveconnect.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PermissionRepository extends JpaRepository<Permission, Integer> {
    Optional<Permission> findOneByPermissionName(String namePermission);
}
