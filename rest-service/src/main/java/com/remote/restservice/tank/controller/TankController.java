package com.remote.restservice.tank.controller;

import com.remote.restservice.common.CommonResponse;
import com.remote.restservice.tank.service.TankService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.Map;

@RestController
@RequestMapping("/tank")
public class TankController {
    private static final Logger LOGGER = LoggerFactory.getLogger(TankController.class);

    private final TankService service;

    public TankController(TankService service) {
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


    @RequestMapping("/search")
    public CommonResponse search(@RequestParam Map<String,Object> params) {
        LOGGER.info("TankController.search() accepted on {}");
        try {
            return CommonResponse.builder()
                    .code("SUCCESS")
                    .status(HttpStatus.OK.value())
                    .message("탱크 주간 조회")
                    .data(service.search(params))
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
    @RequestMapping("/tcustRcvMonth")
    public CommonResponse tcustRcvMonth(@RequestParam Map<String,Object> params) {
        LOGGER.info("TankController.search() accepted on {}");
        try {
            return CommonResponse.builder()
                    .code("SUCCESS")
                    .status(HttpStatus.OK.value())
                    .message("탱크 거래처 상세 수신 (달력표기, 최근충전일) ")
                    .data(service.tcustRcvMonth(params))
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

    @RequestMapping("/tcustRcvDetail")
    public CommonResponse tcustRcvDetail(@RequestParam Map<String,Object> params) {
        LOGGER.info("TankController.search() accepted on {}");
        try {
            return CommonResponse.builder()
                    .code("SUCCESS")
                    .status(HttpStatus.OK.value())
                    .message("탱크 주간 상세 조회")
                    .data(service.tcustRcvDetail(params))
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

    @RequestMapping("/tcustRcvDetailV2")
    public CommonResponse tcustRcvDetailV2(@RequestParam Map<String,Object> params) {
        LOGGER.info("TankController.search() accepted on {}");
        try {
            return CommonResponse.builder()
                    .code("SUCCESS")
                    .status(HttpStatus.OK.value())
                    .message("탱크 주간 월별 상세 조회")
                    .data(service.tcustRcvDetailV2(params))
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
