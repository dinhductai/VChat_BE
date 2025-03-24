package com.website.loveconnect.entity.entityid;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RolePermissionId implements Serializable { //tuần tự hóa để quản lý ở Context với Catching
    private Integer role;
    private Integer permission;
}
