package com.website.loveconnect.dto.response;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PermissionGetResponse {
    private Integer permissionId;
    private String permissionName;
    private String description;
    private Timestamp created;
}
