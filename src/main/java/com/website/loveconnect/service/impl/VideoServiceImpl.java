package com.website.loveconnect.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.website.loveconnect.entity.*;
import com.website.loveconnect.exception.UserNotFoundException;
import com.website.loveconnect.exception.VideoNotFoundException;
import com.website.loveconnect.repository.PhotoRepository;
import com.website.loveconnect.repository.PostVideoRepository;
import com.website.loveconnect.repository.UserRepository;
import com.website.loveconnect.repository.VideoRepository;
import com.website.loveconnect.service.VideoService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
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
public class VideoServiceImpl implements VideoService {
    Cloudinary cloudinary;
    UserRepository userRepository;
    VideoRepository videoRepository;
    PostVideoRepository postVideoRepository;

    private String saveVideo(MultipartFile file, String userEmail, Post post) throws IOException {
        if (file == null || file.isEmpty()) {
            log.warn("Attempt to upload empty file for user: {}", userEmail);
            throw new IllegalArgumentException("Video cannot be null or empty");
        }
        if (StringUtils.isEmpty(userEmail)) {
            throw new IllegalArgumentException("User email cannot be blank");
        }
        if (post != null && post.getPostId() == null) {
            log.error("Post has null ID for user: {}", userEmail);
            throw new IllegalArgumentException("Post must be saved before associating with a video");
        }

        Map uploadResult;
        try {
            uploadResult = cloudinary.uploader()
                    .upload(file.getBytes(), ObjectUtils.asMap("resource_type", "video"));
        } catch (IOException ioe) {
            log.error("Failed to upload video to Cloudinary", ioe.getMessage());
            throw ioe;
        }
        String videoUrl = (String) uploadResult.get("url");

        User user = userRepository.getUserByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Video video = new Video();
        video.setVideoUrl(videoUrl);
        video.setUploadDate(new Timestamp(System.currentTimeMillis()));
        video.setIsApproved(true);
        video.setIsStory(false);
        video.setOwnedVideo(user);

        try {
            videoRepository.save(video);
            log.info("Saved video successfully for user: {}", userEmail);
        } catch (DataAccessException dae) {
            log.error("Failed to save video to database", dae.getMessage());
            throw new DataAccessException("Failed to save video to database", dae) {};
        }

        if (post != null) {
            PostVideo postVideo = PostVideo.builder()
                    .video(video)
                    .post(post)
                    .build();
            postVideoRepository.save(postVideo);
            log.info("Saved PostVideo for post ID: {}", post.getPostId());
        }

        return videoUrl;
    }

    @Override
    public String uploadVideo(MultipartFile file, String userEmail) throws IOException {
       return saveVideo(file,userEmail,null);
    }

    @Override
    public Page<Video> getOwnedVideos(Integer idUser,int page,int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            User user = userRepository.findById(idUser).orElseThrow(() -> new UserNotFoundException("User not found"));
            return videoRepository.findByOwnedVideoAndIsStory(user, false, pageable);
        }catch (DataAccessException e){
            throw new DataAccessException("Failed to save video to database", e) {};
        }
    }

    @Override
    public void deleteVideo(Integer idUser, String urlVideo) {
        try {
            boolean userExisting = userRepository.existsByUserId(idUser);
            Video video = videoRepository.findByVideoUrl(urlVideo)
                    .orElseThrow(() -> new VideoNotFoundException("Video not found with url: " + urlVideo));

            if (userExisting) {
                String publicId = extractPublicId(video.getVideoUrl());
                Map options = ObjectUtils.asMap("resource_type", "video");
                Map deleteResult = cloudinary.uploader().destroy(publicId, options);
                if (deleteResult.get("result").equals("ok")) {
                    videoRepository.delete(video);
                }
            }
        } catch (com.website.loveconnect.exception.DataAccessException da) {
            log.error(da.getMessage());
            throw new com.website.loveconnect.exception.DataAccessException("Cannot access database");
        } catch (IOException e) {
            log.error("Cloudinary delete failed: " + e.getMessage());
            throw new RuntimeException("Error while deleting video from cloud", e);
        }
    }

    @Override
    public String uploadVideoForPost(MultipartFile file, String userEmail, Post post) throws IOException {
        return saveVideo(file, userEmail,post);
    }

    private String extractPublicId(String url) {
        if(url != null){
            //xóa loại ảnh ví dụ .mp4
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
