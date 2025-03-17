package com.remote.restservice.common.exception;

public class CustomJwtRuntimeException extends RuntimeException {

    public CustomJwtRuntimeException(){
        super(ErrorCode.INVALID_JWT_TOKEN.getMessage());
    }

    public CustomJwtRuntimeException(Exception ex){
        super(ex);
    }
    public CustomJwtRuntimeException(String message){
        super(message);
    }
}
