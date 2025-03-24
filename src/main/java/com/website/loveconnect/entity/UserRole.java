package com.website.loveconnect.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.website.loveconnect.entity.entityid.UserRoleId;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Entity
@Table(name = "user_roles")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@IdClass(UserRoleId.class) //id composite
public class UserRole {

    @Id
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    @Id
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "role_id",nullable = false)
    private Role role;

}
