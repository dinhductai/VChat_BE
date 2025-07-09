package com.website.loveconnect.repository;

import com.website.loveconnect.entity.Reaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReactionRepository extends JpaRepository<Reaction, Integer> {
}
