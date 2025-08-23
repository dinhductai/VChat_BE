package com.website.loveconnect.config;

import com.website.loveconnect.enumpackage.RoleName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.bind.annotation.PostMapping;

import javax.crypto.spec.SecretKeySpec;
import java.util.ArrayList;
import java.util.List;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {


    @Autowired
    private CustomJWTDecoder customJWTDecoder;

    private static final String ADMIN_API_PREFIX = "/api/admin";
    private static final String USER_API_PREFIX = "/api";
    private static final String AUTH_API_PREFIX = "/api/auth";
//    private static final
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .cors(cors -> cors.configure(httpSecurity))
                .authorizeHttpRequests(registry -> configureAuthorization(registry))
        //bản thân là resource server nên dùng
        .oauth2ResourceServer(httpSecurityOAuth2ResourceServerConfigurer -> httpSecurityOAuth2ResourceServerConfigurer
                .jwt(jwtConfigurer -> jwtConfigurer.decoder(customJWTDecoder)
                        .jwtAuthenticationConverter(jwtConverter())));

        //tắt csrf
        httpSecurity.csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable());
        return httpSecurity.build();
    }

    //hàm config các endpoint
    private void configureAuthorization(AuthorizeHttpRequestsConfigurer<HttpSecurity>
                                                .AuthorizationManagerRequestMatcherRegistry registry){
        //endpoint công khai cho tất cả role và chưa đăng nhập
        String[] publicGetEndpoint = {
                USER_API_PREFIX+"/interests-name"
        };

        String[] publicPostEndpoint = {
                AUTH_API_PREFIX+"/log-in",
                AUTH_API_PREFIX+"/logout",
                AUTH_API_PREFIX+"/sign-up"

        };

        //endpoint mà admin và user dùng chung
        String[] generalPostEndpoint = {
                AUTH_API_PREFIX+"/introspect"
        };

        String[] adminGetEndpoint = {
                ADMIN_API_PREFIX+"/users",
                ADMIN_API_PREFIX+"/users/{userId}",
                ADMIN_API_PREFIX+"/users/{userId}/update",
                ADMIN_API_PREFIX+"/users/search",
                ADMIN_API_PREFIX+"/roles",
                ADMIN_API_PREFIX+"/roles/permissions",
                ADMIN_API_PREFIX+"/permissions",
                ADMIN_API_PREFIX+"/matches/{userId}"
        };

        String[] adminPostEndpoint = {
                ADMIN_API_PREFIX+"/users/create",
                ADMIN_API_PREFIX+"/users/profile-image/create",
                ADMIN_API_PREFIX+"/permissions/create",
                ADMIN_API_PREFIX+"/permissions/attach",
                ADMIN_API_PREFIX+"/matches/create"
        };
        String[] adminPutEndpoint = {
                ADMIN_API_PREFIX+"/users/{userId}/block",
                ADMIN_API_PREFIX+"/users/{userId}/unblock",
                ADMIN_API_PREFIX+"/users/{userId}/update",
                ADMIN_API_PREFIX+"/permissions/update",
                ADMIN_API_PREFIX+"/matches/{matchId}/status"
        };
        String[] adminDeleteEndpoint = {
                ADMIN_API_PREFIX+"/users/{userId}/delete",
                ADMIN_API_PREFIX+"/permissions/del/{permissionName}"
        };
        String[] userGetEndpoint = {
                USER_API_PREFIX+"/user/interest/{idUser}"
        };
        String[] userPostEndpoint = {
                USER_API_PREFIX+"/profile-image/upload",
                USER_API_PREFIX+"/user/interest/add/{idUser}"
        };
        String[] userPutEndpoint = {
                USER_API_PREFIX+"/user/interest/update/{idUser}/{idInterest}"
        };
        String[] userDeleteEndpoint = {
                USER_API_PREFIX+"/user/interest/delete/{idUser}/{idInterest}"
        };

        String[] apiDocumentEndpoint = {
                "/api-docs",
                "/api-docs/**",
                "/swagger-ui.html",
                "/swagger-ui/**"
        };
        registry.requestMatchers(apiDocumentEndpoint).permitAll();
        registry.requestMatchers("/ws/**").permitAll();
        //api không cần đăng nhập
        registry.requestMatchers(HttpMethod.GET, publicGetEndpoint).permitAll();

        registry.requestMatchers(HttpMethod.POST, publicPostEndpoint).permitAll();
        //api các role dùng chung
        registry.requestMatchers(HttpMethod.POST, generalPostEndpoint)
                .hasAnyRole(RoleName.ADMIN.name(), RoleName.USER.name());
        //api thuộc admin
        registry.requestMatchers(HttpMethod.GET, adminGetEndpoint).hasRole(RoleName.ADMIN.name());
        registry.requestMatchers(HttpMethod.POST, adminPostEndpoint).hasRole(RoleName.ADMIN.name());
        registry.requestMatchers(HttpMethod.POST, adminPutEndpoint).hasRole(RoleName.ADMIN.name());
        registry.requestMatchers(HttpMethod.DELETE,adminDeleteEndpoint).hasRole(RoleName.ADMIN.name());
        //api thuộc user
        registry.requestMatchers(HttpMethod.GET, userGetEndpoint).hasRole(RoleName.USER.name());
        registry.requestMatchers(HttpMethod.POST, userPostEndpoint).hasRole(RoleName.USER.name());
        registry.requestMatchers(HttpMethod.POST, userPutEndpoint).hasRole(RoleName.USER.name());
        registry.requestMatchers(HttpMethod.DELETE, userDeleteEndpoint).hasRole(RoleName.USER.name());

        registry.anyRequest().authenticated();
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


//    @Bean
//    public JwtDecoder jwtDecoder() {
//        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), "HS512");
//        return NimbusJwtDecoder
//                .withSecretKey(secretKeySpec)
//                .macAlgorithm(MacAlgorithm.HS512)
//                .build();
//    }
}
