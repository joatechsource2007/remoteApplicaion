package com.remote.restservice.common;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
public class CommonResponse {

    private String message;
    private int status;
    private String code;

    private Map<String, Object> data;

}