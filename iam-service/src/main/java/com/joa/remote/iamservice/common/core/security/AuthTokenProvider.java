package com.joa.remote.iamservice.common.core.security;

import java.util.Date;

public interface AuthTokenProvider<T> {
    T createAuthToken(String UserPhone, String PrgKind, Date expiredDate);
    T convertAuthToken(String token);
}
