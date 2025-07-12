package com.website.loveconnect.service;

import com.website.loveconnect.dto.request.CommentGetRequest;
import com.website.loveconnect.dto.request.CommentRequest;
import com.website.loveconnect.dto.response.CommentResponse;
import com.website.loveconnect.entity.Comment;
import org.springframework.data.domain.Page;

public interface CommentService {
    void createComment(CommentRequest commentRequest,Integer userId);
    void repComment(CommentRequest commentRequest,Integer userId);
    void editComment(CommentRequest commentRequest,Integer userId);
    void deleteComment(CommentRequest commentRequest,Integer userId);
    Page<CommentResponse> getComments(CommentGetRequest commentGetRequest,int page, int size);
}
