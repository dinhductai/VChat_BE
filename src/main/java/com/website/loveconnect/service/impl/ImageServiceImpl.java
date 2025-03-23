package com.website.loveconnect.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.website.loveconnect.entity.Photo;
import com.website.loveconnect.entity.User;
import com.website.loveconnect.exception.UserNotFoundException;
import com.website.loveconnect.repository.PhotoRepository;
import com.website.loveconnect.repository.UserRepository;
import com.website.loveconnect.service.ImageService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.IOException;
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

    //hàm lưu ảnh profile khi tạo người dùng mới và chưa được duyệt
    @Override
    public String saveImageProfile(MultipartFile file, String userEmail) throws IOException {
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
        String photoUrl = (String) uploadResult.get("url");
        User user = userRepository.getUserByEmail(userEmail);
        Photo photo = new Photo();
        photo.setPhotoUrl(photoUrl);
        photo.setIsProfilePicture(true);
        photo.setUploadDate(new Timestamp(System.currentTimeMillis()));
        photo.setIsApproved(false);
        photo.setOwnedPhoto(user);
        photoRepository.save(photo);
        return photoUrl;
    }
}
