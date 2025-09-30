package com.website.loveconnect.util;

import com.website.loveconnect.entity.User;
import com.website.loveconnect.enumpackage.AccountStatus;
import com.website.loveconnect.exception.UserNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class AuthenticationUtil {
    public static void checkAccountDeleteOrBlock(User user){
        if(user.getAccountStatus().equals(AccountStatus.BLOCKED) ||
                user.getAccountStatus().equals(AccountStatus.DELETED)){
            throw new UserNotFoundException("Account is invalid");
        }
    }

    public static void checkPassword(String rawPassword, String encodedPassword){
        //cài đặt thuật toán và độ phức tạp
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean checkPass =  passwordEncoder.matches(rawPassword, encodedPassword);
        if(!checkPass) {
            throw new BadCredentialsException("Incorrect email or password");
        }
    }
}
