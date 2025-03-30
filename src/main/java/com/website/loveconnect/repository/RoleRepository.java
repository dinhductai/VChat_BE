package com.website.loveconnect.repository;

import com.website.loveconnect.entity.Role;
import com.website.loveconnect.enumpackage.RoleName;
import com.website.loveconnect.repository.custom.query.RoleQueries;
import jakarta.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByRoleName(RoleName name);

    @Query(value = RoleQueries.GET_ROLE_AND_PERMISSIONS,nativeQuery = true)
    List<Tuple> getALlRoleAndPermission();
}
