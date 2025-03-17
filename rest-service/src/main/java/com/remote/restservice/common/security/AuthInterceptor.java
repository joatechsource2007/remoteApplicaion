package com.remote.restservice.common.security;

import com.remote.restservice.common.exception.CustomJwtRuntimeException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

    private final JwtAuthTokenProvider jwtAuthTokenProvider;
    private static final String AUTHORIZATION_HEADER = "Authorization";

    @Override
    public boolean preHandle(HttpServletRequest servletRequest, HttpServletResponse servletResponse, Object handler) {

        log.info("AuthInterceptor.preHandle!!");
        if(servletRequest.getRequestURI().startsWith("/file")){
            return true;
        }

        Optional<String> token = resolveToken(servletRequest);
        if (token.isPresent()) {
            JwtAuthToken jwtAuthToken = jwtAuthTokenProvider.convertAuthToken(token.get());
            try{
                if(jwtAuthToken.validate()) {
                    return true;
                }
            }catch(CustomJwtRuntimeException e){
                throw new CustomJwtRuntimeException(e.getMessage());
            }
        } else {
            throw new CustomJwtRuntimeException("인증토큰이 없습니다. preHandle");
        }
        return true;
    }

    private Optional<String> resolveToken(HttpServletRequest request) {
        String authToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(authToken)) {
            return Optional.of(authToken);
        } else {
            return Optional.empty();
        }
    }
}