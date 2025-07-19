package com.website.loveconnect.dto.response;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentResponse {
    private Integer commentId;
    private Integer postId;
    private String content;
    private Boolean isEdited;
    private Boolean isDeleted;
    private Integer parentCommentId;
    private Integer level;
    private Integer userId;
    private Timestamp commentDate;
    private String fullName;
    private String bio;
    private String phoneNumber;
    private String photoUrl;

    // Thêm trường mới để chứa các bình luận trả lời
    @Builder.Default
    private List<CommentResponse> replies = new ArrayList<>();
}
