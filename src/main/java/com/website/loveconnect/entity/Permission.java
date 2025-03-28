package com.website.loveconnect.entity;

import com.website.loveconnect.enumpackage.RoleName;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "permissions")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "permission_id")
    private Integer permissionId;

    @Column(name = "permission_name",nullable = false,unique = true)
    private String permissionName;

    @Column(name = "description")
    private String description;

    @Column(name = "created")
    private Timestamp created = new Timestamp(System.currentTimeMillis());

    @OneToMany(mappedBy = "permission", fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<RolePermission> rolePermissions;
}
