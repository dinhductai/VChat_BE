package com.website.loveconnect.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
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
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.List;
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

    private static final String CLOUDINARY_BASE_URL = "http://res.cloudinary.com/dvgxke1mp/image/upload/";

//    @PreAuthorize("hasAuthority('ADMIN_UPLOAD_PHOTO')")
    //hàm lưu ảnh profile khi tạo người dùng mới và chưa được duyệt
    public String saveImage(MultipartFile file, String userEmail,boolean isProfilePicture) throws IOException {
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
        photo.setIsProfilePicture(isProfilePicture);
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

    @Override
    public String uploadImage(MultipartFile file, String userEmail) throws IOException {
        return saveImage(file,userEmail,false);
    }

    @Override
    public String uploadImageProfile(MultipartFile file, String userEmail) throws IOException {
        return saveImage(file,userEmail,true);
    }

    @Override
    public String getProfileImage(Integer idUser) {
        User user = userRepository.findById(idUser).orElseThrow(()->new UserNotFoundException("User not found"));
        String photoUrl = null;
        if(user!=null){
            Photo photoProfile = photoRepository
                    .findFirstByOwnedPhotoAndIsApprovedAndIsProfilePicture(user,true,true)
                    .orElseThrow(()->new UserNotFoundException("Photo not found"));
            photoUrl= photoProfile.getPhotoUrl();
        }
        return photoUrl;
    }

    @Override
    public List<String> getOwnedPhotos(Integer idUser) {
        boolean existingUser = userRepository.existsByUserId(idUser);
        if(existingUser){
            List<String> photosUrl = photoRepository.findAllOwnedPhoto(idUser);
            if(!photosUrl.isEmpty()){
                return photosUrl;
            }else
                return null;
        }
        else{
            throw new UserNotFoundException("User not found");
        }
    }

    @Override
    public void deleteImageProfile(Integer idUser, String urlImage) {
        try{
            boolean userExisting = userRepository.existsByUserId(idUser);
            Photo photo = photoRepository.findByPhotoUrl(urlImage)
                    .orElseThrow(()->new NoResultException("Photo not found"));
            if(userExisting) {
                String publicId = extractPublicId(photo.getPhotoUrl());
                //xóa ảnh bằng publicId
                Map deleteResult = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
                if(deleteResult.get("result").equals("ok")){
                    //cloud trả về ok thì xóa ở db
                    photoRepository.delete(photo);
                }
            }
        }catch (com.website.loveconnect.exception.DataAccessException da){
            log.error(da.getMessage());
            throw new com.website.loveconnect.exception.DataAccessException("Cannot access database");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String extractPublicId(String url) {
        if(url != null){
            //xóa loại ảnh ví dụ .png
            Integer lastDotIndex = url.lastIndexOf(".");
            if(lastDotIndex != -1){
                url = url.substring(0, lastDotIndex);
            }
            //loại bỏ phần trước,chỉ giữ lại public id
            Integer firstSlastIndex = url.lastIndexOf("/");
            if(firstSlastIndex != -1){
                url = url.substring(firstSlastIndex+1);
            }
            return url;
        }else return null;
    }


}
