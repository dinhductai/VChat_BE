package com.website.loveconnect.service.impl;

import com.website.loveconnect.dto.request.CommentRequest;
import com.website.loveconnect.entity.Comment;
import com.website.loveconnect.entity.Post;
import com.website.loveconnect.entity.User;
import com.website.loveconnect.exception.PostNotFoundException;
import com.website.loveconnect.exception.UserNotFoundException;
import com.website.loveconnect.repository.CommentRepository;
import com.website.loveconnect.repository.PostRepository;
import com.website.loveconnect.repository.UserRepository;
import com.website.loveconnect.service.CommentService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
public class CommentServiceImpl implements CommentService {
    CommentRepository commentRepository;
    UserRepository userRepository;
    PostRepository postRepository;

    @Override
    public void createComment(CommentRequest commentRequest,Integer userId) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(()->new UserNotFoundException("User not found"));
            Post post =  postRepository.findById(commentRequest.getPostId())
                    .orElseThrow(()-> new PostNotFoundException("Post not found"));
            Comment comment = Comment.builder()
                    .user(user)
                    .post(post)
                    .content(commentRequest.getContent())
                    .commentDate(new Timestamp(System.currentTimeMillis()))
                    .isEdited(commentRequest.getIsEdited())
                    .isDeleted(commentRequest.getIsDeleted())
                    .build();
            commentRepository.save(comment);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
