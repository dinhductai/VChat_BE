package com.website.loveconnect.service;

import com.website.loveconnect.dto.response.PhotoStoryResponse;
import com.website.loveconnect.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.List;

public interface PhotoService {
    String uploadImage(MultipartFile file, String userEmail) throws IOException;
    String uploadImageProfile(MultipartFile file, String userEmail) throws IOException;
    String getProfileImage(Integer idUser);
    Page<String> getOwnedPhotos(Integer idUser,int page,int size);
    void deleteImageProfile(Integer idUser,String urlImage);
    String uploadPhotoForPost(MultipartFile file, String userEmail, Post post) throws IOException;
    Page<PhotoStoryResponse> photoStories(Integer userId, int page, int size);
    String uploadStory(MultipartFile file, String userEmail) throws IOException;
    List<PhotoStoryResponse> getOwnerStories(Integer userId);
}
