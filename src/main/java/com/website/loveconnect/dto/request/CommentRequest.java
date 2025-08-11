package com.website.loveconnect.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentRequest {
    private String token;
    private Integer commentId;
    @NotNull
    private Integer postId;
    @NotBlank(message = "Content comment cannot be blank")
    private String content;
    private Boolean isEdited;
    private Boolean isDeleted;
    private Integer parentCommentId;
    @NotNull
    @Min(value = 1,message = "Comment level at least 1")
    @Max(value = 3,message = "Comment level cannot lager than 3")
    private Integer level;
}
