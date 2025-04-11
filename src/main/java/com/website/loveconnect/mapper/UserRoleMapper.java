package com.website.loveconnect.mapper;

import com.website.loveconnect.entity.Role;
import com.website.loveconnect.entity.User;
import com.website.loveconnect.entity.UserRole;
import org.springframework.stereotype.Component;

@Component
public class UserRoleMapper {
    public UserRole toAttachUserRole(User user, Role role){
        UserRole userRole = UserRole.builder()
                .user(user)
                .role(role)
                .build();
        return userRole;
    }
}
