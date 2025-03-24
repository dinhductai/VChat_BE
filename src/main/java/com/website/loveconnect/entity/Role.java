package com.website.loveconnect.entity;

import com.website.loveconnect.enumpackage.RoleName;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.context.annotation.EnableMBeanExport;

import java.sql.Timestamp;
import java.util.List;


@Entity
@Table(name = "roles")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Integer roleId;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_name")
    private RoleName roleName;

    @Column(name = "description")
    private String description;

    @Column(name = "created_date")
    private Timestamp createdDate = new Timestamp(System.currentTimeMillis());

    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<UserRole> userRoles;

    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<RolePermission> rolePermissions;
}
