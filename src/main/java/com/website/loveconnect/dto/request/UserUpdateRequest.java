package com.website.loveconnect.dto.request;

import com.website.loveconnect.enumpackage.AccountStatus;
import com.website.loveconnect.enumpackage.Gender;
import jakarta.validation.constraints.Email;
import lombok.*;

import java.util.Date;
import java.util.List;
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateRequest {
    private String fullName;
    private Date birthDate;
    private String location;
    private String description;
    private List<String> interestName;
    private String phoneNumber;
    private String email;
    private Gender gender;
    private AccountStatus accountStatus;
}
