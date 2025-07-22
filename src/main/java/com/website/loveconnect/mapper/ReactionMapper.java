package com.website.loveconnect.mapper;

import com.website.loveconnect.dto.response.ReactionResponse;
import com.website.loveconnect.entity.Reaction;
import com.website.loveconnect.enumpackage.EmotionName;
import jakarta.persistence.Tuple;
import org.springframework.stereotype.Component;

@Component
public class ReactionMapper {
    public ReactionResponse toReactionResponse(Tuple tuple) {
        return ReactionResponse.builder()
                .totalReaction(tuple.get("totalReaction",Long.class))
                .isReacted(tuple.get("isReacted",Long.class))
                .emotionName(tuple.get("emotionName", String.class))
                .build();
    }

}
