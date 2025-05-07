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
     * 351 : wsp_Get_Customer_FIND : 전체거래처 리스트 조건초회 (A전체, T.탱크, C.용기, M 검침,   TC,TM,CM, TCM)
     * @return
     * @throws SQLException
     */
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
}
