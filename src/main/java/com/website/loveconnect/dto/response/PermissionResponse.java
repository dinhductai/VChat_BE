package com.website.loveconnect.dto.response;


import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PermissionResponse {
    private String permissionName;
    private String description;
    private Timestamp created;
}
