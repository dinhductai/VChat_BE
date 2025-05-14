package com.website.loveconnect.repository;

import com.website.loveconnect.dto.response.UserNameAndProfileResponse;
import com.website.loveconnect.entity.UserProfile;
import com.website.loveconnect.repository.query.UseProfileQueries;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfile, Integer> {
    Optional<UserProfile> findByUser_UserId(Integer userId);
    Optional<UserProfile> findByFullName(String username);
    Optional<UserProfile> findFullNameByUser_UserId(Integer userId);
    @Query(value = UseProfileQueries.FIND_FULL_NAME_AND_PROFILE_PHOTOS_BY_USER_ID,nativeQuery = true)
    Optional<UserNameAndProfileResponse> findUserNameAndProfileByUserId(@Param("userId") Integer userId);
}
