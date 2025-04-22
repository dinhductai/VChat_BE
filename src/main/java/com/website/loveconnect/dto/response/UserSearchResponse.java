package com.website.loveconnect.dto.response;

import com.website.loveconnect.enumpackage.Gender;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserSearchResponse {
    private String email;
    private String phoneNumber;
    private String fullName;
    private Date birthDate;
    private Gender gender;
    private Gender lookingFor;
    private String bio;
    private Integer height;
    private Integer weight;
    private String location;
    private String jobTitle;
    private String company;
    private String education;
    private String description;
    private List<String> interestName;
}
