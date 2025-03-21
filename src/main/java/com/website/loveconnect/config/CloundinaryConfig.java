package com.website.loveconnect.config;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.HashMap;
import java.util.Map;

@Configuration
//đọc từ file riêng
@PropertySource("classpath:cloudinary.properties")
public class CloundinaryConfig {
    //lấy các giá trị
    @Value("${cloudinary.cloud-name}")
    private String cloudName;
    @Value("${cloudinary.api-key}")
    private String apiKey;
    @Value("${cloudinary.api-secret}")
    private String secretKey;

    @Bean
    public Cloudinary cloudinary(){
        Map<String, String> configCloud = new HashMap<>();
        configCloud.put("cloud_name", cloudName);
        configCloud.put("api_key", apiKey);
        configCloud.put("api_secret", secretKey);
        return new Cloudinary(configCloud);
    }

}
