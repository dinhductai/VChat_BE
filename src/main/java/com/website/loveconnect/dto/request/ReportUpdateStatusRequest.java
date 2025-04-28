package com.website.loveconnect.dto.request;

import com.website.loveconnect.enumpackage.StatusReport;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReportUpdateStatusRequest {
    private int reportId;
    private StatusReport statusReport;
}
