package com.remote.restservice.delivery.charge_new;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ChargeManagementController {

    private final JdbcTemplate jdbcTemplate;



    // ➕ 동적 파라미터 받는 버전 (선택 사항)
    @GetMapping("/charge-management/list")
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
    @GetMapping("/charge-management/all_deliveries")
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

        for (Map<String, Object> row : result) {
            // ✅ REQ_STAT가 null이면 9로 설정
            if (row.get("REQ_STAT") == null) {
                row.put("REQ_STAT", 9);
            }

            // ✅ DLV_ID가 없으면 -1로 설정
            if (!row.containsKey("DLV_ID")) {
                row.put("DLV_ID",0);
            }
        }

        return ResponseEntity.ok(result);
    }

    /**
     * ✅ [신규] 건수만 조회하는 별도 호출용 엔드포인트
     * GET /api/charge-management/counts
     */
    @GetMapping("/charge-management/counts")
    public ResponseEntity<?> getCountsEndpoint(
            @RequestParam String cMngNo,
            @RequestParam String userNo,
            @RequestParam String date,
            @RequestParam(required = false, defaultValue = "") String carCode,
            @RequestParam(required = false, defaultValue = "") String swCode,
            @RequestParam(required = false, defaultValue = "") String findStr
    ) {
        return getCountsOnly(cMngNo, userNo, date, carCode, swCode, findStr);
    }

    /**
     * ✅ 실제 건수 처리 로직 (공통)
     */
    private ResponseEntity<?> getCountsOnly(String cMngNo, String userNo, String date,
                                            String carCode, String swCode, String findStr) {
        try {
            String sql = "EXEC dbo.wsp_TANK_UB_DELIVERY_COUNT " +
                    "@p_C_MNG_NO = ?, @p_USERNO = ?, @p_DATE = ?, " +
                    "@p_CAR_CODE = ?, @p_SW_CODE = ?, @p_FIND_STR = ?";
            Map<String, Object> result = jdbcTemplate.queryForMap(
                    sql, cMngNo, userNo, date, carCode, swCode, findStr);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("❌ 건수 조회 실패: " + e.getMessage());
        }
    }



    @PostMapping("/charge-management/modify")
    public ResponseEntity<?> modifyDelivery(@RequestBody ModifyRequest req) {
        try {
            // ✅ 요청 파라미터 로그
            System.out.println("📤 PL/SQL 전달 파라미터 =========================");
            System.out.println("PageID     : " + req.getPageID());
            System.out.println("TableName  : " + req.getTableName());
            System.out.println("Operation  : " + req.getOperation());
            System.out.println("PKValue    : " + req.getPkValue());
            System.out.println("NewData    : " + req.getNewData());
            System.out.println("ChangedBy  : " + req.getChangedBy());
            System.out.println("USERNO     : " + req.getUserNo());
            System.out.println("App_cert   : " + req.getAppCert());
            System.out.println("==============================================");

            // ✅ 결과셋 받아오기 (예: SELECT 결과 리턴되는 프로시저 대응)
            List<Map<String, Object>> result = jdbcTemplate.queryForList(
                    "EXEC dbo.wsp_Modify_Data " +
                            "@PageID = ?, " +
                            "@TableName = ?, " +
                            "@Operation = ?, " +
                            "@PKValue = ?, " +
                            "@NewData = ?, " +
                            "@ChangedBy = ?, " +
                            "@USERNO = ?, " +
                            "@App_cert = ?",
                    req.getPageID(),
                    req.getTableName(),
                    req.getOperation(),
                    req.getPkValue(),
                    req.getNewData(),
                    req.getChangedBy(),
                    req.getUserNo(),
                    req.getAppCert()
            );

            return ResponseEntity.ok(result); // JSON 결과 응답

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("❌ 오류: " + e.getMessage());
        }
    }




}
