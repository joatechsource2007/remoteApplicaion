package com.joa.remote.iamservice.common.exception;

public class LoginFailedException extends RuntimeException {

    public LoginFailedException(){
        super(ErrorCode.LOGIN_FAILED.getMessage());
    }

    public LoginFailedException(String msg){
        super(msg);
    }
}
