package com.remote.restservice.meter.controller;

import com.remote.restservice.common.CommonResponse;
import com.remote.restservice.meter.service.MeterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.Map;

@RestController
@RequestMapping("/meter")
public class MeterController {
    private static final Logger LOGGER = LoggerFactory.getLogger(MeterController.class);

    private final MeterService service;

    public MeterController(MeterService service) {
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
                    .message("검침 주간 조회")
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

    @RequestMapping("/getMeterDetail")
    public CommonResponse getMeterDetail(@RequestParam Map<String,Object> params) {
        LOGGER.info("TankController.search() accepted on {}");
        try {
            return CommonResponse.builder()
                    .code("SUCCESS")
                    .status(HttpStatus.OK.value())
                    .message("검침 상세수신이력")
                    .data(service.getMeterDetail(params))
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

    @RequestMapping("/getMeterGroup")
    public CommonResponse getMeterGroup(@RequestParam Map<String,Object> params) {
        LOGGER.info("TankController.search() accepted on {}");
        try {
            return CommonResponse.builder()
                    .code("SUCCESS")
                    .status(HttpStatus.OK.value())
                    .message("검침 일간,주간,월간 집계 Data")
                    .data(service.getMeterGroup(params))
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
