package com.website.loveconnect.repository;

import com.website.loveconnect.entity.Interest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InterestRepository extends JpaRepository<Interest, Integer> {
    // Get All Interest
    @Query(value = "SELECT i.* FROM interests i JOIN user_interests ui ON ui.interest_id = i.interest_id WHERE ui.user_id = :idUser", nativeQuery = true)
    List<Interest> getAllInterest(@Param("idUser") int idUser);
}
