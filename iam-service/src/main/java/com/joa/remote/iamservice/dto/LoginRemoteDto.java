package com.joa.remote.iamservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LoginRemoteDto {

    @JsonProperty
    private String UserPhone;

    @JsonProperty
    private String UserName;

    @JsonProperty
    private String UserPass;

    @JsonProperty
    private String AppVer;

    @JsonProperty
    private String RegLat;

    @JsonProperty
    private String RegLong;

    @JsonProperty
    private String PrgKind;

    @JsonProperty
    private String OSKind;

    @JsonProperty
    private String LogOut;

}