package com.website.loveconnect.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostShareSaveRequest {
    @NonNull
    private Integer postId;
    @NotBlank(message = "Status share or save not blank")
    private String status;
}
