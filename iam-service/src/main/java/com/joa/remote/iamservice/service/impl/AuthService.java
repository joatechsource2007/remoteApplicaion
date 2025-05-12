package com.joa.remote.iamservice.service.impl;

import com.joa.remote.iamservice.common.core.security.AuthToken;
import com.joa.remote.iamservice.common.core.security.Role;
import com.joa.remote.iamservice.common.provider.security.JwtAuthTokenProvider;
import com.joa.remote.iamservice.database.*;
import com.joa.remote.iamservice.dto.SignUpRequestDto;
import com.joa.remote.iamservice.dto.UserRemoteInfo;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.*;
import java.time.*;
import java.util.*;
import java.util.Date;


@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtAuthTokenProvider jwtAuthTokenProvider;
    private final static long LOGIN_RETENTION_MINUTES = 30;

    @Value("${joa.app.jwtExpirationMinute}")
    private int jwtExpirationMinute;

    @Value("${joa.app.jwtRefreshExpirationMinute}")
    private int jwtRefreshExpirationMinute;

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final DataSource dataSource;

    @Autowired
    private DbHelper dbHelper;



    //TODO: 네이밍
    public AuthToken createAuthToken(UserRemoteInfo userRemoteInfo) {
        Date expiredDate = Date.from(LocalDateTime.now().plusMinutes(jwtExpirationMinute).atZone(ZoneId.systemDefault()).toInstant());
        return jwtAuthTokenProvider.createAuthToken(userRemoteInfo.getUserPhone(), userRemoteInfo.getAppID(), expiredDate);
    }

    public AuthToken createAuthToken(String UserPhone, String PngKind) {
        Date expiredDate = Date.from(LocalDateTime.now().plusMinutes(jwtExpirationMinute).atZone(ZoneId.systemDefault()).toInstant());
        return jwtAuthTokenProvider.createAuthToken(UserPhone, PngKind, expiredDate);
    }


    /**
     * todo : 로그인하는 로직.!
     * @param UserPhone
     * @param UserPass
     * @param RegLat
     * @param RegLong
     * @param AppVer
     * @return
     * @throws SQLException
     */
    public UserRemoteInfo executeRemoteLogin(String UserPhone,  String UserPass,  String  RegLat, String RegLong, String AppVer) throws SQLException {

        Connection con = dataSource.getConnection();
        AutoSetAutoCommit sac = new AutoSetAutoCommit(con,false);
        AutoRollback tm = new AutoRollback(con);

        String sql = "{call wsp_EYE_LOGIN(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        logger.info("EXECUTED SQL : {}", sql);

        int opt = 50;
        CallableStatement cs =con.prepareCall(sql);
        cs.setInt(1,opt);                  // 1  @OPT_CD
        cs.setString(2,"");             // 2  @p_c_mng_name
        cs.setString(3,"");             // 3  @p_username
        cs.setString(4,"");             // 4  @p_userposition
        cs.setString(5,UserPhone);         // 5  @p_userphone
        cs.setString(6,"");             // 6  @p_useremail
        cs.setString(7,UserPass);          // 7  @p_userpass
        cs.setString(8,AppVer);            // 8  @p_appver
        cs.setString(9,RegLat);            // 9  @p_reg_lat
        cs.setString(10,RegLong);          // 10 @p_reg_long
        cs.setString(11,"");            // 11 @p_userno
        cs.registerOutParameter(12, Types.VARCHAR);
        cs.registerOutParameter(13, Types.TIMESTAMP);

        try (con; cs; sac; tm;) {
            boolean results = cs.execute();
            int count = cs.getUpdateCount();
            if (count >= 0) {
                 String userInfoString = (String) cs.getObject(12);
                Timestamp currentDateTime= (Timestamp)cs.getObject(13);
                if(userInfoString !=null){
                    String[] r = userInfoString.split("\\|", -1);
                    UserRemoteInfo userRemoteInfo = UserRemoteInfo.builder()
                            .UserNo(r[0])
                            .AppID(r[1])
                            .AppSno(r[2])
                            .AuthStatus(r[3])
                            .UserPhone(r[4])
                            .UserName(r[5])
                            .CMngNo(r[6])
                            .CMngName(r[7])
                            .AreaCode(r[8])
                            .AreaName(r[9])
                            .SwCd(r[10])
                            .SwName(r[11])
                            .AppCert(r[12])
                            .UserEmail(r[13])
                            .Role(Role.USER)
                            .CurrentDateTime(currentDateTime)
                            .build();
                    logger.info("UserRemoteInfo = {}",userRemoteInfo);
                    return userRemoteInfo;
                }else{
                    return null;
                }
            } else {
                System.out.println("No more results to process.");
                String userInfoString = (String) cs.getObject(12);
                Timestamp currentDateTime= (Timestamp)cs.getObject(13);
                if(userInfoString !=null){
                    String[] r = userInfoString.split("\\|", -1);
                    UserRemoteInfo userRemoteInfo = UserRemoteInfo.builder()
                            .UserNo(r[0])
                            .AppID(r[1])
                            .AppSno(r[2])
                            .AuthStatus(r[3])
                            .UserPhone(r[4])
                            .UserName(r[5])
                            .CMngNo(r[6])
                            .CMngName(r[7])
                            .AreaCode(r[8])
                            .AreaName(r[9])
                            .SwCd(r[10])
                            .SwName(r[11])
                            .AppCert(r[12])
                            .UserEmail(r[13])
                            .Role(Role.USER)
                            .CurrentDateTime(currentDateTime)
                            .build();
                    logger.info("UserRemoteInfo = {}",userRemoteInfo);
                    return userRemoteInfo;
                }
            }
            tm.commit();
        }
        catch (SQLException e) {
            throw e;
        }

        return null;

    }

    public String createRefreshToken(String UserPhone, String clientIp, String PrgKind, String OSKind) throws SQLException {
        Connection con = dataSource.getConnection();
        AutoSetAutoCommit sac = new AutoSetAutoCommit(con,false);
        AutoRollback tm = new AutoRollback(con);
        PreparedStatement stmt = con.prepareStatement("INSERT INTO CM_Refresh_Token (ExpiryDate, RefreshToken, UserPhone, IPAddress, PrgKind, OSKind) VALUES (DATEADD(ms, "+jwtRefreshExpirationMinute * 60 * 1000+", getdate()),?,?,?,?,?)");
        String token = UUID.randomUUID().toString();
        try (con; stmt; sac; tm) {
            stmt.setString(1, token);
            stmt.setString(2, UserPhone);
            stmt.setString(3, clientIp);
            stmt.setString(4, PrgKind);
            stmt.setString(5, OSKind);
            int count = stmt.executeUpdate();
            tm.commit();
            if(count >0 ) return token;
        }catch (SQLException e) {
            throw e;
        }

        return null;
    }

    public String verifyRefreshTokenExpiry(String token) throws SQLException {
        Connection con = dataSource.getConnection();
        AutoSetAutoCommit sac1 = new AutoSetAutoCommit(con,false);
        AutoRollback tm1 = new AutoRollback(con);
        PreparedStatement stmt = con.prepareStatement("SELECT ID, ExpiryDate, RefreshToken, UserPhone, IPAddress  FROM CM_Refresh_Token WHERE RefreshToken = ? ");
        try (con; stmt; sac1; tm1) {
            stmt.setString(1, token);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                Timestamp d =rs.getTimestamp("ExpiryDate");
                logger.info("d.toInstant() , Instant.now()  ,{}  {}", d.toInstant() , Instant.now());
                if (d.toInstant().compareTo(Instant.now()) < 0) {
                    Connection conn = dataSource.getConnection();
                    AutoSetAutoCommit sac = new AutoSetAutoCommit(conn,false);
                    AutoRollback tm = new AutoRollback(conn);
                    PreparedStatement stmt2 = conn.prepareStatement("DELETE FROM CM_Refresh_Token WHERE RefreshToken = ? ");
                    try (conn; stmt2; sac; tm) {
                        stmt2.setString(1, token);
                        int count = stmt2.executeUpdate();
                        tm.commit();
                        return null;
                    }catch (SQLException e) {
                        return null;
                    }
                }
            }
            tm1.commit();
        }catch (SQLException e) {
            return null;
        }

        return token;
    }

    public int deleteRefreshTokenByUserPhoneAndPrgKind(String UserPhone, String PrgKind)throws SQLException {
        Connection conn = dataSource.getConnection();
        AutoSetAutoCommit sac = new AutoSetAutoCommit(conn,false);
        AutoRollback tm = new AutoRollback(conn);
        PreparedStatement stmt2 = conn.prepareStatement("DELETE FROM CM_Refresh_Token WHERE UserPhone = ?   and  PrgKind = ?");
        int count = -1;
        try (conn; stmt2; sac; tm) {
            stmt2.setString(1, UserPhone);
            stmt2.setString(2, PrgKind);
            count = stmt2.executeUpdate();
            tm.commit();
            return count;
        }catch (SQLException e) {
            return -1;
        }finally {
            return count;
        }
    }

    public String findUserPhoneByRefreshToken(String token) throws SQLException{
        Connection con = dataSource.getConnection();
        AutoSetAutoCommit sac = new AutoSetAutoCommit(con,false);
        AutoRollback tm = new AutoRollback(con);
        PreparedStatement stmt = con.prepareStatement("SELECT ID, ExpiryDate, RefreshToken, UserPhone, IPAddress  FROM CM_Refresh_Token WHERE RefreshToken = ? ");
        try (con; stmt; sac; tm) {
            stmt.setString(1, token);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                String UserPhone = rs.getString("UserPhone");
                return UserPhone;
            }
            tm.commit();
        }catch (SQLException e) {
            return null;
        }

        return null;
    }

    public String findPrgKindByRefreshToken(String token) throws SQLException{
        Connection con = dataSource.getConnection();
        AutoSetAutoCommit sac = new AutoSetAutoCommit(con,false);
        AutoRollback tm = new AutoRollback(con);
        PreparedStatement stmt = con.prepareStatement("SELECT A.ID, A.ExpiryDate, A.RefreshToken, A.UserID, A.IPAddress, A.PrgKind  FROM CM_Refresh_Token A WHERE  A.RefreshToken = ?");
        try (con; stmt; sac; tm) {
            stmt.setString(1, token);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                String PrgKind = rs.getString("PrgKind");
                return PrgKind;
            }
            tm.commit();
        }catch (SQLException e) {
            return "";
        }
        return "";
    }

    public Map<String, Object> findRefreshTokenByUserPhoneAndPrgKind(String UserPhone, String PrgKind) throws SQLException{
        Connection con = dataSource.getConnection();
        AutoSetAutoCommit sac = new AutoSetAutoCommit(con,false);
        AutoRollback tm = new AutoRollback(con);
        Map<String, Object> result = new HashMap<String, Object>();
        PreparedStatement stmt = con.prepareStatement("SELECT ID, ExpiryDate, RefreshToken, UserPhone, IPAddress, PrgKind , OSKind FROM CM_Refresh_Token WHERE UserPhone = ? AND PrgKind=?");
        try (con; stmt; sac; tm) {
            stmt.setString(1, UserPhone);
            stmt.setString(2, PrgKind);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                result.put("UserPhone", rs.getString("UserPhone"));
                result.put("RefreshToken", rs.getString("RefreshToken"));
                result.put("IPAddress", rs.getString("IPAddress"));
                result.put("ID", rs.getLong("ID"));
                result.put("ExpiryDate", rs.getTimestamp("ExpiryDate"));
                result.put("PrgKind", rs.getString("PrgKind"));
                result.put("OSKind", rs.getString("OSKind"));
            }
            tm.commit();
        }catch (SQLException e) {
            return result;
        }
        return result;
    }

    public Map<String, Object> userLogOff(String UserPhone, String CMngNo) throws SQLException {
        List<SpParameter> listOfAllSpParameters = new ArrayList<SpParameter>();
        listOfAllSpParameters.add(SpParameter.builder().name("UserID").direction(SpParameter.Direction.INOUT).value(UserPhone).jdbcType(JDBCType.VARCHAR).build());
        listOfAllSpParameters.add(SpParameter.builder().name("FSCode").direction(SpParameter.Direction.IN).value(CMngNo)  .jdbcType(JDBCType.INTEGER).build());
        listOfAllSpParameters.add(SpParameter.builder().name("LoginGubun").direction(SpParameter.Direction.IN).value("X")  .jdbcType(JDBCType.CHAR).build());

        SpInfo spInfo3 = SpInfo.builder()
                .spName("usp_COMMON_Login_Web")
                .spParameterList(listOfAllSpParameters)
                .build();

        return dbHelper.execute(spInfo3);
    }


    /**
     * todo: 회우너등록!
     * @param dto
     * @return
     * @throws SQLException
     */
    public boolean registerUser(SignUpRequestDto dto) throws SQLException {
        Connection con = dataSource.getConnection();
        AutoSetAutoCommit sac = new AutoSetAutoCommit(con, false);
        AutoRollback tm = new AutoRollback(con);

        String userNoSql = "SELECT ISNULL(MAX(ReqID), 0) + 1 FROM SERVICE_USER";
        PreparedStatement stmtUserNo = con.prepareStatement(userNoSql);
        ResultSet rs = stmtUserNo.executeQuery();

        int newNumber = 1;
        if (rs.next()) {
            newNumber = rs.getInt(1);
        }

        String userNo = String.format("%010d", newNumber);
        String now = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());

        PreparedStatement stmt1 = con.prepareStatement(
                "INSERT INTO SERVICE_USER (UserNo, UserID, UserEmail, UserPhone, UserPass, " +
                        "AuthStatus, AuthID, AuthType, UserName, PositionName, RegDT, " +
                        "CompNo, CompName, CompZipCD, CompAddr1, CompAddr2, UserMemo, ServiceTYPE) " +
                        "VALUES (?, '', ?, ?, ?, 'Y', '', '0', ?, ?, ?, '', ?, '', '', '', '', 'GAS_EYE')");

        stmt1.setString(1, userNo);
        stmt1.setString(2, dto.getUserEmail()); // email 필요
        stmt1.setString(3, dto.getUserPhone());
        stmt1.setString(4, dto.getUserPass());
        stmt1.setString(5, dto.getUserName());
        stmt1.setString(6, dto.getUserPosition());
        stmt1.setString(7, now);
        stmt1.setString(8, dto.getCmngName());

        PreparedStatement stmt2 = con.prepareStatement(
                "INSERT INTO SERVICE_APP (UserNo, AppID, AppSno, AuthStatus, UserPhone, UserName, " +
                        "APP_TYPE, C_MNG_NO, C_MNG_NAME, UserPosition, UserEmail, UserPass, USE_Memo, Request_DT, " +
                        "APP_CERT, SVR_SQL_VER, SVR_IP, SVR_PORT, SVR_DBName, SVR_USER, SVR_PASS) " +
                        "VALUES (?, 'GAS_EYE', 0, 'Y', ?, ?, '', '', ?, ?, ?, ?, '', ?, '0000000000', '2022', " +
                        "'joainfo.dyndns.org', '2521', 'GasMax_EYE', 'GasMax_EYE', 'Gasmax_eye_pass')");

        stmt2.setString(1, userNo);
        stmt2.setString(2, dto.getUserPhone());
        stmt2.setString(3, dto.getUserName());
        stmt2.setString(4, dto.getCmngName());
        stmt2.setString(5, dto.getUserPosition());
        stmt2.setString(6, dto.getUserEmail());  // email 필요
        stmt2.setString(7, dto.getUserPass());
        stmt2.setString(8, now);

        try (con; stmtUserNo; rs; stmt1; stmt2; sac; tm) {
            stmt1.executeUpdate();
            stmt2.executeUpdate();
            tm.commit();
            return true;
        } catch (SQLException e) {
            throw e;
        }
    }


    /**
     * todo: fetch user info
     * @param userPhone
     * @param userPass
     * @param regLat
     * @param regLong
     * @param appVer
     * @param termIP
     * @return
     * @throws SQLException
     */
    public Optional<UserRemoteInfo> getUserInfo2(String userPhone, String userPass, String regLat, String regLong, String appVer, String termIP) throws SQLException {
        Connection con = dataSource.getConnection();
        AutoSetAutoCommit sac = new AutoSetAutoCommit(con, false);
        AutoRollback tm = new AutoRollback(con);

        String userPhoneNum = userPhone.replaceAll("[^0-9]", "");

        String sql = """
            SELECT 
                U.UserNo,
                A.AppID,
                CAST(A.AppSno AS VARCHAR) AS AppSno,
                A.AuthStatus,
                U.UserPhone,
                U.UserName,
                A.C_MNG_NO,
                A.C_MNG_NAME,
                A.Area_Code,
                A.Area_Name,
                A.SW_CD,
                A.SW_Name,
                A.APP_CERT,
                A.UserEmail,
                GETDATE() AS CurrentDateTime
            FROM SERVICE_USER U
            JOIN SERVICE_APP A ON U.UserNo = A.UserNo
            WHERE U.UserPhone = ?
              AND U.UserPass = ?
              AND U.AuthStatus = 'Y'
              AND A.AppID = 'GAS_EYE'
              AND A.AuthStatus = 'Y'
        """;

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, userPhoneNum);
            ps.setString(2, userPass);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                UserRemoteInfo info = UserRemoteInfo.builder()
                        .UserNo(rs.getString("UserNo"))
                        .AppID(rs.getString("AppID"))
                        .AppSno(rs.getString("AppSno"))
                        .AuthStatus(rs.getString("AuthStatus"))
                        .UserPhone(rs.getString("UserPhone"))
                        .UserName(rs.getString("UserName"))
                        .CMngNo(rs.getString("C_MNG_NO"))
                        .CMngName(rs.getString("C_MNG_NAME"))
                        .AreaCode(rs.getString("Area_Code"))
                        .AreaName(rs.getString("Area_Name"))
                        .SwCd(rs.getString("SW_CD"))
                        .SwName(rs.getString("SW_Name"))
                        .AppCert(rs.getString("APP_CERT"))
                        .UserEmail(rs.getString("UserEmail"))
                        .CurrentDateTime(rs.getTimestamp("CurrentDateTime"))
                        .Role(Role.USER)
                        .build();
                logger.info("✅ UserRemoteInfo retrieved via SQL: {}", info);
                tm.commit();
                return Optional.of(info);
            } else {
                logger.warn("❌ No matching user found for login.");
                return Optional.empty();
            }
        } catch (SQLException e) {
            logger.error("❌ SQL error during remotelogin(): {}", e.getMessage());
            throw e;
        } finally {
            con.close();
        }
    }




}
