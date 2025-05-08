package com.remote.restservice.config;

import com.remote.restservice.common.exception.CustomAuthenticationException;
import com.remote.restservice.common.exception.CustomJwtRuntimeException;
import com.remote.restservice.common.filter.CachedBodyHttpServletWrapper;
import com.remote.restservice.common.security.JwtAuthToken;
import com.remote.restservice.common.security.JwtAuthTokenProvider;
import com.remote.restservice.utils.database.AutoRollback;
import com.remote.restservice.utils.database.AutoSetAutoCommit;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class HttpInterceptor implements HandlerInterceptor {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private final JwtAuthTokenProvider jwtAuthTokenProvider;

    private final DataSource dataSource;
    private static final String AUTHORIZATION_HEADER = "Authorization";

    @Value("${joa.app.jwtSecret}")
    private String secret;

    public Claims getJwtContents(String jwt) {
        Claims claims = Jwts.parser()
                .setSigningKey(secret.getBytes())
                .parseClaimsJws(jwt).getBody();
        return claims;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String method = request.getMethod();
        logger.info("[preHandle]:method URL: {}", method);
        String requestURI = request.getRequestURI();
        logger.info("[preHandle]:request URL: {}", requestURI);

        /* 토큰 유효성 검증 또는 슈퍼토큰 체크 */
        Optional<String> token = resolveToken(request);
        String tokenUserPhone = "";
        String tokenPrgKind = "";

        if (token.isPresent()) {
            String rawToken = token.get();

            // ✅ 슈퍼토큰 체크
            if ("supertoken1234".equals(rawToken) || "Bearer supertoken1234".equals(rawToken)) {
                logger.info("[preHandle]: 슈퍼토큰 인증 통과");
                return true;
            }

            JwtAuthToken jwtAuthToken = jwtAuthTokenProvider.convertAuthToken(rawToken);
            try {
                tokenUserPhone = jwtAuthToken.getData().getSubject();
                tokenPrgKind = String.valueOf(getJwtContents(rawToken.replaceAll("Bearer ", "")).get("PrgKind"));
                logger.info("[preHandle]:Userinfo : {},{}", tokenUserPhone, tokenPrgKind);
            } catch (Exception e) {
                throw new CustomJwtRuntimeException("사용자정보가 맞지않습니다.");
            }

        } else {
            throw new CustomJwtRuntimeException("인증토큰이 없습니다.");
        }

        return true;
    }



    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        String controllerName = handlerMethod.getBeanType().getSimpleName();
        String[] hm = handlerMethod.toString().split("#");
        int status = response.getStatus();
        String method = hm.length > 1 ? hm[1]:hm[0];
        String params =
                Collections.list(request.getParameterNames()).stream()
                        .map(p -> p + " : " + Arrays.asList( request.getParameterValues(p)) )
                        .collect(Collectors.joining(", "));

        String exc = ex != null ? ex.toString():"";
        String UserPhone ="";

        logger.info("[afterCompletion]:response status: {}", status);
        logger.info("[afterCompletion]:controller: {}", controllerName);
        logger.info("[afterCompletion]:method: {}", method);
        logger.info("[afterCompletion]:params: {}", params);
        logger.info("[afterCompletion]:exception: {}", exc);

        Optional<String> token = resolveToken(request);
        if (token.isPresent()) {
            JwtAuthToken jwtAuthToken = jwtAuthTokenProvider.convertAuthToken(token.get());
            UserPhone = jwtAuthToken.getData().getSubject();
            logger.info("[afterCompletion]:UserPhone: {}",UserPhone);
        }

        //Connection con = dataSource.getConnection();
        //AutoSetAutoCommit sac = new AutoSetAutoCommit(con,false);
        //AutoRollback tm = new AutoRollback(con);
        //PreparedStatement stmt = con.prepareStatement("INSERT INTO LPIS_Web_Log (ResStatus, Controller,Method,Params,UserPhone,EX) VALUES (?,?,?,?,?,?)");

        //try (con; stmt; sac; tm) {
        //    stmt.setInt(1,status);
        //    stmt.setString(2,controllerName);
        //    stmt.setString(3,method);
        //    stmt.setString(4,params.toString());
        //    stmt.setString(5,UserPhone);
        //   stmt.setString(6,exc);
        //    int count = stmt.executeUpdate();
        //    tm.commit();
        //}catch (SQLException e) {
        //        logger.info("Remote_Web_Log 저장시 오류가 발생하였습니다.");
        //        throw e;
        //}
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
