package com.remote.restservice.delivery.common;

import com.remote.restservice.common.CommonResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
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

    @RequestMapping("/jaegoList")
    public CommonResponse getJaegoList(@RequestParam("C_MNG_NO") String cMngNo,
                                       @RequestParam("JAE_LAST") String jaeLast) {
        LOGGER.info("getJaegoList() called with C_MNG_NO: {}, JAE_LAST: {}", cMngNo, jaeLast);

        try {
            List<Map<String, Object>> resultList = service.selectJaegoList(cMngNo, jaeLast);

            Map<String, Object> result = new HashMap<>();
            result.put("TABLE0", resultList);
            result.put("RowCount", resultList.size());

            return CommonResponse.builder()
                    .code("SUCCESS")
                    .status(HttpStatus.OK.value())
                    .message("재고 리스트 조회 성공")
                    .data(result)
                    .build();
        } catch (RuntimeException e) {
            return CommonResponse.builder()
                    .code("FAIL")
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(e.getMessage())
                    .data(null)
                    .build();
        }
    }




}
