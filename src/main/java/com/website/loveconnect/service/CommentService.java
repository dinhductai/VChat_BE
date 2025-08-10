package com.website.loveconnect.service;

import com.website.loveconnect.dto.request.CommentGetRequest;
import com.website.loveconnect.dto.request.CommentRequest;
import com.website.loveconnect.dto.response.CommentResponse;
import com.website.loveconnect.entity.Comment;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CommentService {
//    void createComment(CommentRequest commentRequest,Integer userId);
//    void repComment(CommentRequest commentRequest,Integer userId);
    void editComment(CommentRequest commentRequest,Integer userId);
    void deleteComment(CommentRequest commentRequest,Integer userId);
    Page<CommentResponse> getComments(Integer postId,Integer level,Integer parentCommentId,int page, int size);

    CommentResponse createComment(CommentRequest commentRequest, Integer userId);
    CommentResponse repComment(CommentRequest commentRequest, Integer userId);
    List<CommentResponse> getCommentTreeByPostId(Integer postId);



}
