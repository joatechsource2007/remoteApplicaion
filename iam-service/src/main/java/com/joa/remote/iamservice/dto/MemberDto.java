package com.joa.remote.iamservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.joa.remote.iamservice.common.core.security.Role;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.sql.Timestamp;
@Data
@Builder
public class MemberDto {

    @JsonProperty("cmngname")
    private String cmngname;              //업체명

    @JsonProperty("username")
    private String username;                //성명

    @JsonProperty("userposition")
    private String userposition;            //직위

    @JsonProperty("userphone")
    private String userphone;              //핸드폰

    @JsonProperty("useremail")
    private String useremail;               //전화번호(ID)

    @JsonProperty("userpass")
    private String userpass;                //이메일

    @JsonProperty("appver")
    private String appver;                  //비밀번호

    @JsonProperty("reglat")
    private String reglat;                 //앱버전

    @JsonProperty("reglong")
    private String reglong;                 //앱버전

    @JsonProperty("userno")
    private String userno;                 //앱버전


}
