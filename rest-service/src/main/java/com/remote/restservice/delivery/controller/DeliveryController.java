package com.remote.restservice.delivery.controller;

import com.remote.restservice.common.CommonResponse;
import com.remote.restservice.delivery.service.DeliveryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.Map;

@RestController
@RequestMapping("/delivery")
public class DeliveryController {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeliveryController.class);

    private final DeliveryService service;

    public DeliveryController(DeliveryService service) {
        this.service = service;
    }


    /**
     * SP NO : 503	wsp_TANK_INSP_SELECT	탱크 정보 Select Display
     * @return
     * @throws SQLException
     */
    @RequestMapping("/tankInspSelect")
    public CommonResponse tankInspSelect(@RequestParam Map<String, Object> params) {
        LOGGER.info("TankController.search() accepted on {}", params);

        try {
            Map<String, Object> result = service.selectTankIN(params);

            return CommonResponse.builder()
                    .code("SUCCESS")
                    .status(HttpStatus.OK.value())
                    .message("탱크 정보 Select Display")
                    .data(result)
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
