package com.website.loveconnect.service;

import com.website.loveconnect.dto.request.CommentRequest;
import com.website.loveconnect.entity.Comment;

public interface CommentService {
    void createComment(CommentRequest commentRequest,Integer userId);
}
