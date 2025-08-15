package com.website.loveconnect.dto.request;

import com.website.loveconnect.enumpackage.StatusReport;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReportUpdateStatusRequest {
    @NotNull
    private int reportId;
    @NotNull
    private StatusReport statusReport;
}
