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
    private static final String SUPER_TOKEN = "supertoken1234";

    @Override
    public boolean preHandle(HttpServletRequest servletRequest, HttpServletResponse servletResponse, Object handler) {

        log.info("AuthInterceptor.preHandle!!");
        String uri = servletRequest.getRequestURI();

        // ✅ 인증 제외 엔드포인트 목록
        if (uri.startsWith("/file") ||
                uri.equals("/v1/signup") ||
                uri.equals("/v1/status") ||
                uri.equals("/v1/remotelogin")) {
            return true;
        }

        Optional<String> token = resolveToken(servletRequest);
        if (token.isPresent()) {
            String rawToken = token.get();

            // ✅ 슈퍼토큰이면 통과
            if (SUPER_TOKEN.equals(rawToken)) {
                log.info("슈퍼토큰으로 인증 통과");
                return true;
            }

            JwtAuthToken jwtAuthToken = jwtAuthTokenProvider.convertAuthToken(rawToken);
            try {
                if (jwtAuthToken.validate()) {
                    return true;
                }
            } catch (CustomJwtRuntimeException e) {
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
