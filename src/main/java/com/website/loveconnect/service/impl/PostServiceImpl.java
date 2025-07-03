package com.website.loveconnect.service.impl;

import com.website.loveconnect.dto.request.PostRequest;
import com.website.loveconnect.entity.Post;
import com.website.loveconnect.repository.*;
import com.website.loveconnect.service.PhotoService;
import com.website.loveconnect.service.PostService;
import com.website.loveconnect.service.VideoService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
public class PostServiceImpl implements PostService {

    PostRepository postRepository;
    PhotoRepository photoRepository;
    VideoRepository videoRepository;
    UserRepository userRepository;
    UserProfileRepository userProfileRepository;
    PhotoService photoService;
    VideoService videoService;

    @Override
    public void savePost(PostRequest postRequest) {
        try {
            Post post = Post.builder()
                    .content(postRequest.getContent())
                    .uploadDate(new Timestamp(System.currentTimeMillis()))
                    .isPublic(true)
                    .build();
            postRepository.save(post);
            for (MultipartFile photo : postRequest.getListImage()) {
                photoService.uploadImage(photo, postRequest.getUserEmail());
            }
            for (MultipartFile video : postRequest.getListVideo()) {
                videoService.uploadVideo(video, postRequest.getUserEmail());
            }
        }
        catch (Exception e) {

        }
    }
}
