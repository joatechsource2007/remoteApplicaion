package com.remote.restservice.delivery.gps_track;

import lombok.Data;
import lombok.RequiredArgsConstructor;
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

    // ✅ 차량 정보 모델
    @Data
    public static class VehicleInfo {
        private String cMngNo;
        private String carCode;
        private String carName;
        private String carType;
        private String carTypeName;
        private String iotCode;
        private String swCode;
        private String swName;
        private String carMaker;
        private String carInsure;
        private String carInsureTurm;
        private String carMemo;
    }

    // ✅ 매퍼
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

    // ✅ 차량 리스트 조회 (wsp_OTHER_LIST → UB_CAR)
    @GetMapping
    public List<VehicleInfo> getVehicleList(@RequestParam String cMngNo) {
        String sql = "EXEC wsp_OTHER_LIST @p_C_MNG_NO = ?, @p_OTHER_NAME = 'UB_CAR', " +
                "@p_FIND_TEXT = NULL, @p_FIND_G1 = NULL, @p_FIND_G2 = NULL, @p_FIND_G3 = NULL";

        System.out.println("🚗 차량 리스트 조회: " + cMngNo);

        return jdbc.query(sql, new VehicleInfoMapper(), cMngNo);
    }
}
