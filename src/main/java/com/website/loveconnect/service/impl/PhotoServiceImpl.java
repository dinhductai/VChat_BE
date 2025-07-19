package com.website.loveconnect.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.website.loveconnect.dto.response.PhotoStoryResponse;
import com.website.loveconnect.entity.Photo;
import com.website.loveconnect.entity.Post;
import com.website.loveconnect.entity.PostPhoto;
import com.website.loveconnect.entity.User;
import com.website.loveconnect.exception.DataAccessException;
import com.website.loveconnect.exception.UserNotFoundException;
import com.website.loveconnect.mapper.PhotoMapper;
import com.website.loveconnect.repository.PhotoRepository;
import com.website.loveconnect.repository.PostPhotoRepository;
import com.website.loveconnect.repository.UserRepository;
import com.website.loveconnect.service.PhotoService;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;


@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
public class PhotoServiceImpl implements PhotoService {

    Cloudinary cloudinary;
    UserRepository userRepository;
    PhotoMapper photoMapper;
    PhotoRepository photoRepository;
    PostPhotoRepository postPhotoRepository;
    private static final String CLOUDINARY_BASE_URL = "http://res.cloudinary.com/dvgxke1mp/image/upload/";

//    @PreAuthorize("hasAuthority('ADMIN_UPLOAD_PHOTO')")
    //hàm lưu ảnh profile khi tạo người dùng mới
public String saveImage(MultipartFile file, String userEmail, boolean isProfilePicture, Post post,boolean isStory) throws IOException {
    if (file == null || file.isEmpty()) {
        log.warn("Attempt to upload empty file for user: {}", userEmail);
        throw new IllegalArgumentException("Photo cannot be null or empty");
    }
    if (StringUtils.isEmpty(userEmail)) {
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
            .orElseThrow(() -> new UserNotFoundException("User not found"));

    Photo photo = new Photo();
    photo.setPhotoUrl(photoUrl);
    photo.setIsProfilePicture(isProfilePicture);
    photo.setUploadDate(new Timestamp(System.currentTimeMillis()));
    photo.setIsApproved(true);
    photo.setIsStory(isStory);
    photo.setOwnedPhoto(user);

    // Lưu Photo trước
    try {
        photoRepository.save(photo);
        log.info("Saved image profile successfully");
    } catch (DataAccessException dae) {
        log.error("Failed to save image profile", dae.getMessage());
        throw new DataAccessException("Failed to save photo to database") {};
    }

    // Sau đó lưu PostPhoto nếu post không null
    if (post != null) {
        PostPhoto postPhoto = PostPhoto.builder()
                .photo(photo)
                .post(post)
                .build();
        postPhotoRepository.save(postPhoto);
    }

    return photoUrl;
}

    @Override
    public String uploadImage(MultipartFile file, String userEmail) throws IOException {
        return saveImage(file,userEmail,false,null,false);
    }

    @Override
    public String uploadImageProfile(MultipartFile file, String userEmail) throws IOException {
        return saveImage(file,userEmail,true,null,false);
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
    public Page<String> getOwnedPhotos(Integer idUser,int page,int size) {
        boolean existingUser = userRepository.existsByUserId(idUser);
        Pageable pageable = PageRequest.of(page,size);
        if(existingUser){
            Page<String> photosUrl = photoRepository.findAllOwnedPhoto(idUser,pageable);
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

    @Override
    public String uploadPhotoForPost(MultipartFile file, String userEmail, Post post) throws IOException {
        return saveImage(file,userEmail,false,post,false);
    }

    @Override
    public Page<PhotoStoryResponse> photoStories(Integer userId, int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            return photoRepository.findAllStoryPhotos(userId, pageable).map(photoMapper::toPhotoStoryResponseList);
        }catch (DataAccessException da){
            log.error(da.getMessage());
            throw new DataAccessException("Cannot access data");
        }
    }

    @Override
    public String uploadStory(MultipartFile file, String userEmail) throws IOException {
        return saveImage(file,userEmail,false,null,true);
    }

    @Override
    public List<PhotoStoryResponse> getOwnerStories(Integer userId) {
        try{
            return photoRepository.findOwnerStories(userId).stream().map(photoMapper::toPhotoStoryResponseList).toList();
        }catch (DataAccessException da){
            log.error(da.getMessage());
            throw new DataAccessException("Cannot access data");
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
