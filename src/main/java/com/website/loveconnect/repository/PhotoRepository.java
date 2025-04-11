package com.website.loveconnect.repository;

import com.website.loveconnect.entity.Photo;
import com.website.loveconnect.entity.User;
import com.website.loveconnect.repository.query.PhotoQueries;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PhotoRepository extends JpaRepository<Photo, Integer> {

    @Query(value = "SELECT p.* FROM photos p WHERE p.user_id = :userId " +
            "AND p.is_profile_picture = 1 AND p.is_approved = 1 ORDER BY p.upload_date DESC " +
            "LIMIT 1 ",nativeQuery = true)
    Optional<Photo> findOneByUserId(@Param("userId") Integer userId);

    Optional<Photo> findFirstByOwnedPhotoAndIsApprovedAndIsProfilePicture(User user, Boolean isApproved, Boolean isProfilePicture);

    @Query(value = PhotoQueries.FIND_OWNED_PHOTO,nativeQuery = true)
    List<String> findAllOwnedPhoto(@Param("idUser") Integer idUser);

}
