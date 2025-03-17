package com.joa.remote.iamservice.dto;


import com.joa.remote.iamservice.common.core.security.Role;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.sql.Timestamp;

@Data
@Builder
public class UserInfo {

    @JsonProperty("UserID")
    private String UserID;              //사용자아이디
    @JsonProperty("UserName")
    private String UserName;            //사용자이름
    @JsonProperty("EMail")
    private String EMail;               //Email주소
    @JsonProperty("DeptID")
    private String DeptID;              //부서코드
    @JsonProperty("DeptName")
    private String DeptName;            //부서명칭
    @JsonProperty("TitleID")
    private String TitleID;             //직책코드
    @JsonProperty("TitleName")
    private String TitleName;           //직책명칭
    @JsonProperty("GradeID")
    private String GradeID;             //직급코드
    @JsonProperty("GradeName")
    private String GradeName;           //직급명칭
    @JsonProperty("AccountUnitID")
    private String AccountUnitID;       //회계지역코드 -> 용역충전소
    @JsonProperty("AccountUnitNick")
    private String AccountUnitNick;     //회계지역Nick
    @JsonProperty("AccountUnitName")
    private String AccountUnitName;     //회계지역명칭
    @JsonProperty("StockArea")
    private String StockArea;           //입출하처지역(A, Y, C 등...)
    @JsonProperty("FSCode")
    private String FSCode;              //충전소코드
    @JsonProperty("FSName")
    private String FSName;              //충전소명칭
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
