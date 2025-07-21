package com.website.loveconnect.dto.response;

import com.website.loveconnect.enumpackage.EmotionName;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReactionResponse {
    private Long totalReaction;
    private Long isReacted;
    private String emotionName;
}
