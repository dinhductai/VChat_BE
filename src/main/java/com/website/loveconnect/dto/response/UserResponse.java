package com.website.loveconnect.dto.response;

import com.website.loveconnect.enumpackage.AccountStatus;
import com.website.loveconnect.enumpackage.Gender;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.*;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private Integer userId;
    private String photoUrl;
    private String fullName;
    private String email;
    private Gender gender;
    private String location;
    private String description;
    private List<String> interestName;
    private Timestamp registrationDate;
    private Date birthDate;
    private String phoneNumber;
    private AccountStatus accountStatus;
}
