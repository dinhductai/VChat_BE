package com.website.loveconnect.repository.custom.impl;

import com.website.loveconnect.repository.custom.UserRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public class UserRepositoryCustomImpl implements UserRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    //lấy một user bằng id
    @Override
    public Object[] getUserById(Integer idUser) {
        String query = "SELECT u.user_id, p.photo_url, up.full_name, u.email, up.gender, up.location, up.description, " +
                "GROUP_CONCAT(i.interest_name ORDER BY i.interest_name SEPARATOR ', ') AS interests, " +
                "u.registration_date, up.birthdate, u.phone_number, u.account_status, p.upload_date " +
                "FROM users u " +
                "JOIN user_profiles up ON up.user_id = u.user_id " +
                "JOIN photos p ON p.user_id = u.user_id " +
                "JOIN user_interests ui ON u.user_id = ui.user_id " +
                "JOIN interests i ON ui.interest_id = i.interest_id " +
                "WHERE u.user_id = :idUser AND p.is_profile_picture = 1 And p.is_approved = 1 " +
                "GROUP BY u.user_id, p.photo_url, up.full_name, u.email, up.gender, up.location, up.description, " +
                "u.registration_date, up.birthdate, u.phone_number, u.account_status, p.upload_date " +
                "ORDER BY p.upload_date DESC " +
                "LIMIT 1; ";
        Query nativeQuery = entityManager.createNativeQuery(query);
        nativeQuery.setParameter("idUser", idUser);
        return (Object[]) nativeQuery.getSingleResult();
    }
}
