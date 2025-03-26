package com.website.loveconnect.repository;

import com.website.loveconnect.entity.User;
import com.website.loveconnect.repository.custom.UserRepositoryCustom;
import com.website.loveconnect.repository.custom.query.UserQueries;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>, UserRepositoryCustom {
        // lấy tất cả user với tất cả trạng thái trừ deleted
        @Query(value = UserQueries.GET_ALL_USER, nativeQuery = true)
        //cần trả về tuple
        Page<Object[]> getAllUser (Pageable pageable);

        Optional<User> getUserByEmail(String email);

        boolean existsByEmail(String email);

        @Query(value = UserQueries.EXIST_USER_BY_ROLE_ADMIN_AND_STATUS_ACTIVE,nativeQuery = true)
        Long existsByRoleAdminAndStatusActive();
}
