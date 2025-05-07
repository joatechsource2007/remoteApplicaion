package com.remote.restservice.transm.controller;

import com.remote.restservice.common.CommonResponse;
import com.remote.restservice.transm.service.TransmService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.Map;

@RestController
@RequestMapping("/transm")
public class TransmController {
    private static final Logger LOGGER = LoggerFactory.getLogger(TransmController.class);

    private final TransmService service;

    public TransmController(TransmService service) {
        this.service = service;
    }


    /**
     * SP NO :  504 WSP_TRANSM_LIST 발신기 관리 -리스트
     * @return
     * @throws SQLException
     */
    @RequestMapping("/transmlist")
    public CommonResponse transmlist(@RequestParam Map<String,Object> params) {
        LOGGER.info("TransmController.search() accepted on {}");
        try {
            return CommonResponse.builder()
                    .code("SUCCESS")
                    .status(HttpStatus.OK.value())
                    .message("발신기 관리 리스트")
                    .data(service.transmlist(params))
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
     * SP NO :  504 WSP_TRANSM_LIST 발신기 관리 -리스트
     * @return
     * @throws SQLException
     */
    @RequestMapping("/otherlist")
    public CommonResponse otherlist(@RequestParam Map<String,Object> params) {
        LOGGER.info("TransmController.search() accepted on {}");
        try {
            return CommonResponse.builder()
                    .code("SUCCESS")
                    .status(HttpStatus.OK.value())
                    .message("기초정로 리스트")
                    .data(service.otherlist(params))
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
