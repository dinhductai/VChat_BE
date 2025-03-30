package com.website.loveconnect.dto.request;
import com.website.loveconnect.enumpackage.RoleName;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PermissionAttachRequest {
    private Integer[] permissionIds; ;
    private RoleName roleName;
}
