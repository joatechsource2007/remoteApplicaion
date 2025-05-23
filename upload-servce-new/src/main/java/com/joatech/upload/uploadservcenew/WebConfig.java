package com.joatech.upload.uploadservcenew;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;



@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*") // ✅ 전체 Origin 허용
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(false);
    }
}
