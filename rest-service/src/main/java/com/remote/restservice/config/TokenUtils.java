package com.remote.restservice.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Component
public class TokenUtils {

    private static String secret ="The_Quick_Brown_Fox_Jumps_Over_The_Lazy_Dog_By_Joa";
    private static final String AUTHORIZATION_HEADER = "Authorization";
    public static Optional<String> resolveToken(HttpServletRequest request) {
        String authToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(authToken)) {
            return Optional.of(authToken);
        } else {
            return Optional.empty();
        }
    }

    public static Claims getJwtContents(String jwt) {
        Claims claims = Jwts.parser()
                .setSigningKey(secret.getBytes())
                .parseClaimsJws(jwt).getBody();
        return claims;
    }
}
