package com.website.loveconnect.repository;

import com.website.loveconnect.entity.User;
import com.website.loveconnect.enumpackage.Gender;
import com.website.loveconnect.repository.custom.UserRepositoryCustom;
import com.website.loveconnect.repository.query.UserQueries;
import jakarta.persistence.Tuple;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>, UserRepositoryCustom {
        // lấy tất cả user với tất cả trạng thái trừ deleted
        @Query(value = UserQueries.GET_ALL_USER, nativeQuery = true)
        //cần trả về tuple
        Page<Object[]> getAllUser (Pageable pageable);

        Optional<User> getUserByEmail(String email);

        boolean existsByUserId(Integer userId);

        boolean existsByEmail(String email);

        @Query(value = UserQueries.EXIST_USER_BY_ROLE_ADMIN_AND_STATUS_ACTIVE,nativeQuery = true)
        Long existsByRoleAdminAndStatusActive();

        @Query(value = UserQueries.GET_ALL_USER_ROLE_BY_USERID,nativeQuery = true)
        List<String> getUserRoleByUserId(Integer idUser);

        @Query(value = UserQueries.GET_USER_BY_FILTERS,nativeQuery = true)
        Page<Tuple> getAllUserByFilters(@Param("status") String status, @Param("gender") String gender ,
                                         @Param("sort") String sort, @Param("keyword") String keyword,
                                         Pageable pageable);

        @Query(value = UserQueries.GET_ALL_USER_BY_KEYWORD,nativeQuery = true)
        Page<Tuple> getAllUserByKeyword(@Param("keyword") String keyword,Pageable pageable);

        @Query(value = UserQueries.GET_USER_AND_PHOTO_HOME_PAGE,nativeQuery = true)
        Page<Tuple> getAllUserAndPhotos(@Param("lookingFor") String lookingFor, Pageable pageable);

        @Query(value = UserQueries.GET_USER_FRIENDS,nativeQuery = true)
        Page<Tuple> getAllUserFriends(@Param("userId")Integer userId,Pageable pageable);

        @Query(value = UserQueries.GET_FRIENDS_MATCHED,nativeQuery = true)
        Page<Tuple> getAllFriendMatched(@Param("userId") Integer userId,Pageable pageable);

        @Query(value = UserQueries.GET_FRIENDS_FRIENDS,nativeQuery = true)
        Page<Tuple> getFriendsFriends(@Param("userId") Integer userId,Pageable pageable);

        @Query(value = UserQueries.GET_RANDOM_FRIENDS,nativeQuery = true)
        Page<Tuple> getRandomFriends(@Param("userId") Integer userId,Pageable pageable);

        @Query(value = UserQueries.GET_FRIENDS_PENDING,nativeQuery = true)
        Page<Tuple> getAllFriendPending(@Param("userId") Integer userId,Pageable pageable);
}
