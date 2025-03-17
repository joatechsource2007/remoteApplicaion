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
     * OPT:60 화면초기화 컨트롤러
     * @return
     * @throws SQLException
     */
//    @RequestMapping("/init")
//    public CommonResponse init() {
//        LOGGER.info("TankController.init() accepted on {}");
//        try {
//            return CommonResponse.builder()
//                    .code("SUCCESS")
//                    .status(HttpStatus.OK.value())
//                    .message("충전소기본정보 초기화")
//                    .data(service.init())
//                    .build();
//        } catch (RuntimeException | SQLException e) {
//            return CommonResponse.builder()
//                    .code("FAIL")
//                    .status(HttpStatus.OK.value())
//                    .message(e.getMessage())
//                    .data(null)
//                    .build();
//        }
//    }


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




//    @PostMapping("/update")
//    public CommonResponse update(@RequestBody Map<String,Object> params) {
//        LOGGER.info("F11BM010Controller.update() accepted on {}");
//        try {
//            params.put("OPT", 20);
//            Map<String, Object> rtnMap = service.update(params);
//            params.put("OPT", 21);
//            rtnMap = service.update(params);
//
//            return CommonResponse.builder()
//                    .code("SUCCESS")
//                    .status(HttpStatus.OK.value())
//                    .message("충전소기본정보 수정")
//                    .data(rtnMap)
//                    .build();
//        } catch (RuntimeException | SQLException e) {
//            return CommonResponse.builder()
//                    .code("FAIL")
//                    .status(HttpStatus.OK.value())
//                    .message(e.getMessage())
//                    .data(null)
//                    .build();
//        }
//    }



//    @PostMapping("/insert")
//    public CommonResponse insert(@RequestBody Map<String,Object> params) {
//        LOGGER.info("F11BM010Controller.insert() accepted on {}");
//        try {
//            params.put("OPT", 10);
//            Map<String, Object> rtnMap = service.insert(params);
//            params.put("OPT", 11);
//            rtnMap = service.insert(params);
//
//            return CommonResponse.builder()
//                    .code("SUCCESS")
//                    .status(HttpStatus.OK.value())
//                    .message("충전소기본정보 저장")
//                    .data(rtnMap)
//                    .build();
//        } catch (RuntimeException | SQLException e) {
//            return CommonResponse.builder()
//                    .code("FAIL")
//                    .status(HttpStatus.OK.value())
//                    .message(e.getMessage())
//                    .data(null)
//                    .build();
//        }
//    }

}
