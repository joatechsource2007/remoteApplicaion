package com.joa.remote.iamservice.dto;

import lombok.Data;

@Data
public class SignUpRequestDto {
    private String userPhone;
    private String userPass;
    private String userName;
    private String userPosition;
    private String cmngName;  // 이 이름 유지 시, 서
    private String userEmail; // 이메일 추가
}
