package com.website.loveconnect.service;

import com.website.loveconnect.dto.request.PostRequest;
import com.website.loveconnect.dto.request.ReelRequest;
import com.website.loveconnect.dto.response.PostResponse;
import com.website.loveconnect.dto.response.ReelResponse;
import com.website.loveconnect.entity.Post;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PostService {
    PostResponse savePost(PostRequest postRequest);
    Page<PostResponse> getRandom(int page,int size);
    PostResponse getPostById(Integer postId);
    Page<PostResponse> getOwnPost(Integer userId,int page,int size);
    Page<ReelResponse> getReelRandom(int page,int size);
    ReelResponse createReel(ReelRequest reelRequest);
}
