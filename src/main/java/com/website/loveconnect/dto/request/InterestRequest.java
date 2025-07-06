package com.website.loveconnect.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InterestRequest {
    @NotBlank(message = "Interest Name cannot be blank")
    private String interestName;
    @NotBlank(message = "Category cannot be blank")
    private String category;
}
