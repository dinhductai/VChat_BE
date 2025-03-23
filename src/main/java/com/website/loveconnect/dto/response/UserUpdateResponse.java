package com.website.loveconnect.dto.response;

import com.website.loveconnect.enumpackage.AccountStatus;
import com.website.loveconnect.enumpackage.Gender;
import lombok.*;

import java.security.Timestamp;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateResponse {
    private Integer userId;
    private String fullName;
    private Date birthDate;
    private String location;
    private String description;
    private List<String> interestName;
    private String photoUrl;
    private String phoneNumber;
    private String email;
    private Gender gender;
    private AccountStatus accountStatus;
}
