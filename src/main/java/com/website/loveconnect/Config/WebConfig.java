package com.website.loveconnect.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO;

@Configuration
@EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO) //config lỗi PageImpl Serialization Warning
public class WebConfig implements WebMvcConfigurer {

//    config cors cho toan bo ung dung, tat ca cac api
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // cho phép tất cả các api
                .allowedOrigins("http://127.0.0.1:5500") //cho phép fe
                .allowedMethods("GET", "POST", "PUT", "DELETE"); // các phương thức http được cho phép
    }
}
