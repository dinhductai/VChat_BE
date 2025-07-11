package com.website.loveconnect.service.impl;

import com.cloudinary.utils.StringUtils;
import com.website.loveconnect.dto.request.PostRequest;
import com.website.loveconnect.dto.response.PostResponse;
import com.website.loveconnect.entity.*;
import com.website.loveconnect.enumpackage.PostStatus;
import com.website.loveconnect.exception.DataAccessException;
import com.website.loveconnect.exception.PostNotFoundException;
import com.website.loveconnect.exception.UserNotFoundException;
import com.website.loveconnect.mapper.PostMapper;
import com.website.loveconnect.repository.*;
import com.website.loveconnect.service.PhotoService;
import com.website.loveconnect.service.PostService;
import com.website.loveconnect.service.VideoService;
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

import java.sql.Timestamp;
import java.util.List;

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
    PostVideoRepository postVideoRepository;
    PostPhotoRepository postPhotoRepository;
    PostMapper postMapper;
    @Override
    public PostResponse savePost(PostRequest postRequest) {
        try {
            // Kiểm tra dữ liệu đầu vào
            if (postRequest == null || StringUtils.isEmpty(postRequest.getUserEmail())) {
                throw new IllegalArgumentException("PostRequest or userEmail cannot be null");
            }

            // Tạo và lưu Post trước
            Post post = Post.builder()
                    .content(postRequest.getContent())
                    .uploadDate(new Timestamp(System.currentTimeMillis()))
                    .isPublic(true)
                    .isApproved(true)
                    .status(PostStatus.ACTIVE)
                    .build();
            post = postRepository.save(post); // Lưu Post để có ID
            log.info("Saved post with ID: {}", post.getId());

            // Lưu UserPost
            User user = userRepository.getUserByEmail(postRequest.getUserEmail())
                    .orElseThrow(() -> new UserNotFoundException("User not found"));
            UserPost userPost = UserPost.builder()
                    .post(post)
                    .user(user)
                    .upload(true)
                    .share(false)
                    .save(false)
                    .build();
            userPostRepository.save(userPost);
            log.info("Saved UserPost for user: {} and post ID: {}", postRequest.getUserEmail(), post.getId());

            // Xử lý ảnh
            List<MultipartFile> imageFiles = postRequest.getListImage();
            if (imageFiles != null && !imageFiles.isEmpty()) {
                for (MultipartFile photo : imageFiles) {
                    try {
                        photoService.uploadPhotoForPost(photo, postRequest.getUserEmail(), post);
                        log.info("Uploaded photo for post ID: {}", post.getId());
                    } catch (Exception e) {
                        log.error("Failed to upload photo for post ID: {}", post.getId(), e);
                        throw new RuntimeException("Failed to upload photo", e);
                    }
                }
            }

            // Xử lý video
            List<MultipartFile> videoFiles = postRequest.getListVideo();
            if (videoFiles != null && !videoFiles.isEmpty()) {
                for (MultipartFile video : videoFiles) {
                    try {
                        videoService.uploadVideoForPost(video, postRequest.getUserEmail(), post);
                        log.info("Uploaded video for post ID: {}", post.getId());
                    } catch (Exception e) {
                        log.error("Failed to upload video for post ID: {}", post.getId(), e);
                        throw new RuntimeException("Failed to upload video", e);
                    }
                }
            }
            return postMapper.toPostResponse(postRepository.getOnePostByPostId(post.getId()));
        } catch (Exception e) {
            log.error("Failed to save post for user: {}", postRequest.getUserEmail(), e);
            throw new RuntimeException("Failed to save post", e);
        }
    }

    @Override
    public Page<PostResponse> getRandom(int page, int size) {
        Pageable pageable = PageRequest.of(page,size);
        return postRepository.getRandomPost(pageable).map(postMapper::toPostResponse);
    }

    @Override
    public PostResponse getPostById(Integer postId) {
        try{
            Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException("Post not found"));
            return postMapper.toPostResponse(postRepository.getOnePostByPostId(postId));
        }catch (DataAccessException e){
            throw new DataAccessException("Cannot access database");
        }

    }
}
