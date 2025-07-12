package com.website.loveconnect.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentGetRequest {
    @NonNull
    private Integer postId;
    @NonNull
    private Integer level;
    private Integer parentCommentId;
}
