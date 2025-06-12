package com.remote.restservice.delivery.vehicle;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.bind.annotation.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
@RequiredArgsConstructor
public class VehicleListController {

    private final JdbcTemplate jdbc;

    @Data
    public static class VehicleInfo {
        @JsonProperty("cMngNo")
        private String cMngNo;

        @JsonProperty("carCode")
        private String carCode;

        @JsonProperty("carName")
        private String carName;

        @JsonProperty("carType")
        private String carType;

        @JsonProperty("carTypeName")
        private String carTypeName;

        @JsonProperty("iotCode")
        private String iotCode;

        @JsonProperty("swCode")
        private String swCode;

        @JsonProperty("swName")
        private String swName;

        @JsonProperty("carMaker")
        private String carMaker;

        @JsonProperty("carInsure")
        private String carInsure;

        @JsonProperty("carInsureTurm")
        private String carInsureTurm;

        @JsonProperty("carMemo")
        private String carMemo;
    }

    public static class VehicleInfoMapper implements RowMapper<VehicleInfo> {
        @Override
        public VehicleInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
            VehicleInfo v = new VehicleInfo();
            v.setCMngNo(rs.getString("C_MNG_NO"));
            v.setCarCode(rs.getString("CAR_CODE"));
            v.setCarName(rs.getString("CAR_NAME"));
            v.setCarType(rs.getString("CAR_TYPE"));
            v.setCarTypeName(rs.getString("CAR_TYPE_NAME"));
            v.setIotCode(rs.getString("IOT_CODE"));
            v.setSwCode(rs.getString("SW_CODE"));
            v.setSwName(rs.getString("SW_NAME"));
            v.setCarMaker(rs.getString("CAR_MAKER"));
            v.setCarInsure(rs.getString("CAR_INSURE"));
            v.setCarInsureTurm(rs.getString("CAR_INSURE_TURM"));
            v.setCarMemo(rs.getString("CAR_MEMO"));
            return v;
        }
    }

    private static final Logger log = LoggerFactory.getLogger(VehicleListController.class);

    //todo: ✅ 차량 리스트 조회
    @GetMapping
    public List<VehicleInfo> getVehicleList(@RequestParam String cMngNo) {
        log.info("📥 차량 리스트 조회 요청 - cMngNo: {}", cMngNo);

        String sql = "EXEC wsp_OTHER_LIST @p_C_MNG_NO = ?, @p_OTHER_NAME = 'UB_CAR', " +
                "@p_FIND_TEXT = NULL, @p_FIND_G1 = NULL, @p_FIND_G2 = NULL, @p_FIND_G3 = NULL";

        List<VehicleInfo> result = jdbc.query(sql, new VehicleInfoMapper(), cMngNo);

        log.info("📤 차량 리스트 조회 완료 - 반환 건수: {}", result.size());

        return result;
    }


    // ✅ 차량 등록
    @PostMapping
    public ResponseEntity<String> registerVehicle(@RequestBody VehicleInfo vehicle) {
        if (vehicle.getCMngNo() == null || vehicle.getCarCode() == null) {
            return ResponseEntity.badRequest().body("필수 필드(cMngNo, carCode)가 누락되었습니다.");
        }

        String sql = "INSERT INTO UB_CAR " +
                "(C_MNG_NO, CAR_CODE, CAR_NAME, CAR_TYPE, IOT_CODE, SW_CODE, CAR_MAKER, CAR_INSURE, CAR_INSURE_TURM, CAR_MEMO) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            int result = jdbc.update(sql,
                    vehicle.getCMngNo(),
                    vehicle.getCarCode(),
                    vehicle.getCarName(),
                    vehicle.getCarType(),
                    vehicle.getIotCode(),
                    vehicle.getSwCode(),
                    vehicle.getCarMaker(),
                    vehicle.getCarInsure(),
                    vehicle.getCarInsureTurm(),
                    vehicle.getCarMemo()
            );
            return result > 0
                    ? ResponseEntity.status(HttpStatus.CREATED).body("등록 성공")
                    : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("등록 실패");
        } catch (Exception e) {
            if (e.getMessage().contains("PRIMARY KEY")) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("중복된 차량 코드입니다: " + vehicle.getCarCode());
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("예외 발생: " + e.getMessage());
        }
    }

    // ✅ 차량 수정
    @PutMapping
    public ResponseEntity<String> updateVehicle(@RequestBody VehicleInfo vehicle) {
        if (vehicle.getCMngNo() == null || vehicle.getCarCode() == null) {
            return ResponseEntity.badRequest().body("필수 필드(cMngNo, carCode)가 누락되었습니다.");
        }

        String sql = "UPDATE UB_CAR SET " +
                "CAR_NAME = ?, CAR_TYPE = ?, IOT_CODE = ?, SW_CODE = ?, " +
                "CAR_MAKER = ?, CAR_INSURE = ?, CAR_INSURE_TURM = ?, CAR_MEMO = ? " +
                "WHERE C_MNG_NO = ? AND CAR_CODE = ?";
        int result = jdbc.update(sql,
                vehicle.getCarName(),
                vehicle.getCarType(),
                vehicle.getIotCode(),
                vehicle.getSwCode(),
                vehicle.getCarMaker(),
                vehicle.getCarInsure(),
                vehicle.getCarInsureTurm(),
                vehicle.getCarMemo(),
                vehicle.getCMngNo(),
                vehicle.getCarCode()
        );
        return result > 0
                ? ResponseEntity.ok("수정 성공")
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body("수정 대상 없음");
    }

    // ✅ 차량 삭제
    @DeleteMapping
    public ResponseEntity<String> deleteVehicle(@RequestParam String cMngNo, @RequestParam String carCode) {
        String sql = "DELETE FROM UB_CAR WHERE C_MNG_NO = ? AND CAR_CODE = ?";
        try {
            int result = jdbc.update(sql, cMngNo, carCode);
            return result > 0
                    ? ResponseEntity.ok("삭제 성공")
                    : ResponseEntity.status(HttpStatus.NOT_FOUND).body("삭제 대상 없음");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("삭제 중 오류: " + e.getMessage());
        }
    }
}
