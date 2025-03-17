package com.joa.remote.iamservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LoginRequestDto {
    @JsonProperty
    private String UserID;
    @JsonProperty
    private int FSCode;
    @JsonProperty
    private String PassWord;
    @JsonProperty
    private String LoginGubun;
    @JsonProperty
    private String TermID;
    @JsonProperty
    private String TermIP;
    @JsonProperty
    private String LogOut;
}