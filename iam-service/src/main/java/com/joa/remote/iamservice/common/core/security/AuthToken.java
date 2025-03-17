package com.joa.remote.iamservice.common.core.security;

public interface AuthToken<T> {
    boolean validate();
    T getData();
}