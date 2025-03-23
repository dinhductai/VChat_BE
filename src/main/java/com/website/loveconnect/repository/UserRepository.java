package com.website.loveconnect.repository;

import com.website.loveconnect.entity.User;
import com.website.loveconnect.repository.custom.UserRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>, UserRepositoryCustom {
        // lấy tất cả user với tất cả trạng thái trừ deleted
        @Query(value = " select u.user_id, up.full_name, u.email, " +
                " u.phone_number, u.registration_date, u.account_status " +
                "from users u " +
                "join user_profiles up on up.user_id = u.user_id " +
                "where u.account_status <> 'DELETED' ", nativeQuery = true)
        //cần trả về tuple
        Page<Object[]> getAllUser (Pageable pageable);

        User getUserByEmail(String email);

}
