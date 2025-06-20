package com.remote.restservice.dashboard.controller;

import com.remote.restservice.common.CommonResponse;
import com.remote.restservice.dashboard.service.DashBoardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.Map;

@RestController
@RequestMapping("/Dashboard")
public class DashboardController {
    private static final Logger LOGGER = LoggerFactory.getLogger(DashboardController.class);

    private final DashBoardService service;

    public DashboardController(DashBoardService service) {
        this.service = service;
    }


    /**
     * SP NO : A102: wsp_Get_CAR_Driver_SW
     * @return
     * @throws SQLException
     */
    @RequestMapping("/cardriver")
    public CommonResponse cardriver(@RequestParam Map<String,Object> params) {
        LOGGER.info("TankController.search() accepted on {}");
        try {
            return CommonResponse.builder()
                    .code("SUCCESS")
                    .status(HttpStatus.OK.value())
                    .message("검침 주간 조회")
                    .data(service.cardriver(params))
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
     * SP NO : A102: wsp_Get_Tank_LastData_Summary
     * @return
     * @throws SQLException
     */
    @RequestMapping("/tanksummary")
    public CommonResponse tanksummary(@RequestParam Map<String,Object> params) {
        LOGGER.info("TankController.search() accepted on {}");
        try {
            return CommonResponse.builder()
                    .code("SUCCESS")
                    .status(HttpStatus.OK.value())
                    .message("검침 상세수신이력")
                    .data(service.tanksummary(params))
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
