package com.website.loveconnect.service;

import com.website.loveconnect.dto.request.PostRequest;
import com.website.loveconnect.entity.Post;

public interface PostService {
    void savePost(PostRequest postRequest);
}
