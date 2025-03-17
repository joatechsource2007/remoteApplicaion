package com.remote.restservice.common.core.security;

import com.remote.restservice.common.exception.CustomJwtRuntimeException;

public interface AuthToken<T> {
    boolean validate();
    T getData() throws CustomJwtRuntimeException;
}