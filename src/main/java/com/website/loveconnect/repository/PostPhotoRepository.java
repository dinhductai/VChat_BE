package com.website.loveconnect.repository;

import com.website.loveconnect.entity.PostPhoto;
import com.website.loveconnect.repository.query.PostQueries;
import jakarta.persistence.Tuple;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PostPhotoRepository extends JpaRepository<PostPhoto, Integer> {

}
