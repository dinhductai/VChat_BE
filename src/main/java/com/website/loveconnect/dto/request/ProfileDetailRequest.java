package com.website.loveconnect.dto.request;

import com.website.loveconnect.enumpackage.Gender;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProfileDetailRequest {
    @NotBlank(message = "Email cannot be blank")
    @Email(
            regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$",
            message = "Email invalid"
    )
    private String email;
    @Pattern(regexp = "\\d{10}", message = "Phone number must be 10 digits")
    private String phoneNumber;
    @NotBlank(message = "User name cannot be blank")
    @Max(value = 30,message = "User name can be at most 30 characters")
    private String fullName;
    @NotNull(message = "Birthdate is required")
    @Past(message = "Birthdate must be in past")
    private Date birthDate;
    @NotNull(message = "Gender is required")
    private Gender gender;
    @NotNull(message = "Looking for is required")
    private Gender lookingFor;
    private String bio;
    @Max(value = 250,message = "Height can be at most 250 cm ~ 2m5" )
    private Integer height;
    @Max(value = 150,message = "Weight can be at most 150 kg")
    private Integer weight;
    private String location;
    private String jobTitle;
    private String company;
    private String education;
    private String description;
    private List<String> interestName;
}
