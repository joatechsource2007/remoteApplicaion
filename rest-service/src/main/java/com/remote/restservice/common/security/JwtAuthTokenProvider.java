package com.remote.restservice.common.security;
import com.remote.restservice.common.core.security.AuthTokenProvider;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;

import java.security.Key;
import java.util.Date;

@Slf4j
public class JwtAuthTokenProvider implements AuthTokenProvider<JwtAuthToken> {

    private final Key key;

    public JwtAuthTokenProvider(String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    @Override
    public JwtAuthToken createAuthToken(String id, String role, Date expiredDate) {
        return new JwtAuthToken(id, role, expiredDate, key);
    }

    @Override
    public JwtAuthToken convertAuthToken(String token) {

        if (token.substring(0, "Bearer ".length()).equalsIgnoreCase("Bearer ")) {
            token = token.split(" ")[1].trim();
        }
        return new JwtAuthToken(token, key);
    }

    // 토큰 검증
    @Override
    public boolean validateToken(String token) {
        try {
            // Bearer 검증
            if (!token.substring(0, "Bearer ".length()).equalsIgnoreCase("Bearer ")) {
                return false;
            } else {
                token = token.split(" ")[1].trim();
            }
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            // 만료되었을 시 false
            return !claims.getBody().getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            throw new RuntimeException("토큰이 만료되었습니다.");
        } catch (UnsupportedJwtException e) {
            throw new RuntimeException("지원하지 않는 토큰형식입니다.");
        } catch (MalformedJwtException e) {
            throw new RuntimeException("토큰형식이 잘못되었습니다.");
        } catch (SignatureException e) {
            throw new RuntimeException("토큰서명검증에 실패하였습니다.");
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("토큰매개변수가 잘못되었습니다.");
        }
    }
}