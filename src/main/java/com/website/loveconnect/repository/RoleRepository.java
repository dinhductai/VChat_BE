package com.website.loveconnect.repository;

import com.website.loveconnect.entity.Role;
import com.website.loveconnect.enumpackage.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByRoleName(RoleName name);
}
