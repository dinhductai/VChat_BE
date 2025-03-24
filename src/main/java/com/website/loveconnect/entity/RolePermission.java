package com.website.loveconnect.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "role_permissions")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
//đánh khóa chính composite
@IdClass(RolePermission.class)
public class RolePermission {
    @Id
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "role_id",nullable = false)
    private Role role;

    @Id
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "permission_id",nullable = false)
    private Permission permission;
}
