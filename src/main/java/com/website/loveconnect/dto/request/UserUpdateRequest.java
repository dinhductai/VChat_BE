package com.website.loveconnect.dto.request;

import com.website.loveconnect.enumpackage.AccountStatus;
import com.website.loveconnect.enumpackage.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Date;
import java.util.List;
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateRequest {
    @NotBlank(message = "Full name is required")
    @Size(min = 2, max = 50, message = "Full name must be between 2 and 50 characters")
    private String fullName;
    @Past(message = "Birth date must be in the past")
    private Date birthDate;
    @Size(max = 100, message = "Location must not exceed 100 characters")
    private String location;
    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;
    private List<@NotBlank(message = "Interest name cannot be blank")  String> interestName;
    @Pattern(
            regexp = "^(\\+?\\d{1,3})?\\d{9,15}$",
            message = "Invalid phone number format"
    )
    private String phoneNumber;
    @Email(message = "Invalid email format")
    private String email;
    @NotBlank(message = "Gender cannot be blank")
    private Gender gender;
    @NotBlank(message = "Account status cannot be blank")
    private AccountStatus accountStatus;
}
