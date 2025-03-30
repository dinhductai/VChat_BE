package com.website.loveconnect.config;

import com.website.loveconnect.enumpackage.RoleName;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.spec.SecretKeySpec;
import java.util.ArrayList;
import java.util.List;


@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Value(value = "${jwt.secret}")
    private String secretKey;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> authorizationManagerRequestMatcherRegistry
                .requestMatchers(HttpMethod.POST,"/api/auth/log-in","/api/admin/users/create")
                .permitAll()
                .requestMatchers(HttpMethod.GET,"/api/admin/users/{userId}")
                .hasRole(RoleName.ADMIN.name())
                .anyRequest().authenticated());

        //bản thân là resource server nên dùng
        httpSecurity.oauth2ResourceServer(httpSecurityOAuth2ResourceServerConfigurer -> httpSecurityOAuth2ResourceServerConfigurer
                .jwt(jwtConfigurer -> jwtConfigurer.decoder(jwtDecoder())
                        .jwtAuthenticationConverter(jwtConverter())));

        //tắt csrf
        httpSecurity.csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable());
        return httpSecurity.build();
    }

    //convert từ SCOPE_ qua ROLE_
    @Bean
    public JwtAuthenticationConverter jwtConverter() {
        //set lại chuẩn autho
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwt -> {
            List<GrantedAuthority> authorities = new ArrayList<>();
            String scope = jwt.getClaimAsString("scope");
            if (scope != null) {
                String[] scopeItems = scope.split(" ");
                for (String item : scopeItems) {
                    if (item.equals("ADMIN")) {
                        //nếu là ADMIN, thêm ROLE_ADMIN
                        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
                    } else if (item.equals("USER")) {
                        //nếu là USER, thêm ROLE_USER
                        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
                    } else {
                        // nếu không phải role, coi là permission và giữ nguyên
                        authorities.add(new SimpleGrantedAuthority(item));
                    }
                }
            }
            return authorities;
        });
        return jwtAuthenticationConverter;
    }


    @Bean
    public JwtDecoder jwtDecoder() {
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), "HS512");
        return NimbusJwtDecoder
                .withSecretKey(secretKeySpec)
                .macAlgorithm(MacAlgorithm.HS512)
                .build();
    }
}
