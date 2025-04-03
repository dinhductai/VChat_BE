package com.website.loveconnect.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.fasterxml.jackson.databind.DatabindException;
import com.website.loveconnect.entity.Photo;
import com.website.loveconnect.entity.User;
import com.website.loveconnect.exception.UserNotFoundException;
import com.website.loveconnect.repository.PhotoRepository;
import com.website.loveconnect.repository.UserRepository;
import com.website.loveconnect.service.ImageService;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import java.awt.*;
import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.Map;


@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
public class ImageServiceImpl implements ImageService {

    Cloudinary cloudinary;
    UserRepository userRepository;
    PhotoRepository photoRepository;


    @PreAuthorize("hasAuthority('ADMIN_UPLOAD_PHOTO')")
    //hàm lưu ảnh profile khi tạo người dùng mới và chưa được duyệt
    @Override
    public String saveImageProfile(MultipartFile file, String userEmail) throws IOException {

            if (file == null || file.isEmpty()) {
                log.warn("Attempt to upload empty file for user: {}", userEmail);
                throw new IllegalArgumentException("Photo cannot be blank");
            }
            if (StringUtils.isEmpty(userEmail)) { //bắt validation email
                throw new IllegalArgumentException("User email cannot be blank");
            }
            Map uploadResult;
            try {
                uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            } catch (IOException ioe) {
                log.error("Failed to upload image to Cloudinary", ioe.getMessage());
                throw ioe;
            }
            String photoUrl = (String) uploadResult.get("url");

            User user = userRepository.getUserByEmail(userEmail)
                    .orElseThrow(()->new UserNotFoundException("User not found"));
            if (user == null) {
                throw new UserNotFoundException("User not found");
            }
            Photo photo = new Photo();
            photo.setPhotoUrl(photoUrl);
            photo.setIsProfilePicture(true);
            photo.setUploadDate(new Timestamp(System.currentTimeMillis()));
            photo.setIsApproved(false);
            photo.setOwnedPhoto(user);
            try {
                photoRepository.save(photo);
                log.info("Saved image profile successfully");
            } catch (DataAccessException dae) {
                log.error("Failed to save image profile", dae.getMessage());
                throw new DataAccessException("Failed to save photo to database", dae) {
                };
            }
            return photoUrl;

    }
}
