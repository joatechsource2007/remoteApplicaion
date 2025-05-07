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
     * SP NO : 331: wsp_Week_Meter_V1
     * @return
     * @throws SQLException
     */
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

    /**
     * SP NO : 332: wsp_Get_Meter_DETAIL
     * @return
     * @throws SQLException
     */
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

    /**
     * SP NO : 332: wsp_Get_Meter_GROUP
     * @return
     * @throws SQLException
     */
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

    @RequestMapping("/getMeterGroupApp")
    public CommonResponse getMeterGroupApp(@RequestParam Map<String,Object> params) {
        LOGGER.info("TankController.search() accepted on {}");
        try {
            return CommonResponse.builder()
                    .code("SUCCESS")
                    .status(HttpStatus.OK.value())
                    .message("거래처 정보 검침 일간,주간,월간 집계 Data")
                    .data(service.getMeterGroupApp(params))
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
     * SP NO : 333: wsp_MCUST_RCV_AVG
     * @return
     * @throws SQLException
     */
    @RequestMapping("/mcustRcvAvg")
    public CommonResponse mcustRcvAvg(@RequestParam Map<String,Object> params) {
        LOGGER.info("TankController.search() accepted on {}");
        try {
            return CommonResponse.builder()
                    .code("SUCCESS")
                    .status(HttpStatus.OK.value())
                    .message("검침 거래처 정보 , 최종검침")
                    .data(service.mcustRcvAvg(params))
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
