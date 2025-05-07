package com.remote.restservice.config;

import com.remote.restservice.common.exception.CustomAuthenticationException;
import com.remote.restservice.common.exception.CustomJwtRuntimeException;
import com.remote.restservice.common.filter.CachedBodyHttpServletWrapper;
import com.remote.restservice.common.security.JwtAuthToken;
import com.remote.restservice.common.security.JwtAuthTokenProvider;
import com.remote.restservice.utils.database.AutoRollback;
import com.remote.restservice.utils.database.AutoSetAutoCommit;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class HttpInterceptor implements HandlerInterceptor {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private final JwtAuthTokenProvider jwtAuthTokenProvider;

    private final DataSource dataSource;
    private static final String AUTHORIZATION_HEADER = "Authorization";

    @Value("${joa.app.jwtSecret}")
    private String secret;

    public Claims getJwtContents(String jwt) {
        Claims claims = Jwts.parser()
                .setSigningKey(secret.getBytes())
                .parseClaimsJws(jwt).getBody();
        return claims;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String method = request.getMethod();
        logger.info("[preHandle]:method URL: {}", method);
        String requestURI = request.getRequestURI();
        logger.info("[preHandle]:request URL: {}", requestURI);

        /* 토큰유효성 검증 토큰에 UserID, FSCode 존재하는지*/
        Optional<String> token = resolveToken(request);
        String tokenUserPhone ="";
        String tokenPrgKind = "";
        if (token.isPresent()) {
            JwtAuthToken jwtAuthToken = jwtAuthTokenProvider.convertAuthToken(token.get());
            try {
                tokenUserPhone = jwtAuthToken.getData().getSubject();
                tokenPrgKind =String.valueOf(getJwtContents(token.get().replaceAll("Bearer ","")).get("PrgKind"));
                logger.info("[preHandle]:Userinfor : {},{}",tokenUserPhone, tokenPrgKind);
            }catch(Exception e){
                throw new CustomJwtRuntimeException("사용자정보가 맞지않습니다.");
            }
        }else{ //토큰 없으면 에러
            throw new CustomJwtRuntimeException("인증토큰이 없습니다.");
        }

//        if("GET".equals(method)){   //GET방식이면
//            if(requestURI.startsWith("/file/") //파일관련
//                    || requestURI.startsWith("/approve/") //approve
//                    || requestURI.startsWith("/code/") //code
//                    || requestURI.startsWith("/c00li020/") //메뉴얼
//                    || requestURI.startsWith("/c11cd020/") //입금종류
//                    || requestURI.startsWith("/c11cf025/") // 충전소직무조회
//                    || requestURI.startsWith("/c11cd030/") //지급종류
//                    || requestURI.startsWith("/c11cd040/") //대체종류
//                    || requestURI.startsWith("/f11pm070/") //국내매입단가관리
//                    || requestURI.startsWith("/f21ca030/") //일일시재현황
//                    || requestURI.startsWith("/f24ac010/") //계정과목코드
//                    || requestURI.startsWith("/f24ac020/") //제무제표코드ㅗ
//                    || requestURI.startsWith("/f24ts015/") //대체종류
//                    || requestURI.startsWith("/f25ta010/") //매출세금계산서발행
//                    || requestURI.startsWith("/f25ta020/") //매출세금계산서수기발행
//                    || requestURI.startsWith("/f24ae010/") //매출자료회계반영
//                    || requestURI.startsWith("/f24ca010/") //매출원가
//                    || requestURI.startsWith("/f28rm030/") //채권채무확인의뢰서
//                    || requestURI.startsWith("/f27cm040/") //경영실적보고서
//                    || requestURI.startsWith("/f11bm010/") //충전소기본정보
//            ){ //파일관련, QnA URL 우회
//                logger.info("GET METHOD 우회경로요청");
//                return true;
//            }

//            if(!"c49999".equalsIgnoreCase(tokenUserPhone) || tokenPrgKind != 100){
//                //파라미터 검색
//                logger.info("사용자 GET METHOD 요청- 파라미터 검증 필요");
//                Enumeration eParam = request.getParameterNames();
//
//                String pUserNo = request.getParameter("QueryUserID") !=null ? request.getParameter("QueryUserID"): "";
//                String pGPSLat = request.getParameter("UserID") !=null ? request.getParameter("UserID"): "";
//                String pGPSLat = request.getParameter("UserID") !=null ? request.getParameter("UserID"): "";
//
//
//                int pQueryFSCode = request.getParameter("QueryFSCode") !=null ? Integer.valueOf(request.getParameter("QueryFSCode")): 0;
//                int pFSCode = request.getParameter("FSCode") !=null ? Integer.valueOf(request.getParameter("FSCode")): 0;
//                String pQueryUserID = request.getParameter("QueryUserID") !=null ? request.getParameter("QueryUserID"): "";
//                String pUserID = request.getParameter("UserID") !=null ? request.getParameter("UserID"): "";
//                int pLoginFSCode = request.getParameter("LoginFSCode") !=null ? Integer.valueOf(request.getParameter("LoginFSCode")): 0;
//                String pLoginUserID = request.getParameter("LoginUserID") !=null ? request.getParameter("LoginUserID"): "";
//
//                if(pQueryFSCode==0 && pFSCode==0 && pLoginFSCode==0 && pQueryUserID.equals("") && pUserID.equals("") && pLoginUserID.equals("")){
//                    //6개 파라미터 중 모두 안들어오면 오류 발생---> 필요파라미터가 전달되지 않음.
//                    logger.info("GET METHOD:6개 파라미터 중 모두 안들어오면 오류 발생---> 필요파라미터가 전달되지 않음");
//                    throw new CustomAuthenticationException("사용자인증실패");
//                }
//
//                String requestURL = request.getRequestURI();
//                logger.info("preHandle.requestURL = {}",requestURL);
//
//                //파라미터 변조 확인
//                while (eParam.hasMoreElements()) {
//                    String pName = (String)eParam.nextElement();
//                    String pValue = request.getParameter(pName);
//                    logger.info("preHandle.parameter = {}, value ={} ",pName, pValue);
//                    if(pName.equals("UserID")){
//                        if(!tokenUserPhone.equals(pValue)){ //사용자가 다른 UserID 사용
//                            logger.info("사용자가 다른 UserID 사용");
//                            throw new CustomAuthenticationException("사용자인증실패");
//                        }
//                    }
//                    if(pName.equals("QueryUserID")){
//                        if(!tokenUserPhone.equals(pValue)){ //사용자가 다른 QueryUserID 사용
//                            logger.info("사용자가 다른 QueryUserID 사용");
//                            throw new CustomAuthenticationException("사용자인증실패");
//                        }
//                    }
//                    if(pName.equals("LoginUserID")){
//                        if(!tokenUserPhone.equals(pValue)){ //사용자가 다른 QueryUserID 사용
//                            logger.info("사용자가 다른 LoginUserID 사용");
//                            throw new CustomAuthenticationException("사용자인증실패");
//                        }
//                    }
//                    if(pName.equals("QueryFSCode")){
//                        if(tokenFSCode != Integer.valueOf(pValue)){ //사용자가 다른 QueryFSCode 사용
//                            logger.info("사용자가 다른 QueryFSCode 사용");
//                            throw new CustomAuthenticationException("사용자인증실패");
//                        }
//                    }
//                    if(pName.equals("FSCode")){
//                        if(tokenFSCode != Integer.valueOf(pValue)){ //사용자가 다른 FSCode 사용
//                            logger.info("사용자가 다른 FSCode 사용");
//                            throw new CustomAuthenticationException("사용자인증실패");
//                        }
//                    }
//                    if(pName.equals("LoginFSCode")){
//                        if(tokenFSCode != Integer.valueOf(pValue)){ //사용자가 다른 FSCode 사용
//                            logger.info("사용자가 다른 LoginFSCode 사용");
//                            throw new CustomAuthenticationException("사용자인증실패");
//                        }
//                    }
//                }
//            }
//        }else if("POST".equals(method)){
//            if("c49999".equalsIgnoreCase(tokenUserPhone) && tokenFSCode == 100){
//                logger.info("관리자 우회경로요청 ");
//                return true;
//            }
//
//            if(requestURI.startsWith("/file/") //파일관련
//                    || requestURI.startsWith("/c11cd020/") //
//                    || requestURI.startsWith("/c11cd030/") //
//                    || requestURI.startsWith("/c11cd040/") //
//                    || requestURI.startsWith("/c11cf050/") //
//                    || requestURI.startsWith("/c11fm020/") //
//                    || requestURI.startsWith("/f25ta010/") //매출세금계산서발행
//                    || requestURI.startsWith("/f25ta020/") //매출세금계산서수기발행
//                    || requestURI.startsWith("/f11bm040/") //임대료관리
//                    || requestURI.startsWith("/c11fm040/") //직무할당
//            ){ //파일관련, QnA URL 우회
//                logger.info("POST 우회경로요청");
//                return true;
//            }
//
//            if (request.getContentType() != null && request.getContentType().contains("application/json")) {
//                final CachedBodyHttpServletWrapper cachingRequest = (CachedBodyHttpServletWrapper) request;
//                String requestBody;
//                if ((requestBody = cachingRequest.getReader().readLine()) != null) {
//                    logger.info("[preHandle]:requestBody: {}",requestBody);
//                    int jQueryFSCode = 0;
//                    int jFSCode = 0;
//                    String jQueryUserID="";
//                    String jUserID="";
//                    String jUserID2="";
//                    JSONObject jsonObject = new JSONObject(requestBody);
//                    try{
//                        jQueryFSCode = jsonObject.getInt("QueryFSCode");
//                    }catch(Exception e){logger.info("QueryFSCode 없음");}
//                    try{
//                        jFSCode = jsonObject.getInt("FSCode");
//                    }catch(Exception e){logger.info("FSCode 없음");}
//                    try{
//                        jQueryUserID = jsonObject.getString("QueryUserID");
//                    }catch(Exception e){logger.info("QueryUserID 없음");}
//                    try{
//                        jUserID = jsonObject.getString("UserID");
//                    }catch(Exception e){logger.info("UserID 없음");}
//                    try{
//                        jUserID2 = jsonObject.getString("UserID2");
//                    }catch(Exception e){logger.info("UserID2 없음");}
//
//                    logger.info("[preHandle.JsonParsed]:QueryFSCode: {},FSCode:{},,QueryUserID:{},UserID:{},UserID2:{}",jQueryFSCode, jFSCode,jQueryUserID,jUserID,jUserID2);
//                    if(jQueryFSCode==0 && jFSCode==0 && jQueryUserID.equals("") && jUserID.equals("") && jUserID2.equals("")){
//                        //5개 파라미터 중 모두 안들어오면 오류 발생---> 필요파라미터가 전달되지 않음.
//                        logger.info("5개 파라미터 중 모두 안들어오면 오류 발생---> 필요파라미터가 전달되지 않음");
//                        throw new CustomAuthenticationException("사용자인증실패");
//
//                    }else{
//                        if(requestURI.startsWith("/f11bm020/")){    //예외처리 URL--> 다른값 검증
//                            if(jUserID2.equals("") || !jUserID2.equals(tokenUserPhone)){
//                                logger.info("UserID2가 없거나  tokenUserID 와 다름");
//                                throw new CustomAuthenticationException("사용자인증실패");
//                            }
//                        }else{
//                            if(jQueryFSCode !=0 && jQueryFSCode!=tokenFSCode){ //파라미터값과 토큰값이 다름.
//                                logger.info("QueryFSCode 가 없거나  tokenFSCode 와 다름");
//                                throw new CustomAuthenticationException("사용자인증실패");
//                            }
//                            if(jFSCode !=0 && jFSCode!=tokenFSCode){ //파라미터값과 토큰값이 다름.
//                                logger.info("FSCode 가 없거나  tokenFSCode 와 다름");
//                                throw new CustomAuthenticationException("사용자인증실패");
//                            }
//                            if(!jQueryUserID.equals("") && !jQueryUserID.equals(tokenUserPhone)){ //파라미터값과 토큰값이 다름.
//                                logger.info("QueryUserID 가 없거나  tokenUserID 와 다름");
//                                throw new CustomAuthenticationException("사용자인증실패");
//                            }
//                            if(!jUserID.equals("") && !jUserID.equals(tokenUserPhone)){ //파라미터값과 토큰값이 다름.
//                                logger.info("UserID 가 없거나  tokenUserID 와 다름");
//                                throw new CustomAuthenticationException("사용자인증실패");
//                            }
//                        }
//                    }
//                }
//            }
//        }

        return true;
    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        String controllerName = handlerMethod.getBeanType().getSimpleName();
        String[] hm = handlerMethod.toString().split("#");
        int status = response.getStatus();
        String method = hm.length > 1 ? hm[1]:hm[0];
        String params =
                Collections.list(request.getParameterNames()).stream()
                        .map(p -> p + " : " + Arrays.asList( request.getParameterValues(p)) )
                        .collect(Collectors.joining(", "));

        String exc = ex != null ? ex.toString():"";
        String UserPhone ="";

        logger.info("[afterCompletion]:response status: {}", status);
        logger.info("[afterCompletion]:controller: {}", controllerName);
        logger.info("[afterCompletion]:method: {}", method);
        logger.info("[afterCompletion]:params: {}", params);
        logger.info("[afterCompletion]:exception: {}", exc);

        Optional<String> token = resolveToken(request);
        if (token.isPresent()) {
            JwtAuthToken jwtAuthToken = jwtAuthTokenProvider.convertAuthToken(token.get());
            UserPhone = jwtAuthToken.getData().getSubject();
            logger.info("[afterCompletion]:UserPhone: {}",UserPhone);
        }

        //Connection con = dataSource.getConnection();
        //AutoSetAutoCommit sac = new AutoSetAutoCommit(con,false);
        //AutoRollback tm = new AutoRollback(con);
        //PreparedStatement stmt = con.prepareStatement("INSERT INTO LPIS_Web_Log (ResStatus, Controller,Method,Params,UserPhone,EX) VALUES (?,?,?,?,?,?)");

        //try (con; stmt; sac; tm) {
        //    stmt.setInt(1,status);
        //    stmt.setString(2,controllerName);
        //    stmt.setString(3,method);
        //    stmt.setString(4,params.toString());
        //    stmt.setString(5,UserPhone);
        //   stmt.setString(6,exc);
        //    int count = stmt.executeUpdate();
        //    tm.commit();
        //}catch (SQLException e) {
        //        logger.info("Remote_Web_Log 저장시 오류가 발생하였습니다.");
        //        throw e;
        //}
    }

    private Optional<String> resolveToken(HttpServletRequest request) {
        String authToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(authToken)) {
            return Optional.of(authToken);
        } else {
            return Optional.empty();
        }
    }
}