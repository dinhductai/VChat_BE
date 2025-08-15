package com.website.loveconnect.dto.request;

import com.website.loveconnect.enumpackage.AccountStatus;
import com.website.loveconnect.enumpackage.Gender;
import com.website.loveconnect.enumpackage.RoleName;
import jakarta.validation.constraints.*;
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

    @NotBlank(message = "Full name cannot be blank")
    @Size(min = 2, max = 50, message = "Full name must be between 2 and 50 characters")
    private String fullName;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8, max = 100, message = "Password must be at least 8 characters")
    private String password;

    @NotBlank(message = "Password confirmation cannot be blank")
    private String passwordConfirm;

    @NotNull(message = "Birth date cannot be null")
    @Past(message = "Birth date must be in the past")
    private Date birthDate;

    @Size(max = 255, message = "Location must be less than 255 characters")
    private String location;

    @Size(max = 500, message = "Description must be less than 500 characters")
    private String description;

    @NotNull(message = "Interests list cannot be null")
    @Size(min = 1, message = "At least one interest must be provided")
    private List<@NotBlank(message = "Interest name cannot be blank") String> interestName;

    @Pattern(
            regexp = "^(\\+?\\d{1,3}[- ]?)?\\d{9,15}$",
            message = "Invalid phone number format"
    )
    private String phoneNumber;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email format")
    private String email;

    @NotNull(message = "Gender is required")
    private Gender gender;

    @NotNull(message = "Account status is required")
    private AccountStatus accountStatus;

    @NotNull(message = "Role name is required")
    private RoleName roleName;
}
