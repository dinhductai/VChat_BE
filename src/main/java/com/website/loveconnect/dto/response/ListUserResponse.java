package com.website.loveconnect.dto.response;

import com.website.loveconnect.enumpackage.AccountStatus;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ListUserResponse {
    private Integer userId;
    private String fullName;
    private String email;
    private String phone;
    private Timestamp registrationDate;
    private AccountStatus accountStatus;


}
