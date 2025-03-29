package com.website.loveconnect.dto.request;

import lombok.*;

import java.sql.Timestamp;
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PermissionUpdateRequest {
    private Integer permissionId;
    private String permissionName;
    private String description;
}
