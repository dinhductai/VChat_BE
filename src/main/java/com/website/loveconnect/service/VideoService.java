package com.website.loveconnect.service;

import com.website.loveconnect.entity.Post;
import com.website.loveconnect.entity.Video;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface VideoService {
    String uploadVideo(MultipartFile file, String userEmail) throws IOException;
    Page<Video> getOwnedVideos(Integer idUser,int page,int size);
    void deleteVideo(Integer idUser,String urlVideo);
    String uploadVideoForPost(MultipartFile file, String userEmail, Post post) throws IOException;
}
