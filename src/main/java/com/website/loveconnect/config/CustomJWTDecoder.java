package com.website.loveconnect.config;

import com.website.loveconnect.dto.request.IntrospectRequest;
import com.website.loveconnect.exception.TokenInvalid;
import com.website.loveconnect.service.impl.AuthenticationServiceImpl;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import javax.naming.AuthenticationException;
import java.util.Objects;

@Component
public class CustomJWTDecoder implements JwtDecoder {

    @NonFinal
    @Value("${jwt.secret}")
    protected String SIGNED_KEY;
    @Autowired
    private AuthenticationServiceImpl authenticationService;
    private NimbusJwtDecoder nimbusJwtDecoder = null;

    @Override
    public Jwt decode(String token) throws JwtException {
        try{
//            check xem token còn hiệu lực ko
            var checkToken = authenticationService.introspect(IntrospectRequest
                    .builder()
                    .token(token)
                    .build());
            if(!checkToken.isValid()){
                throw new TokenInvalid("Token invalid");
            }

        } catch (AuthenticationException e) {
            throw new RuntimeException(e);
        }
        if(Objects.isNull(nimbusJwtDecoder)){
            SecretKeySpec secretKeySpec = new SecretKeySpec(SIGNED_KEY.getBytes(), "HS512");
            nimbusJwtDecoder = NimbusJwtDecoder
                    .withSecretKey(secretKeySpec)
                    .macAlgorithm(MacAlgorithm.HS512)
                    .build();
        }
        return nimbusJwtDecoder.decode(token);
    }
}
