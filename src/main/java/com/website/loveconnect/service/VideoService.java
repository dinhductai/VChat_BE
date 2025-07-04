package com.website.loveconnect.service;

import com.website.loveconnect.entity.Post;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface VideoService {
    String uploadVideo(MultipartFile file, String userEmail) throws IOException;
    List<String> getOwnedVideos(Integer idUser);
    void deleteVideo(Integer idUser,String urlImage);
    String uploadVideoForPost(MultipartFile file, String userEmail, Post post) throws IOException;
}
