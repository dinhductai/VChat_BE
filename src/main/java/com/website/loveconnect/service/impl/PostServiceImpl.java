package com.website.loveconnect.service.impl;

import com.cloudinary.utils.StringUtils;
import com.website.loveconnect.dto.request.PostRequest;
import com.website.loveconnect.dto.request.PostShareSaveRequest;
import com.website.loveconnect.dto.request.PostUpdateRequest;
import com.website.loveconnect.dto.request.ReelRequest;
import com.website.loveconnect.dto.response.PostResponse;
import com.website.loveconnect.dto.response.ReelResponse;
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
            Post post = Post.builder()
                    .content(postRequest.getContent())
                    .uploadDate(new Timestamp(System.currentTimeMillis()))
                    .isPublic(postRequest.getIsPublic())
                    .isApproved(true)
                    .isReel(false)
                    .status(PostStatus.ACTIVE)
                    .build();
            post = postRepository.save(post);

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
            // Xử lý ảnh
            List<MultipartFile> imageFiles = postRequest.getListImage();
            if (imageFiles != null && !imageFiles.isEmpty()) {
                for (MultipartFile photo : imageFiles) {
                    try {
                        photoService.uploadPhotoForPost(photo, postRequest.getUserEmail(), post);
                        log.info("Uploaded photo for post ID: {}", post.getPostId());
                    } catch (Exception e) {
                        log.error("Failed to upload photo for post ID: {}", post.getPostId(), e);
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
                        log.info("Uploaded video for post ID: {}", post.getPostId());
                    } catch (Exception e) {
                        log.error("Failed to upload video for post ID: {}", post.getPostId(), e);
                        throw new RuntimeException("Failed to upload video", e);
                    }
                }
            }
            return postMapper.toPostResponse(postRepository.getOnePostByPostId(post.getPostId()));
        } catch (DataAccessException da) {
            throw new DataAccessException("Cannot access database");
        }
    }

    @Override
    public Page<PostResponse> getRandom(int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            return postRepository.getRandomPost(pageable).map(postMapper::toPostResponse);
        }
        catch (DataAccessException e) {
            throw new DataAccessException("Cannot access database");
        }
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

    @Override
    public Page<PostResponse> getOwnPost(Integer userId, int page, int size) {
        try{
            Pageable pageable = PageRequest.of(page,size);
            return postRepository.getPostsByUserId(userId, pageable).map(postMapper::toPostResponse);
        }catch (DataAccessException e){
            throw new DataAccessException("Cannot access database");
        }
    }

    @Override
    public Page<ReelResponse> getReelRandom( int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            return postRepository.getRandomReel(pageable).map(postMapper::toReelResponse);
        }
        catch (DataAccessException e) {
            throw new DataAccessException("Cannot access database");
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }    }

    @Override
    public ReelResponse createReel(ReelRequest reelRequest) {
        try {
            // Kiểm tra dữ liệu đầu vào
            if (reelRequest == null || StringUtils.isEmpty(reelRequest.getUserEmail())) {
                throw new IllegalArgumentException("PostRequest or userEmail cannot be null");
            }
            Post post = Post.builder()
                    .content(reelRequest.getContent())
                    .uploadDate(new Timestamp(System.currentTimeMillis()))
                    .isPublic(reelRequest.getIsPublic())
                    .isApproved(true)
                    .status(PostStatus.ACTIVE)
                    .isReel(true)
                    .build();
            post = postRepository.save(post); // Lưu Post để có ID
            log.info("Saved post with ID: {}", post.getPostId());

            // Lưu UserPost
            User user = userRepository.getUserByEmail(reelRequest.getUserEmail())
                    .orElseThrow(() -> new UserNotFoundException("User not found"));
            UserPost userPost = UserPost.builder()
                    .post(post)
                    .user(user)
                    .upload(true)
                    .share(false)
                    .save(false)
                    .build();
            userPostRepository.save(userPost);
            log.info("Saved UserPost for user: {} and post ID: {}", reelRequest.getUserEmail(), post.getPostId());

            // Xử lý video
            MultipartFile videoFiles = reelRequest.getVideo();
            if (videoFiles != null && !videoFiles.isEmpty()) {
                    try {
                        videoService.uploadVideoForPost(videoFiles, reelRequest.getUserEmail(), post);
                        log.info("Uploaded video for post ID: {}", post.getPostId());
                    } catch (Exception e) {
                        log.error("Failed to upload video for post ID: {}", post.getPostId(), e);
                        throw new RuntimeException("Failed to upload video", e);
                    }

            }
            return postMapper.toReelResponse(postRepository.getOneReelByPostId(post.getPostId()));
        } catch (Exception e) {
            log.error("Failed to save post for user: {}", reelRequest.getUserEmail(), e);
            throw new RuntimeException("Failed to save post", e);
        }
    }

    @Override
    public void shareOrSavePost(PostShareSaveRequest postShareSaveRequest, Integer userId) {
        try{
            User user =  userRepository.findById(userId)
                    .orElseThrow(() -> new UserNotFoundException("User not found"));
            Post post = postRepository.findById(postShareSaveRequest.getPostId())
                    .orElseThrow(() -> new PostNotFoundException("Post not found"));
            UserPost userPost = userPostRepository.findByUserAndPostAndUpload(user,post,true);
            if(userPost == null && postShareSaveRequest.getStatus().equals("SHARE")){
                UserPost newUserPost = UserPost.builder()
                        .user(user)
                        .post(post)
                        .upload(false)
                        .share(true)
                        .save(false)
                        .build();
                userPostRepository.save(newUserPost);
            }else if(userPost == null && postShareSaveRequest.getStatus().equals("SAVE")) {
                UserPost newUserPost = UserPost.builder()
                        .user(user)
                        .post(post)
                        .upload(false)
                        .share(false)
                        .save(true)
                        .build();
                userPostRepository.save(newUserPost);
            }else {
                userPost.setShare(true);
                userPostRepository.save(userPost);
            }
        }catch (DataAccessException da){
            throw new DataAccessException("Cannot access database");
        }
    }

    @Override
    public void deletePost(Integer postId, Integer userId) {
        try{
            User user =  userRepository.findById(userId)
                    .orElseThrow(() -> new UserNotFoundException("User not found"));
            Post post = postRepository.findById(postId)
                    .orElseThrow(() -> new PostNotFoundException("Post not found"));
            if(post!=null && user!=null){
                userPostRepository.deleteByUserAndPost(user,post);
            }

        }catch (DataAccessException da){
            throw new DataAccessException("Cannot access database");
        }
    }

    @Override
    public PostResponse updatePostById(PostUpdateRequest postUpdateRequest,Integer userId) {
        try{
//            Boolean userExisting = userRepository.existsByUserId(userId);
            User userExisting = userRepository.findById(userId)
                    .orElseThrow(()->new UserNotFoundException("User not found"));
            Post post = postRepository.findById(postUpdateRequest.getPostId())
                    .orElseThrow(() -> new PostNotFoundException("Post not found"));
            if(userExisting !=null && post!=null){
                post.setContent(postUpdateRequest.getContent());
                post.setUploadDate(new Timestamp(System.currentTimeMillis()));
                postRepository.save(post);
                return postMapper.toPostResponse(postRepository.getOnePostByPostId(post.getPostId()));
            }
            else return null;

        }catch (DataAccessException da){
            throw new DataAccessException("Cannot access database");
        }
    }

    @Override
    public Page<PostResponse> getOwnPostShared(Integer userId, int page, int size) {
        try{
            Pageable pageable = PageRequest.of(page,size);
            return postRepository.getPostsSharedByUserId(userId, pageable).map(postMapper::toPostResponse);
        }catch (DataAccessException e){
            throw new DataAccessException("Cannot access database");
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Page<PostResponse> getOwnPostSaved(Integer userId, int page, int size) {
        try{
            Pageable pageable = PageRequest.of(page,size);
            return postRepository.getPostsSavedByUserId(userId, pageable).map(postMapper::toPostResponse);
        }catch (DataAccessException e){
            throw new DataAccessException("Cannot access database");
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }    }
}
