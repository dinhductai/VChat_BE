package com.website.loveconnect.service;

import com.website.loveconnect.dto.request.ReactionRequest;
import com.website.loveconnect.dto.response.ReactionResponse;
import com.website.loveconnect.entity.Reaction;
import com.website.loveconnect.enumpackage.ContentType;

public interface ReactionService {
    void addReaction(ReactionRequest reactionRequest, Integer userId);
    Long countReactionOnAPost(Integer postId);
}
