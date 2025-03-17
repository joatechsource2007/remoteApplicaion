package com.joa.remote.iamservice.service;

import com.joa.remote.iamservice.common.core.security.AuthToken;
import com.joa.remote.iamservice.dto.UserRemoteInfo;

import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;

public interface LoginService {

//    Optional<UserInfo> login(String UserId, int FSCode, String PassWord, String LoginGubun, String TermID, String TermIP) throws SQLException;

    Optional<UserRemoteInfo> remotelogin(String UserPhone,  String UserPass, String  RegLat, String RegLong, String AppVer, String PrgKind) throws SQLException;

    AuthToken createAuthToken(UserRemoteInfo userRemoteInfo);

    AuthToken createAuthToken(String UserPhone, String PrgKind);

//    Map<String, Object> check(String UserId) throws SQLException;

    String createRefreshToken(String UserPhone, String clientIp, String PrgKind, String OSKind) throws SQLException;

    public String verifyRefreshTokenExpiry(String token) throws SQLException;

    public int deleteRefreshTokenByUserPhoneAndPrgKind(String UserPhone, String PrgKind) throws SQLException;

    public String findUserPhoneByRefreshToken(String token) throws SQLException;

    public String findPrgKindByRefreshToken(String token) throws SQLException;

//  public Map<String, Object> findRefreshTokenByUserID(String UserID) throws SQLException;

    Map<String, Object> findRefreshTokenByUserPhoneAndPrgKind(String UserPhone, String PrgKind) throws SQLException;

    Map<String, Object> userLogOff(String UserID, String PrgKind) throws SQLException;

//    public Map<String, Object> changepassword(String UserID, String OldPassWord, String NewPassWord) throws SQLException;

 //   public Map<String, Object> initpassword(String UserID, String NewPassWord) throws SQLException;
}
