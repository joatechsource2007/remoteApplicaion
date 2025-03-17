package com.remote.restservice.common;

import com.remote.restservice.common.security.AuthInterceptor;
import com.remote.restservice.config.HttpInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final AuthInterceptor authInterceptor;

    private final HttpInterceptor httpInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/*/*/init","/*/init","/init", "/file", "/file/*", "/file/*/*","/code");
        registry.addInterceptor(httpInterceptor)
                .order(2)
                .addPathPatterns("/**")
                .excludePathPatterns("/*/*/init","/*/init","/init","/user/menus/*/*", "/file", "/file/*", "/file/*/*","/code"); // 해당 경로는 인터셉터가 가로채지 않는다.
    }
}
