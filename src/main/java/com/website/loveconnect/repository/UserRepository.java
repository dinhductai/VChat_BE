package com.website.loveconnect.repository;

import com.website.loveconnect.dto.response.UserResponse;
import com.website.loveconnect.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
        // lấy tất cả user với tất cả trạng thái
        @Query(value = " select u.user_id, up.full_name, u.email, " +
                " u.phone_number, u.registration_date, u.account_status " +
                "from users u " +
                "join user_profiles up on up.user_id = u.user_id " +
                " ", nativeQuery = true)
        Page<Object[]> getAllUser (Pageable pageable);

        // lấy thông tin chi tiết 1 user theo id
        @Query(value = "select u.user_id, p.photo_url, up.full_name, u.email, " +
                "up.gender, up.location, up.description, " +
                "GROUP_CONCAT(i.interest_name order by i.interest_name separator ', ') as interests " +
                ",u.registration_date, up.birthdate , u.phone_number , u.account_status " +
                "from users u " +
                "join user_profiles up on up.user_id = u.user_id " +
                "join photos p on p.user_id =  u.user_id " +
                "join user_interests ui on u.user_id = ui.user_id " +
                "join interests i on ui.interest_id = i.interest_id " +
                "where u.user_id = :idUser " +
                "group by u.user_id, p.photo_url, up.full_name, u.email, up.gender, up.location, " +
                "up.description, u.registration_date, up.birthdate , u.phone_number , u.account_status; ",
                nativeQuery = true)
        Object[] getUserById(@Param("idUser") Integer idUser);
}
