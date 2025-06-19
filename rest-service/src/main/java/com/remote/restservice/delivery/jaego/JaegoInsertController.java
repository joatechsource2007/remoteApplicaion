package com.remote.restservice.delivery.jaego;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/jaego")
@RequiredArgsConstructor
@Slf4j
public class JaegoInsertController {

    private final JdbcTemplate jdbc;

    @Data
    public static class JaegoInsertRequest {

        @JsonProperty("cMngNo")
        private String cMngNo;

        @JsonProperty("carCode")
        private String carCode;

        @JsonProperty("jaeDate")
        private String jaeDate;

        @JsonProperty("jaeType")
        private String jaeType;

        @JsonProperty("jaeLast")
        private String jaeLast;

        @JsonProperty("carName")
        private String carName;

        @JsonProperty("swCode")
        private String swCode;

        @JsonProperty("swName")
        private String swName;

        @JsonProperty("jaeDateType")
        private String jaeDateType;

        @JsonProperty("wkPostDt")
        private String wkPostDt;

        @JsonProperty("appUser")
        private String appUser;

        @JsonProperty("gpsX")
        private String gpsX;

        @JsonProperty("gpsY")
        private String gpsY;

        @JsonProperty("userNo")
        private String userNo;

        @JsonProperty("changedBy")
        private String changedBy;

        @JsonProperty("appCert")
        private String appCert;

        @JsonProperty("jaeCarP")
        private String jaeCarP;

        @JsonProperty("jaeCarKm")
        private String jaeCarKm;

        @JsonProperty("jaeCarL")
        private String jaeCarL;

        @JsonProperty("jaeCarKg")
        private String jaeCarKg;

        @JsonProperty("totalReg")
        private String totalReg;

        @JsonProperty("userMemo")
        private String userMemo;

    }

    private String buildKeyValueString(Map<String, String> map) {
        return map.entrySet().stream()
                .map(e -> String.format("\"%s\":\"%s\"", e.getKey(), e.getValue()))
                .collect(Collectors.joining(","));
    }


    /**
     * todo: 운행 처량 목록 insert
     * @param req
     * @return
     */
    @PostMapping("/insert")
    public ResponseEntity<String> insertJaego(@RequestBody JaegoInsertRequest req) {
        log.info("📥 UB_JAEGO INSERT 요청 수신: {}", req);

        if (req.getCMngNo() == null || req.getCarCode() == null || req.getJaeDate() == null) {
            return ResponseEntity.badRequest().body("❌ 필수 필드 누락: cMngNo, carCode, jaeDate");
        }

        String pkValue = buildKeyValueString(Map.of(
                "C_MNG_NO", req.getCMngNo().trim(),
                "CAR_CODE", req.getCarCode().trim(),
                "JAE_DATE", req.getJaeDate().trim()
        ));

        String newData = buildKeyValueString(Map.ofEntries(
                Map.entry("JAE_TYPE", req.getJaeType()),
                Map.entry("JAE_LAST", req.getJaeLast()),
                Map.entry("CAR_NAME", req.getCarName()),
                Map.entry("SW_CODE", req.getSwCode()),
                Map.entry("SW_NAME", req.getSwName()),
                Map.entry("JAE_DATE_TYPE", req.getJaeDateType()),
                Map.entry("WK_POST_DT", req.getWkPostDt()),
                Map.entry("APP_USER", req.getAppUser()),
                Map.entry("GPS_X", req.getGpsX()),
                Map.entry("GPS_Y", req.getGpsY()),
                Map.entry("USERNO", req.getUserNo())
        ));


        log.info("🔑 PKValue: {}", pkValue);
        log.info("📦 NewData: {}", newData);

        String sql = "EXEC dbo.wsp_Modify_Data " +
                "@PageID = ?, " +
                "@TableName = ?, " +
                "@Operation = ?, " +
                "@PKValue = ?, " +
                "@NewData = ?, " +
                "@ChangedBy = ?, " +
                "@USERNO = ?, " +
                "@App_cert = ?";

        try {
            jdbc.query(con -> {
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setString(1, "A111");
                ps.setString(2, "UB_JAEGO");
                //ps.setString(2, "UB_JAEGO_XYZ"); // 잘못된 TableName
                ps.setString(3, "INSERT");
                ps.setString(4, pkValue);
                ps.setString(5, newData);
                ps.setString(6, req.getChangedBy());
                ps.setString(7, req.getUserNo());
                ps.setString(8, req.getAppCert());
                return ps;
            }, (ResultSet rs) -> {
                while (rs.next()) {
                    String message = rs.getString(1); // 필요 시 반환 메시지 확인
                    log.info("📨 프로시저 응답 메시지: {}", message);
                }
                return null;
            });

            return ResponseEntity.ok("✅ INSERT 성공");
        } catch (Exception e) {
            log.error("❌ INSERT 예외 발생: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body("예외 발생: " + e.getMessage());
        }
    }

    @PostMapping("/insert-driving")
    public ResponseEntity<String> insertDriving(@RequestBody JaegoInsertRequest req) {
        String typeLabel = "1".equals(req.getJaeType()) ? "운행시작" : "운행종료";
        log.info("📥 {} INSERT 요청 수신: {}", typeLabel, req);

        if (req.getCMngNo() == null || req.getCarCode() == null || req.getJaeDate() == null) {
            return ResponseEntity.badRequest().body("❌ 필수 필드 누락: cMngNo, carCode, jaeDate");
        }

        String pkValue = buildKeyValueString(Map.of(
                "C_MNG_NO", req.getCMngNo().trim(),
                "CAR_CODE", req.getCarCode().trim(),
                "JAE_DATE", req.getJaeDate().trim()
        ));

        String newData = buildKeyValueString(Map.ofEntries(
                Map.entry("JAE_TYPE", req.getJaeType()),
                Map.entry("JAE_LAST", req.getJaeLast()),
                Map.entry("CAR_NAME", req.getCarName()),
                Map.entry("SW_CODE", req.getSwCode()),
                Map.entry("SW_NAME", req.getSwName()),
                Map.entry("JAE_CAR_P", req.getJaeCarP()),
                Map.entry("JAE_CAR_Km", req.getJaeCarKm()),
                Map.entry("JAE_CAR_L", req.getJaeCarL()),
                Map.entry("JAE_CAR_Kg", req.getJaeCarKg()),
                Map.entry("TOTAL_REG", req.getTotalReg()),
                Map.entry("JAE_DATE_TYPE", req.getJaeDateType()),
                Map.entry("WK_POST_DT", req.getWkPostDt()),
                Map.entry("APP_USER", req.getAppUser()),
                Map.entry("GPS_X", req.getGpsX()),
                Map.entry("GPS_Y", req.getGpsY()),
                Map.entry("USERMEMO", req.getUserMemo()),
                Map.entry("USERNO", req.getUserNo())
        ));

        log.info("🔑 PKValue: {}", pkValue);
        log.info("📦 NewData: {}", newData);

        String sql = "EXEC dbo.wsp_Modify_Data " +
                "@PageID = ?, " +
                "@TableName = ?, " +
                "@Operation = ?, " +
                "@PKValue = ?, " +
                "@NewData = ?, " +
                "@ChangedBy = ?, " +
                "@USERNO = ?, " +
                "@App_cert = ?";

        try {
            jdbc.query(con -> {
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setString(1, "A112");
                ps.setString(2, "UB_JAEGO");
                ps.setString(3, "INSERT");
                ps.setString(4, pkValue);
                ps.setString(5, newData);
                ps.setString(6, req.getChangedBy());
                ps.setString(7, req.getUserNo());
                ps.setString(8, req.getAppCert());
                return ps;
            }, (ResultSet rs) -> {
                while (rs.next()) {
                    log.info("📨 프로시저 응답: {}", rs.getString(1));
                }
                return null;
            });

            return ResponseEntity.ok("✅ " + typeLabel + " INSERT 성공");
        } catch (Exception e) {
            log.error("❌ {} INSERT 실패: {}", typeLabel, e.getMessage(), e);
            return ResponseEntity.status(500).body("예외 발생: " + e.getMessage());
        }
    }




    /**
     * todo: 운행 차량 콤보 박스 가져오기
     * @param cMngNo
     * @return
     */
    @GetMapping("/car-combo")
    public List<Map<String, Object>> getCarCombo(@RequestParam String cMngNo) {
        log.info("🚗 차량 콤보 조회 요청: cMngNo={}", cMngNo);

        String sql = "EXEC wsp_Get_Base_Code @p_CodeType = 'UB_CAR', @p_C_MNG_NO = ?";
        return jdbc.queryForList(sql, cMngNo);
    }
}
