package com.joa.remote.iamservice.web;


import com.joa.remote.iamservice.common.core.CommonResponse;
import com.joa.remote.iamservice.common.exception.LoginFailedException;
import com.joa.remote.iamservice.common.provider.security.JwtAuthToken;
import com.joa.remote.iamservice.common.utils.HttpServletUtils;
import com.joa.remote.iamservice.dto.LoginRemoteDto;
import com.joa.remote.iamservice.dto.SignUpRequestDto;
import com.joa.remote.iamservice.dto.UserRemoteInfo;
import com.joa.remote.iamservice.service.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // 컨트롤러에서 설정
public class LoginController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

    private final LoginService loginService;

    @RequestMapping("/status")
    public String status() {
        return "IAM-SERVICE IS ACTIVE";
    }

    @RequestMapping("/remotelogin")
    public CommonResponse remotelogin(@RequestBody LoginRemoteDto loginRemoteDTO, HttpServletRequest request) throws SQLException {

        String clientIp = HttpServletUtils.getClientIP(request);
        Optional<UserRemoteInfo> optionalUserRemoteInfo = null;
        String UserPhone= loginRemoteDTO.getUserPhone();
        String PrgKind = loginRemoteDTO.getPrgKind();
        String LogOut = loginRemoteDTO.getLogOut();


        if(LogOut.equals("") || LogOut.equals("N")) {

            //기접속확인
            Map<String, Object> refreshToken = loginService.findRefreshTokenByUserPhoneAndPrgKind(UserPhone, PrgKind);
            LOGGER.info("Already Login Info = {}", refreshToken);
            //이미접속한 ID가 있으면 기존접속해제 메세지 출력
            if (refreshToken.get("UserPhone") != null && ((String) refreshToken.get("UserPhone")).equals(UserPhone)) {
                return CommonResponse.builder()
                        .code("MULTI_LOGIN")
                        .status(200)
                        .message(String.format("이미 같은 아이디(%s)로 로그인한 기록이 있습니다.\n기존접속을 해제하고 접속하시겠습니까?", (String) refreshToken.get("UserPhone")))
                        .token("")
                        .refreshToken("")
                        .isAlreadyLogin("Y")
                        .data(null)
                        .build();
            }
        }
        //기존접속 제거작업
        if(LogOut.equals("Y")){
            loginService.deleteRefreshTokenByUserPhoneAndPrgKind(UserPhone,PrgKind);
        }


        try {
            optionalUserRemoteInfo = loginService.remotelogin(loginRemoteDTO.getUserPhone(),
                    loginRemoteDTO.getUserPass(),
                    loginRemoteDTO.getRegLat(),
                    loginRemoteDTO.getRegLong(),
                    loginRemoteDTO.getAppVer(),
                    clientIp
                    );

            System.out.println(optionalUserRemoteInfo);
            System.out.println(optionalUserRemoteInfo);
            System.out.println(optionalUserRemoteInfo);

        } catch (SQLException e) {
            throw new LoginFailedException(e.getMessage());
        }

        if (optionalUserRemoteInfo.isPresent()) {  //로그인 성공하면

            JwtAuthToken jwtAuthToken = (JwtAuthToken) loginService.createAuthToken(optionalUserRemoteInfo.get());
            return CommonResponse.builder()
                    .code("LOGIN_SUCCESS")
                    .status(200)
                    .message("로그인 성공")
                    .token(jwtAuthToken.getToken())   //Access 토큰생성
                    .refreshToken(loginService.createRefreshToken(loginRemoteDTO.getUserPhone(),clientIp, loginRemoteDTO.getPrgKind(), loginRemoteDTO.getOSKind()))  //RefreshToken 생성
                    .data(Map.of("UserRemoteInfo", optionalUserRemoteInfo.get()))
                    .build();

        } else {
            throw new LoginFailedException();
        }


    }


    @RequestMapping("/refreshToken")
    public CommonResponse verifyRefreshToken(@RequestBody Map<String,Object> params) throws SQLException{
        LOGGER.info("LoginController.verifyRefreshToken() accepted on {}", params);
        try {
            String refreshToken = params.get("refreshToken").toString();
            refreshToken = loginService.verifyRefreshTokenExpiry(refreshToken);
            String UserPhone = loginService.findUserPhoneByRefreshToken(refreshToken);
            String PrgKind = loginService.findPrgKindByRefreshToken(refreshToken);

            JwtAuthToken jwtAuthToken = (JwtAuthToken) loginService.createAuthToken(UserPhone, PrgKind);
            if(refreshToken!=null && jwtAuthToken !=null){
                return CommonResponse.builder()
                        .code("SUCCESS")
                        .status(HttpStatus.OK.value())
                        .message("토큰 재발급")
                        .token(jwtAuthToken.getToken())
                        .refreshToken(refreshToken)
                        .data(null)
                        .build();
            }else{
                return CommonResponse.builder()
                        .code("FAIL")
                        .status(HttpStatus.FORBIDDEN.value())
                        .message("토큰 재발급 실패(기간만료 등)")
                        .token(null)
                        .refreshToken(null)
                        .data(null)
                        .build();
            }
        } catch (RuntimeException | SQLException e) {
            return CommonResponse.builder()
                    .code("FAIL")
                    .status(HttpStatus.FORBIDDEN.value())
                    .token(null)
                    .refreshToken(null)
                    .message("토큰 재발급에 실패하였습니다.")
                    .data(null)
                    .build();
        }
    }

    @RequestMapping("/logout")
    public CommonResponse logout(@RequestBody LoginRemoteDto loginRemoteDTO) {
        LOGGER.info("UserController.logout() accepted on {}", loginRemoteDTO);
        try {
            //로그인 토큰 삭제
            loginService.deleteRefreshTokenByUserPhoneAndPrgKind(loginRemoteDTO.getUserPhone(), loginRemoteDTO.getPrgKind());

            //DB 로그아웃 프로시져
            //Map<String, Object> result = loginService.userLogOff(loginRemoteDTO.getUserPhone(), loginRemoteDTO.getPrgKind());
            return CommonResponse.builder()
                    .code("SUCCESS")
                    .status(HttpStatus.OK.value())
                    .message("로그아웃")
                    .data(null)
                    .build();

        } catch (SQLException e) {
            return CommonResponse.builder()
                    .code("FAIL")
                    .status(HttpStatus.OK.value())
                    .message(e.getMessage())
                    .data(null)
                    .build();
        }
    }

    @RequestMapping("/token/{refreshToken}")
    public CommonResponse refreshToken(@PathVariable String refreshToken) {
        LOGGER.info("UserController.refreshToken() accepted on {}", refreshToken);
        try {
            String UserPhone = loginService.findUserPhoneByRefreshToken(refreshToken);
            if(UserPhone==null){
                return CommonResponse.builder()
                        .code("SUCCESS")
                        .status(HttpStatus.FORBIDDEN.value())
                        .message("로그아웃됨")
                        .data(null)
                        .build();
            }else{
                return CommonResponse.builder()
                        .code("SUCCESS")
                        .status(HttpStatus.OK.value())
                        .message("정상적으로 로그인 중")
                        .data(null)
                        .build();
            }

        } catch (SQLException e) {
            return CommonResponse.builder()
                    .code("FAIL")
                    .status(HttpStatus.FORBIDDEN.value())
                    .message(e.getMessage())
                    .data(null)
                    .build();
        }
    }


    /**
     * todo: 회원가입
     * @param dto
     * @return
     */
    @PostMapping("/signup")
    public CommonResponse signup(@RequestBody SignUpRequestDto dto) {
        try {
            boolean success = loginService.registerUser(dto);
            if (success) {
                return CommonResponse.builder()
                        .code("SIGNUP_SUCCESS")
                        .status(200)
                        .message("회원가입 성공")
                        .data(null)
                        .build();
            } else {
                return CommonResponse.builder()
                        .code("SIGNUP_FAIL")
                        .status(500)
                        .message("회원가입 실패")
                        .data(null)
                        .build();
            }
        } catch (SQLException e) {
            return CommonResponse.builder()
                    .code("SIGNUP_ERROR")
                    .status(500)
                    .message("SQL 오류: " + e.getMessage())
                    .data(null)
                    .build();
        }
    }

}
