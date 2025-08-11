package com.website.loveconnect.service.impl;

import com.website.loveconnect.dto.request.CommentGetRequest;
import com.website.loveconnect.dto.request.CommentRequest;
import com.website.loveconnect.dto.response.CommentResponse;
import com.website.loveconnect.dto.response.UserNameAndProfileResponse;
import com.website.loveconnect.entity.Comment;
import com.website.loveconnect.entity.Post;
import com.website.loveconnect.entity.User;
import com.website.loveconnect.entity.UserProfile;
import com.website.loveconnect.exception.CommentNotFoundException;
import com.website.loveconnect.exception.PostNotFoundException;
import com.website.loveconnect.exception.UserNotFoundException;
import com.website.loveconnect.mapper.CommentMapper;
import com.website.loveconnect.repository.CommentRepository;
import com.website.loveconnect.repository.PostRepository;
import com.website.loveconnect.repository.UserProfileRepository;
import com.website.loveconnect.repository.UserRepository;
import com.website.loveconnect.service.CommentService;
import jakarta.persistence.Tuple;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
public class CommentServiceImpl implements CommentService {
    CommentRepository commentRepository;
    UserRepository userRepository;
    PostRepository postRepository;
    CommentMapper commentMapper;
    UserProfileRepository userProfileRepository;
//    @Override
//    public void createComment(CommentRequest commentRequest,Integer userId) {
//        try {
//            User user = userRepository.findById(userId)
//                    .orElseThrow(()->new UserNotFoundException("User not found"));
//            Post post =  postRepository.findById(commentRequest.getPostId())
//                    .orElseThrow(()-> new PostNotFoundException("Post not found"));
//            Comment comment = Comment.builder()
//                    .user(user)
//                    .post(post)
//                    .content(commentRequest.getContent())
//                    .commentDate(new Timestamp(System.currentTimeMillis()))
//                    .isEdited(commentRequest.getIsEdited())
//                    .isDeleted(commentRequest.getIsDeleted())
//                    .level(commentRequest.getLevel()) //=1
//                    .build();
//            commentRepository.save(comment);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void repComment(CommentRequest commentRequest, Integer userId) {
//        try {
//            User user = userRepository.findById(userId)
//                    .orElseThrow(()->new UserNotFoundException("User not found"));
//            Post post =  postRepository.findById(commentRequest.getPostId())
//                    .orElseThrow(()-> new PostNotFoundException("Post not found"));
//            Comment commentRepted = commentRepository.findById(commentRequest.getCommentId())
//                    .orElseThrow(ChangeSetPersister.NotFoundException::new);
//
//                Comment comment = Comment.builder()
//                        .user(user)
//                        .post(post)
//                        .content(commentRequest.getContent())
//                        .commentDate(new Timestamp(System.currentTimeMillis()))
//                        .isEdited(commentRequest.getIsEdited())
//                        .isDeleted(commentRequest.getIsDeleted())
//                        .parentComment(commentRepted)
//                        .level(commentRequest.getLevel()) //=2 =3
//                        .build();
//                commentRepository.save(comment);
//
//
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void editComment(CommentRequest commentRequest, Integer userId) {
//
//    }
//
//    @Override
//    public void deleteComment(CommentRequest commentRequest, Integer userId) {
//
//    }
//
    @Override
    public Page<CommentResponse> getComments(Integer postId,Integer level,Integer parentCommentId, int page, int size) {
        Pageable pageable = PageRequest.of(page,size);
        return commentRepository.getComments(pageable,postId,level,parentCommentId).map(commentMapper::toCommentResponse);
    }

    @Override
    public void editComment(CommentRequest commentRequest, Integer userId) {

    }

    @Override
    public void deleteComment(CommentRequest commentRequest, Integer userId) {

    }

//    @Override
//    public Page<CommentResponse> getComments(CommentGetRequest commentGetRequest, int page, int size) {
//        return null;
//    }

    // Sửa lại các hàm create và rep để trả về CommentResponse
    @Override
    public CommentResponse createComment(CommentRequest commentRequest, Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        Post post = postRepository.findById(commentRequest.getPostId())
                .orElseThrow(() -> new PostNotFoundException("Post not found"));
        Comment comment = Comment.builder()
                .user(user)
                .post(post)
                .content(commentRequest.getContent())
                .level(1) // Comment gốc luôn là level 1
                .build();
        Comment savedComment = commentRepository.save(comment);
        UserNameAndProfileResponse userNameAndProfileResponse = userProfileRepository.findUserNameAndProfileByUserId(user.getUserId())
                .orElseThrow(()-> new UserNotFoundException("User not found"));
        return CommentResponse.builder()
                .commentId(savedComment.getCommentId())
                .content(savedComment.getContent())
                .commentDate(savedComment.getCommentDate())
                .isDeleted(savedComment.getIsDeleted())
                .isEdited(savedComment.getIsEdited())
                .level(savedComment.getLevel())
                .parentCommentId(
                        savedComment.getParentComment() != null
                                ? savedComment.getParentComment().getCommentId()
                                : null
                )
                .postId(commentRequest.getPostId())
                .userId(userId)
                .fullName(userNameAndProfileResponse.getFullName())
                .photoUrl(userNameAndProfileResponse.getProfileUrl())
                .build();
    }

    @Override
    public CommentResponse repComment(CommentRequest commentRequest, Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        Post post = postRepository.findById(commentRequest.getPostId())
                .orElseThrow(() -> new PostNotFoundException("Post not found"));
        Comment parentComment = commentRepository.findById(commentRequest.getParentCommentId())
                .orElseThrow(() -> new CommentNotFoundException("Parent comment not found"));

        Comment reply = Comment.builder()
                .user(user)
                .post(post)
                .content(commentRequest.getContent())
                .parentComment(parentComment)
                .level(parentComment.getLevel()+1) // Level của con = level của cha + 1
                .build();
        Comment savedComment = commentRepository.save(reply);
        UserNameAndProfileResponse userNameAndProfileResponse = userProfileRepository.findUserNameAndProfileByUserId(user.getUserId())
                .orElseThrow(()-> new UserNotFoundException("User not found"));
        return CommentResponse.builder()
                .commentId(savedComment.getCommentId())
                .content(savedComment.getContent())
                .commentDate(savedComment.getCommentDate())
                .isDeleted(savedComment.getIsDeleted())
                .isEdited(savedComment.getIsEdited())
                .level(savedComment.getLevel())
                .parentCommentId(
                        savedComment.getParentComment() != null
                                ? savedComment.getParentComment().getCommentId()
                                : null
                )
                .postId(commentRequest.getPostId())
                .userId(userId)
                .fullName(userNameAndProfileResponse.getFullName())
                .photoUrl(userNameAndProfileResponse.getProfileUrl())
                .build(); // Trả về DTO
    }


    // Thêm hàm mới để lấy và xây dựng cây bình luận
    public List<CommentResponse> getCommentTreeByPostId(Integer postId) {
        // 1. Gọi repository để lấy danh sách Tuple
        List<Tuple> commentTuples = commentRepository.getAllCommentsByPostId(postId);

        // 2. Chuyển đổi tất cả Tuple sang CommentResponse DTO
        List<CommentResponse> allCommentDtos = commentTuples.stream()
                .map(commentMapper::tupleToCommentResponse)
                .collect(Collectors.toList());

        // 3. Xây dựng cây từ danh sách DTO
        List<CommentResponse> rootComments = new ArrayList<>();
        Map<Integer, CommentResponse> commentMap = allCommentDtos.stream()
                .collect(Collectors.toMap(CommentResponse::getCommentId, Function.identity()));

        allCommentDtos.forEach(commentDto -> {
            if (commentDto.getParentCommentId() == null) {
                // Đây là comment gốc (level 1)
                rootComments.add(commentDto);
            } else {
                // Đây là comment trả lời, tìm cha của nó trong map
                CommentResponse parentDto = commentMap.get(commentDto.getParentCommentId());
                if (parentDto != null) {
                    // Thêm vào danh sách replies của cha
                    parentDto.getReplies().add(commentDto);
                }
            }
        });

        return rootComments;
    }
}
