package com.joa.remote.iamservice.service.impl;

import com.joa.remote.iamservice.common.core.security.AuthToken;
import com.joa.remote.iamservice.common.core.security.Role;
import com.joa.remote.iamservice.common.provider.security.JwtAuthTokenProvider;
import com.joa.remote.iamservice.database.*;
import com.joa.remote.iamservice.dto.UserRemoteInfo;
import com.joa.remote.iamservice.dto.UserSafeInfo;
import com.joa.remote.iamservice.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.jooq.tools.json.JSONArray;
import org.jooq.tools.json.JSONObject;
import javax.sql.DataSource;
import java.sql.*;
import java.time.*;
import java.util.*;
import java.util.Date;
import java.util.stream.IntStream;




@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final JwtAuthTokenProvider jwtAuthTokenProvider;
    private final static long LOGIN_RETENTION_MINUTES = 30;

    @Value("${joa.app.jwtExpirationMinute}")
    private int jwtExpirationMinute;

    @Value("${joa.app.jwtRefreshExpirationMinute}")
    private int jwtRefreshExpirationMinute;

    private static final Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);

    private final DataSource dataSource;

    @Autowired
    private DbHelper dbHelper;

    @Override
    public Optional<UserRemoteInfo> remotelogin(String UserPhone, String UserPass, String  RegLat, String RegLong, String AppVer, String TermIP) throws SQLException {

        UserRemoteInfo userRemoteInfo = executeRemoteLogin(UserPhone, UserPass, RegLat, RegLong, AppVer);
        return Optional.ofNullable(userRemoteInfo);
    }

    //TODO: 네이밍
    @Override
    public AuthToken createAuthToken(UserRemoteInfo userRemoteInfo) {
        Date expiredDate = Date.from(LocalDateTime.now().plusMinutes(jwtExpirationMinute).atZone(ZoneId.systemDefault()).toInstant());
        return jwtAuthTokenProvider.createAuthToken(userRemoteInfo.getUserPhone(), userRemoteInfo.getAppID(), expiredDate);
    }

    @Override
    public AuthToken createAuthToken(String UserPhone, String PngKind) {
        Date expiredDate = Date.from(LocalDateTime.now().plusMinutes(jwtExpirationMinute).atZone(ZoneId.systemDefault()).toInstant());
        return jwtAuthTokenProvider.createAuthToken(UserPhone, PngKind, expiredDate);
    }


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

    @Override
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

    @Override
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

    @Override
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

    @Override
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

    @Override
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

    @Override
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

    @Override
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
}
