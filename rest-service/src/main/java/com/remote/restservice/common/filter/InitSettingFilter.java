package com.remote.restservice.common.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Slf4j
@Component
public class InitSettingFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        if(((HttpServletRequest) request).getRequestURI().startsWith("/file")){
            chain.doFilter(request, response);
        }else{
            CachedBodyHttpServletWrapper cachedBodyHttpServletWrapper = new CachedBodyHttpServletWrapper(httpRequest);
            chain.doFilter(cachedBodyHttpServletWrapper, response);
        }
    }

    @Override
    public void destroy() {
    }
}