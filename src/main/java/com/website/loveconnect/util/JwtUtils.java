package com.website.loveconnect.util;

import com.nimbusds.jwt.SignedJWT;
import com.nimbusds.jwt.JWTClaimsSet;
import java.text.ParseException;
import java.util.Date;

public class JwtUtils  {

    public static void decodeToken(String token) {
        try {
            // Parse token
            SignedJWT signedJWT = SignedJWT.parse(token);

            // Lấy claims
            JWTClaimsSet claims = signedJWT.getJWTClaimsSet();

            // Trích xuất thông tin
            String userId = claims.getSubject(); // sub
            String email = (String) claims.getClaim("email");
            String scope = (String) claims.getClaim("scope");
            String tokenId = claims.getJWTID();
            Date issueTime = claims.getIssueTime();
            Date expiryTime = claims.getExpirationTime();

            // In ra thông tin
            System.out.println("User ID: " + userId);
            System.out.println("Email: " + email);
            System.out.println("Scope: " + scope);
            System.out.println("Token ID (jti): " + tokenId);
            System.out.println("Issued at: " + issueTime);
            System.out.println("Expires at: " + expiryTime);

        } catch (ParseException e) {
            System.err.println("Token invalid or malformed!");
            e.printStackTrace();
        }
    }
}
