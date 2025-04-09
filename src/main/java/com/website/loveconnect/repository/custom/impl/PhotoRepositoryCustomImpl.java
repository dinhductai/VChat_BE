package com.website.loveconnect.repository.custom.impl;

import com.website.loveconnect.repository.custom.PhotoRepositoryCustom;
import jakarta.persistence.Tuple;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public class PhotoRepositoryCustomImpl implements PhotoRepositoryCustom {
    @Override
    public Page<Tuple> getAllPhoto(Pageable pageable) {
        return null;
    }
}
