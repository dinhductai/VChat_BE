package com.website.loveconnect.dto.request;

import com.website.loveconnect.enumpackage.ContentType;
import com.website.loveconnect.enumpackage.EmotionName;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReactionRequest {
    @NotBlank(message = "Emotion name cannot be blank")
    private EmotionName emotionName;
    @NotBlank(message = "ContentId cannot be blank")
    private Integer contentId;
    @NotBlank(message = "Content reaction cannot be blank")
    private ContentType contentReact;
}
