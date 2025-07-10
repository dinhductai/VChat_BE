package com.website.loveconnect.dto.request;

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
//    private Integer userId;
    private Integer postId;
    private String content;
    private Boolean isEdited;
    private Boolean isDeleted;
    private Integer parentCommentId;
}
