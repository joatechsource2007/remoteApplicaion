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
     * SP NO : 311: wsp_Week_Tank_V1
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

    /**
     * SP NO : 312: wsp_TCust_RCV_Month
     * @return
     * @throws SQLException
     */
    @RequestMapping("/tcustRcvMonth")
    public CommonResponse tcustRcvMonth(@RequestParam Map<String,Object> params) {
        LOGGER.info("TankController.search() accepted on {}");
        try {
            return CommonResponse.builder()
                    .code("SUCCESS")
                    .status(HttpStatus.OK.value())
                    .message("APP 달력 일자별잔량, 최근충전일 ")
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

    /**
     * SP NO : 313: wsp_TCust_RCV_Detail
     * @return
     * @throws SQLException
     */
    @RequestMapping("/tcustRcvDetail")
    public CommonResponse tcustRcvDetail(@RequestParam Map<String,Object> params) {
        LOGGER.info("TankController.search() accepted on {}");
        try {
            return CommonResponse.builder()
                    .code("SUCCESS")
                    .status(HttpStatus.OK.value())
                    .message("탱크 거래처 상세 수신이력 (기간별 상세)")
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

    /**
     * SP NO : 314: wsp_TCust_RCV_Detail_V2
     * @return
     * @throws SQLException
     */
    @RequestMapping("/tcustRcvDetailV2")
    public CommonResponse tcustRcvDetailV2(@RequestParam Map<String,Object> params) {
        LOGGER.info("TankController.search() accepted on {}");
        try {
            return CommonResponse.builder()
                    .code("SUCCESS")
                    .status(HttpStatus.OK.value())
                    .message("탱크 거래처 수신이력(달력), 집계 포함")
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

    /**
     * SP NO : 315: wsp_CU_CHARGE_LIST
     * @return
     * @throws SQLException
     */
    @RequestMapping("/cuChargeList")
    public CommonResponse cuChargeList(@RequestParam Map<String,Object> params) {
        LOGGER.info("TankController.search() accepted on {}");
        try {
            return CommonResponse.builder()
                    .code("SUCCESS")
                    .status(HttpStatus.OK.value())
                    .message("탱크 거래처 상세 수신이력 (충전이력)")
                    .data(service.cuChargeList(params))
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
     * SP NO : 501	wsp_TANK_INSP_LIST	탱크 리스트 (master)
     * @return
     * @throws SQLException
     */
    @RequestMapping("/tankInspList")
    public CommonResponse tankInspList(@RequestParam Map<String,Object> params) {
        LOGGER.info("TankController.search() accepted on {}");
        try {
            return CommonResponse.builder()
                    .code("SUCCESS")
                    .status(HttpStatus.OK.value())
                    .message("탱크 거래처 상세 수신이력 (충전이력)")
                    .data(service.tankinsplist(params))
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
     * SP NO : 502	wsp_TANK_INSP_HIST	탱크 검사현황 (Detail)
     * @return
     * @throws SQLException
     */
    @RequestMapping("/tankInspHist")
    public CommonResponse tankInspHist(@RequestParam Map<String,Object> params) {
        LOGGER.info("TankController.search() accepted on {}");
        try {
            return CommonResponse.builder()
                    .code("SUCCESS")
                    .status(HttpStatus.OK.value())
                    .message("탱크 거래처 상세 수신이력 (충전이력)")
                    .data(service.tankinsphist(params))
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
     * SP NO : 503	wsp_TANK_INSP_SELECT	탱크 정보 Select Display
     * @return
     * @throws SQLException
     */
    @RequestMapping("/tankInspSelect")
    public CommonResponse tankInspSelect(@RequestParam Map<String,Object> params) {
        LOGGER.info("TankController.search() accepted on {}");
        try {
            return CommonResponse.builder()
                    .code("SUCCESS")
                    .status(HttpStatus.OK.value())
                    .message("탱크 정보 Select Display")
                    .data(service.tankinspselect(params))
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
