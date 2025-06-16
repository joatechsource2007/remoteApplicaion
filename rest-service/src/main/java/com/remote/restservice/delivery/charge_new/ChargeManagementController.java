package com.remote.restservice.delivery.charge_new;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/charge-management")
@RequiredArgsConstructor
public class ChargeManagementController {

    private final JdbcTemplate jdbcTemplate;



    // ➕ 동적 파라미터 받는 버전 (선택 사항)
    @GetMapping("/list")
    public ResponseEntity<List<Map<String, Object>>> getCompletedDeliveries(
            @RequestParam String divType,   // ✅ 조회구분 추가 (A, 1, 2, 3, 4 중 하나)
            @RequestParam String cMngNo,
            @RequestParam String userNo,
            @RequestParam String date,
            @RequestParam String carCode,
            @RequestParam String swCode,
            @RequestParam(defaultValue = "") String findStr
    ) {
        String procedure = "{CALL dbo.wsp_TANK_UB_DELIVERY_USERNO(?, ?, ?, ?, ?, ?, ?)}";

        List<Map<String, Object>> result = jdbcTemplate.queryForList(
                procedure,
                divType,  // 동적으로 전달된 조회구분
                cMngNo,
                userNo,
                date,
                carCode,
                swCode,
                findStr
        );
        return ResponseEntity.ok(result);
    }

    // ✅ 요청/접수/연기 + 완료 병합 조회
    @GetMapping("/all_deliveries")
    public ResponseEntity<List<Map<String, Object>>> getRequestedDeliveries(
            @RequestParam String cMngNo,
            @RequestParam String userNo,
            @RequestParam String date,
            @RequestParam String carCode,
            @RequestParam String swCode,
            @RequestParam(defaultValue = "") String findStr
    ) {
        String procedure = "{CALL dbo.wsp_TANK_UB_DELIVERY_USERNO(?, ?, ?, ?, ?, ?, ?)}";

        // 요청/접수/연기
        List<Map<String, Object>> result = jdbcTemplate.queryForList(
                procedure, "2", cMngNo, userNo, date, carCode, swCode, findStr
        );

        // 완료
        List<Map<String, Object>> result2 = jdbcTemplate.queryForList(
                procedure, "3", cMngNo, userNo, date, carCode, swCode, findStr
        );

        // 병합
        result.addAll(result2);

        // ✅ REQ_STAT가 null인 경우 9로 세팅
        for (Map<String, Object> row : result) {
            if (row.get("REQ_STAT") == null) {
                row.put("REQ_STAT", 9);
            }
        }

        return ResponseEntity.ok(result);
    }

}
