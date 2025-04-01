package com.website.loveconnect.custom.entitycustom;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.website.loveconnect.custom.annotationcustom.*;
import com.website.loveconnect.enumpackage.AccountStatus;
import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@TableCustom(name = "users")
@EntityCustom
public class UserCustom implements Serializable {
    @IdCustom
    @GeneratedValueCustom
    @ColumnCustom(name = "user_id")
    private Integer userId;

    @ColumnCustom(name = "email", nullable = false, unique = true)
    private String email;

    @ColumnCustom(name = "password", nullable = false)
    private String password;

    @ColumnCustom(name = "phone_number")
    private String phoneNumber;

    @ColumnCustom(name = "is_verified")
    private Boolean isVerified;

    @ColumnCustom(name = "registration_date")
    private Timestamp registrationDate;

    @ColumnCustom(name = "last_login_date")
    private Timestamp lastLoginDate;

    @ColumnCustom(name = "account_status")
    @EnumeratedCustom
    private AccountStatus accountStatus = AccountStatus.ACTIVE;

    public UserCustom() {
    }

    public UserCustom(Integer userId, String email, String password, String phoneNumber, Boolean isVerified, Timestamp registrationDate, Timestamp lastLoginDate, AccountStatus accountStatus) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.isVerified = isVerified;
        this.registrationDate = registrationDate;
        this.lastLoginDate = lastLoginDate;
        this.accountStatus = accountStatus;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Boolean getVerified() {
        return isVerified;
    }

    public void setVerified(Boolean verified) {
        isVerified = verified;
    }

    public Timestamp getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Timestamp registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Timestamp getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(Timestamp lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public AccountStatus getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(AccountStatus accountStatus) {
        this.accountStatus = accountStatus;
    }
}
