package com.website.loveconnect.repository.custom;

import jakarta.persistence.Tuple;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PhotoRepositoryCustom {
    Page<Tuple> getAllPhoto(Pageable pageable);
}
