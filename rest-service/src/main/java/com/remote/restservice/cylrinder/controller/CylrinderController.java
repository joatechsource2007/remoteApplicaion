package com.remote.restservice.cylrinder.controller;

import com.remote.restservice.common.CommonResponse;
import com.remote.restservice.cylrinder.service.CylrinderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.Map;

@RestController
@RequestMapping("/cylrinder")
public class CylrinderController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CylrinderController.class);

    private final CylrinderService service;

    public CylrinderController(CylrinderService service) {
        this.service = service;
    }


    /**
     * SP NO : 321: wsp_Week_Cylinder_V1
     * @return
     * @throws SQLException
     */
    @RequestMapping("/search")
    public CommonResponse search(@RequestParam Map<String,Object> params) {
        LOGGER.info("CylrinderController.search() accepted on {}");
        try {
            return CommonResponse.builder()
                    .code("SUCCESS")
                    .status(HttpStatus.OK.value())
                    .message("321 : 용기 주간수신 리스트")
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
     * SP NO : 321: wsp_Week_Cylinder_V2
     * @return
     * @throws SQLException
     */
    @RequestMapping("/searchV2")
    public CommonResponse searchV2(@RequestParam Map<String,Object> params) {
        LOGGER.info("CylrinderController.search() accepted on {}");
        try {
            return CommonResponse.builder()
                    .code("SUCCESS")
                    .status(HttpStatus.OK.value())
                    .message("321-2 : 집계건수 + 용기 주간수신 리스트")
                    .data(service.searchV2(params))
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
     * SP NO : 323: wsp_JCust_RCV_Detail_V2
     * @return
     * @throws SQLException
     */
    @RequestMapping("/jcustRcvDetail")
    public CommonResponse jcustRcvDetail(@RequestParam Map<String,Object> params) {
        LOGGER.info("CylrinderController.search() accepted on {}");
        try {
            return CommonResponse.builder()
                    .code("SUCCESS")
                    .status(HttpStatus.OK.value())
                    .message("용기 주간 조회")
                    .data(service.jcustRcvDetail(params))
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
     * SP NO : 324: wsp_JCust_RCV_Detail_V2
     * @return
     * @throws SQLException
     */
    @RequestMapping("/jcustRcvMonth")
    public CommonResponse jcustRcvMonth(@RequestParam Map<String,Object> params) {
        LOGGER.info("CylrinderController.search() accepted on {}");
        try {
            return CommonResponse.builder()
                    .code("SUCCESS")
                    .status(HttpStatus.OK.value())
                    .message("324 : 용기 거래처 수신이력(달력) +집계 포함")
                    .data(service.jcustRcvMonth(params))
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
