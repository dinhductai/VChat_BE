package com.website.loveconnect.repository;

import com.website.loveconnect.entity.Photo;
import com.website.loveconnect.entity.User;
import com.website.loveconnect.repository.query.PhotoQueries;
import jakarta.persistence.Tuple;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PhotoRepository extends JpaRepository<Photo, Integer> {

    @Query(value = PhotoQueries.FIND_OWNED_PHOTO,nativeQuery = true)
    Optional<Photo> findOneByUserId(@Param("userId") Integer userId);

    Optional<Photo> findFirstByOwnedPhotoAndIsApprovedAndIsProfilePicture(User user, Boolean isApproved, Boolean isProfilePicture);

    @Query(value = PhotoQueries.FIND_OWNED_PHOTO,nativeQuery = true)
    Page<String> findAllOwnedPhoto(@Param("idUser") Integer idUser,Pageable pageable);

    Optional<Photo> findByPhotoUrl(String photoUrl);

    @Query(value = PhotoQueries.GET_ALL_STORY_PHOTOS,nativeQuery = true)
    Page<Tuple> findAllStoryPhotos(@Param("userId") Integer userId,Pageable pageable);

    @Query(value = PhotoQueries.GET_OWNER_STORIES,nativeQuery = true)
    List<Tuple> findOwnerStories(@Param("userId") Integer userId);

}
