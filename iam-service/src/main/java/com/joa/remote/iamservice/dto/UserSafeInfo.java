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
public class UserSafeInfo {

    @JsonProperty("UserNo")
    private String UserNo;              //사용자NO

    @JsonProperty("AppID")
    private String AppID;              //AppID

    @JsonProperty("AppSno")
    private String AppSno;              //AppSno

    @JsonProperty("AuthStatus")
    private String AuthStatus;           //AuthStatus

    @JsonProperty("UserPhone")
    private String UserPhone;           //UserPhone

    @JsonProperty("UserName")
    private String UserName;           //UserName

    @JsonProperty("CMmgNo")
    private String CMngNo;           //UserName

    @JsonProperty("CMngName")
    private String CMngName;           //UserName

    @JsonProperty("AreaCode")
    private String AreaCode;           //UserName

    @JsonProperty("AreaName")
    private String AreaName;           //UserName

    @JsonProperty("SwCd")
    private String SwCd;           //UserName

    @JsonProperty("SwName")
    private String SwName;           //UserName

    @JsonProperty("AppCert")
    private String AppCert;           //UserName

    @JsonProperty("UserEmail")
    private String UserEmail;           //UserName

    @JsonProperty("Role")
    private Role Role;

    @JsonProperty("CurrentDateTime")
    private Timestamp CurrentDateTime;  //현재 시각

    // Duck.class
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

}
