package com.website.loveconnect.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostUpdateRequest {
    private Integer postId;
    private String content;


}
