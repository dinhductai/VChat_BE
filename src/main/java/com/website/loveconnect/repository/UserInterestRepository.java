package com.website.loveconnect.repository;

import com.website.loveconnect.entity.Interest;
import com.website.loveconnect.entity.UserInterest;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@Transactional
public interface UserInterestRepository extends JpaRepository<UserInterest, Integer> {
    // Find interest has idInterest and idUser need find
    @Query(value = "SELECT ui.* FROM user_interests ui WHERE ui.user_id = :idUser AND ui.interest_id = :idInterest", nativeQuery = true)
    UserInterest findUserInterestWithIdUserAndIdInterest(@Param("idInterest") int idInterest, @Param("idUser") int idUser);

    @Modifying
    @Query(value = "INSERT INTO user_interests (user_id, interest_id) " +
            "SELECT :userId, interest_id FROM interests WHERE interest_id IN :interestIds " +
            "AND NOT EXISTS (SELECT 1 FROM user_interests WHERE user_id = :userId AND interest_id = interests.interest_id)",
            nativeQuery = true)
    void insertUserInterestNotExist(@Param("userId") Integer userId, @Param("interestIds") List<Integer> interestIds);
}
