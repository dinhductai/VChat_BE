package com.website.loveconnect.config;

import com.website.loveconnect.entity.Role;
import com.website.loveconnect.entity.User;
import com.website.loveconnect.entity.UserProfile;
import com.website.loveconnect.entity.UserRole;
import com.website.loveconnect.enumpackage.AccountStatus;
import com.website.loveconnect.enumpackage.Gender;
import com.website.loveconnect.enumpackage.RoleName;
import com.website.loveconnect.repository.RoleRepository;
import com.website.loveconnect.repository.UserProfileRepository;
import com.website.loveconnect.repository.UserRepository;
import com.website.loveconnect.repository.UserRoleRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
@Slf4j
public class ApplicationInitConfig {
    UserRepository userRepository;
    UserRoleRepository userRoleRepository;
    RoleRepository roleRepository;
    UserProfileRepository userProfileRepository;

    @Bean
    public ApplicationRunner applicationRunner(UserRepository userRepository) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        return args -> {
            //check trong database chưa có bất kì tài khoản admin nào
            if(userRepository.existsByRoleAdminAndStatusActive() == 0) {
                User user = User.builder()
                        .email("admin@admin.com")
                        .password(passwordEncoder.encode("admin"))
                        .isVerified(true)
                        .accountStatus(AccountStatus.ACTIVE)
                        .build();
                UserProfile userProfile = UserProfile.builder()
                        .fullName("Admin")
                        .birthDate(new Date())
                        .gender(Gender.OTHER)
                        .lookingFor(Gender.OTHER)
                        .user(user)
                        .build();
                Role role = Role.builder()
                        .roleName(RoleName.ADMIN)
                        .build();
                UserRole userRole = UserRole.builder()
                        .user(user)
                        .role(role)
                        .build();
                userRepository.save(user);
                userProfileRepository.save(userProfile);
                roleRepository.save(role);
                userRoleRepository.save(userRole);
                log.warn("ADMIN ACCOUNT CREATED");
            }
        };
    }

}
