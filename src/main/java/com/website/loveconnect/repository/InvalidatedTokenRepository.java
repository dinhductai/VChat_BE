package com.website.loveconnect.repository;

import com.website.loveconnect.entity.InvalidatedToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvalidatedTokenRepository extends JpaRepository<InvalidatedToken, Integer> {
    Boolean existsByToken(String token);
}
