package com.joa.remote.iamservice.common.core;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
public class CommonResponse {

    private String message;
    private int status;
    private String code;

    private String token;

    private String refreshToken;

    private Map<String, Object> data;

    private String isAlreadyLogin;

}