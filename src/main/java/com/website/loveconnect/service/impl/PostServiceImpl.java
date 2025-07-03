package com.website.loveconnect.service.impl;

import com.website.loveconnect.dto.request.PostRequest;
import com.website.loveconnect.entity.Post;
import com.website.loveconnect.entity.User;
import com.website.loveconnect.entity.UserPost;
import com.website.loveconnect.exception.UserNotFoundException;
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
    UserPostRepository userPostRepository;
    @Override
    public void savePost(PostRequest postRequest) {
        try {
            Post post = Post.builder()
                    .content(postRequest.getContent())
                    .uploadDate(new Timestamp(System.currentTimeMillis()))
                    .isPublic(true)
                    .build();
            postRepository.save(post);
            User user = userRepository.getUserByEmail(postRequest.getUserEmail())
                    .orElseThrow(()->new UserNotFoundException("User Not Found"));
            UserPost userPost = UserPost.builder()
                    .post(post)
                    .user(user)
                    .upload(true)
                    .share(false)
                    .save(false)
                    .build();
            userPostRepository.save(userPost);
            postRequest.getListImage()
                    .parallelStream()
                    .forEach(photo -> {
                        try {
                            photoService.uploadImage(photo, postRequest.getUserEmail());
                        } catch (Exception e) {
                            log.error("Failed to upload image", e);
                        }
                    });
            postRequest.getListVideo()
                    .parallelStream()
                    .forEach(video -> {
                        try {
                            videoService.uploadVideo(video, postRequest.getUserEmail());
                        } catch (Exception e) {
                            log.error("Failed to upload video", e);
                        }
                    });
        }
        catch (Exception e) {

        }
    }
}
