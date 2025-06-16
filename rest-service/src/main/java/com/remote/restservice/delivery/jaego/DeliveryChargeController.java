package com.remote.restservice.delivery.jaego;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/charge")
@RequiredArgsConstructor
@Slf4j
public class DeliveryChargeController {

    private final JdbcTemplate jdbc;

    @GetMapping("/delivery")
    public List<Map<String, Object>> getDeliveryCharge(
            @RequestParam String cMngNo,
            @RequestParam String userNo,
            @RequestParam String date,
            @RequestParam(required = false) String carCode,
            @RequestParam(required = false) String swCode,
            @RequestParam(required = false, defaultValue = "") String findStr
    ) {
        log.info("📦 충전관리 조회 요청 → cMngNo={}, userNo={}, date={}, carCode={}, swCode={}, findStr={}",
                cMngNo, userNo, date, carCode, swCode, findStr);

        String sql = "EXEC dbo.wsp_TANK_UB_DELIVERY_USERNO " +
                "@p_C_MNG_NO = ?, " +
                "@p_USERNO = ?, " +
                "@p_DATE = ?, " +
                "@p_CAR_CODE = ?, " +
                "@p_SW_CODE = ?, " +
                "@p_FIND_STR = ?";

        List<Map<String, Object>> result = jdbc.queryForList(sql,
                cMngNo,
                userNo,
                date,
                carCode != null ? carCode : "",
                swCode != null ? swCode : "",
                findStr);

        log.info("📊 충전관리 조회 결과 {}건", result.size());
        for (Map<String, Object> row : result) {
            log.info("🔸 결과 Row: {}", row);
        }

        return result;
    }

    /**
     * 배송 지시 목록 조회
     */
    @GetMapping("/delivery-request")
    public List<Map<String, Object>> getDeliveryRequests(
            @RequestParam String cMngNo,
            @RequestParam String userNo,
            @RequestParam String date
    ) {
        log.info("📥 배송 지시 조회 요청 → cMngNo={}, userNo={}, date={}", cMngNo, userNo, date);

        String sql = "SELECT * FROM dbo.wfn_TANK_DELIVERY_USERNO(?, ?, ?)";

        List<Map<String, Object>> result = jdbc.queryForList(sql, cMngNo, userNo, date);

        log.info("📊 배송 지시 결과 {}건", result.size());
        for (Map<String, Object> row : result) {
            log.info("📦 지시 Row: {}", row);
        }

        return result;
    }

}
