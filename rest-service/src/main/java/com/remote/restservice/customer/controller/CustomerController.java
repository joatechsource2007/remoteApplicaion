package com.remote.restservice.customer.controller;

import com.remote.restservice.common.CommonResponse;
import com.remote.restservice.customer.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.Map;

@RestController
@RequestMapping("/customer")
public class CustomerController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerController.class);

    private final CustomerService service;

    public CustomerController(CustomerService service) {
        this.service = service;
    }

    /**
     * 351 : wsp_Get_Customer_FIND : 전체거래처 리스트 조건초회 (A전체, T.탱크, C.용기, M 검침,   TC,TM,CM, TCM)
     * @return
     * @throws SQLException
     */
    @RequestMapping("/find")
    public CommonResponse find(@RequestParam Map<String,Object> params) {
        LOGGER.info("CustomerController.search() accepted on {}");
        try {
            return CommonResponse.builder()
                    .code("SUCCESS")
                    .status(HttpStatus.OK.value())
                    .message("거래처 정보")
                    .data(service.find(params))
                    .build();
        } catch (RuntimeException | SQLException e) {
            return CommonResponse.builder()
                    .code("FAIL")
                    .status(HttpStatus.OK.value())
                    .message(e.getMessage())
                    .data(null)
                    .build();
        }
    }

    /**
     * 301	wsp	전체	wsp_CUST_INFO	거래처정보  (T,C,M) 탱크,용기,검침고객정보, 탱크리스트,검침리스트, 이미지 LIST
     * @return
     * @throws SQLException
     */
    @RequestMapping("/custinfo")
    public CommonResponse custinfo(@RequestParam Map<String,Object> params) {
        LOGGER.info("CustomerController.Cust Info accepted on {}");
        try {
            return CommonResponse.builder()
                    .code("SUCCESS")
                    .status(HttpStatus.OK.value())
                    .message("거래처 정보")
                    .data(service.custinfo(params))
                    .build();
        } catch (RuntimeException | SQLException e) {
            return CommonResponse.builder()
                    .code("FAIL")
                    .status(HttpStatus.OK.value())
                    .message(e.getMessage())
                    .data(null)
                    .build();
        }
    }

    /**
     * 401	wsp	전체	wsp_Select_Data	데이터 조회 (Select)
     * @return
     * @throws SQLException
     */
    @RequestMapping("/selectData")
    public CommonResponse selectData(@RequestParam Map<String,Object> params) {
        LOGGER.info("CustomerController.Select Data accepted on {}");
        try {
            return CommonResponse.builder()
                    .code("SUCCESS")
                    .status(HttpStatus.OK.value())
                    .message("테이블 데이터 정보")
                    .data(service.selectdata(params))
                    .build();
        } catch (RuntimeException | SQLException e) {
            return CommonResponse.builder()
                    .code("FAIL")
                    .status(HttpStatus.OK.value())
                    .message(e.getMessage())
                    .data(null)
                    .build();
        }
    }

    /**
     * 401	wsp	전체	wsp_Modify_Data	데이터 조회 (Select)
     * @return
     * @throws SQLException
     */
    @RequestMapping("/modifyData")
    public CommonResponse modifyData(@RequestParam Map<String,Object> params) {
        LOGGER.info("CustomerController.Cust Info accepted on {}");
        try {
            return CommonResponse.builder()
                    .code("SUCCESS")
                    .status(HttpStatus.OK.value())
                    .message("테이블 데이터 정보")
                    .data(service.modifydata(params))
                    .build();
        } catch (RuntimeException | SQLException e) {
            return CommonResponse.builder()
                    .code("FAIL")
                    .status(HttpStatus.OK.value())
                    .message(e.getMessage())
                    .data(null)
                    .build();
        }
    }

    /**
     * 402	wsp	전체		wsp_Generate_PK	Insert PK 항번생성
     * @return
     * @throws SQLException
     */
    @RequestMapping("/generatePK")
    public CommonResponse generatePK(@RequestParam Map<String,Object> params) {
        LOGGER.info("CustomerController.  generate  Pk accepted on {}");
        try {
            return CommonResponse.builder()
                    .code("SUCCESS")
                    .status(HttpStatus.OK.value())
                    .message("테이블 데이터 정보")
                    .data(service.generatepk(params))
                    .build();
        } catch (RuntimeException | SQLException e) {
            return CommonResponse.builder()
                    .code("FAIL")
                    .status(HttpStatus.OK.value())
                    .message(e.getMessage())
                    .data(null)
                    .build();
        }
    }

    /**
     * 300	wsp_Get_Base_Code	기초코드
     * @return
     * @throws SQLException
     */
    @RequestMapping("/getbasecode")
    public CommonResponse getbasecode(@RequestParam Map<String,Object> params) {
        LOGGER.info("CustomerController.BaseCode Info accepted on {}");
        try {
            return CommonResponse.builder()
                    .code("SUCCESS")
                    .status(HttpStatus.OK.value())
                    .message("기초 콤보 코드 리스트 ")
                    .data(service.getbasecode(params))
                    .build();
        } catch (RuntimeException | SQLException e) {
            return CommonResponse.builder()
                    .code("FAIL")
                    .status(HttpStatus.OK.value())
                    .message(e.getMessage())
                    .data(null)
                    .build();
        }
    }




}
