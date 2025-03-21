package com.website.loveconnect.repository;

import com.website.loveconnect.entity.Interest;
import com.website.loveconnect.entity.UserInterest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserInterestRepository extends JpaRepository<UserInterest, Integer> {
    // Find interest has idInterest and idUser need find
    @Query(value = "SELECT ui.* FROM user_interests ui WHERE ui.user_id = :idUser AND ui.interest_id = :idInterest", nativeQuery = true)
    Optional<UserInterest> findUserInterestWithIdUserAndIdInterest(@Param("idInterest") int idInterest, @Param("idUser") int idUser);
}
