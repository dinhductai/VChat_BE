package com.website.loveconnect.dto.request;

import lombok.*;
import jakarta.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentQueryParametersRequest {
    private int page = 0;
    private int size = 10;
    @NotNull
    private Integer postId;
    @NotNull
    private Integer level;
    private Integer parentCommentId;

}
