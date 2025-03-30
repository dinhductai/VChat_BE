package com.website.loveconnect.dto.response;
import com.website.loveconnect.enumpackage.AccountStatus;
import com.website.loveconnect.enumpackage.Gender;
import com.website.loveconnect.enumpackage.RoleName;
import lombok.*;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoleGetResponse {
    private RoleName roleName;
    private String description;
    private Timestamp createTime;
    private List<PermissionResponse> permissionName;



}
