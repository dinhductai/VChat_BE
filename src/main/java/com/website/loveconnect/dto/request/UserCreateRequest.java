package com.website.loveconnect.dto.request;
import com.website.loveconnect.enumpackage.AccountStatus;
import com.website.loveconnect.enumpackage.Gender;
import com.website.loveconnect.enumpackage.RoleName;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateRequest {
    private String fullName;
    private String password;
    private String passwordConfirm;
    private Date birthDate;
    private String location;
    private String description;
    private List<String> interestName;
    private String phoneNumber;
    private String email;
    private Gender gender;
    private AccountStatus accountStatus;
    private RoleName roleName;
}
