package com.remote.restservice.company.controller;

import com.remote.restservice.common.CommonResponse;
import com.remote.restservice.company.service.CompanyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.Map;

@RestController
@RequestMapping("/company")
public class CompanyController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CompanyController.class);

    private final CompanyService service;

    public CompanyController(CompanyService service) {
        this.service = service;
    }


    /**
     * OPT:60 화면초기화 컨트롤러
     * @return
     * @throws SQLException
     */
//    @RequestMapping("/init")
//    public CommonResponse init() {
//        LOGGER.info("CompanyController.init() accepted on {}");
//        try {
//            return CommonResponse.builder()
//                    .code("SUCCESS")
//                    .status(HttpStatus.OK.value())
//                    .message("거래처 초기화")
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
        LOGGER.info("CompanyController.search() accepted on {}");
        try {
            return CommonResponse.builder()
                    .code("SUCCESS")
                    .status(HttpStatus.OK.value())
                    .message("Company Info")
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
