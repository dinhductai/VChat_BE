package com.website.loveconnect.repository.custom.impl;

import com.website.loveconnect.repository.custom.UserRepositoryCustom;
import com.website.loveconnect.repository.query.UserQueries;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.Tuple;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public class UserRepositoryCustomImpl implements UserRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;
    private UserQueries userQueries;


    //lấy một user bằng id
    @Override
    public Tuple getUserById(Integer idUser) {
        Query nativeQuery = entityManager.createNativeQuery(UserQueries.GET_USER_BY_ID, Tuple.class);
        nativeQuery.setParameter("idUser", idUser);
        return (Tuple) nativeQuery.getSingleResult();
    }

    @Override
    public Tuple getUserForUpdateById(Integer idUser) {
        Query nativeQuery = entityManager.createNativeQuery(
                UserQueries.GET_USER_FOR_UPDATE_BY_ID, Tuple.class);
        nativeQuery.setParameter("idUser", idUser);
        return (Tuple) nativeQuery.getSingleResult();
    }


}
