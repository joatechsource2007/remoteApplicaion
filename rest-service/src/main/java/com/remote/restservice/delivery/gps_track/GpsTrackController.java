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
@RequestMapping("/api/gps-tracks")
@RequiredArgsConstructor
public class GpsTrackController {

    private final JdbcTemplate jdbc;

    // 🧭 궤적 결과 모델
    @Data
    public static class VehicleTrajectory {
        private String cMngNo;
        private String carCode;
        private String carName;
        private String carType;
        private String rDate;
        private String rTime;
        private String gpsX;
        private String gpsY;
        private String gpsEvent;
    }

    // 🧭 최종 위치 결과 모델
    @Data
    public static class VehicleLatestLocation {
        private String cMngNo;
        private String carCode;
        private String transmCd;
        private String rDate;
        private String rTime;
        private String gpsX;
        private String gpsY;
        private String gpsEvent;
        private String gpsReal;
        private String status;
        private int elapsedMinute;
    }

    // 🧭 궤적 매퍼
    public static class VehicleTrajectoryMapper implements RowMapper<VehicleTrajectory> {
        @Override
        public VehicleTrajectory mapRow(ResultSet rs, int rowNum) throws SQLException {
            VehicleTrajectory v = new VehicleTrajectory();
            v.setCMngNo(rs.getString("C_MNG_NO"));
            v.setCarCode(rs.getString("CAR_CODE"));
            v.setCarName(rs.getString("CAR_NAME"));
            v.setCarType(rs.getString("CAR_TYPE"));
            v.setRDate(rs.getString("R_DATE"));
            v.setRTime(rs.getString("R_TIME"));
            v.setGpsX(rs.getString("GPS_X"));
            v.setGpsY(rs.getString("GPS_Y"));
            v.setGpsEvent(rs.getString("GPS_Event"));
            return v;
        }
    }

    // 🧭 최종 위치 매퍼
    public static class VehicleLatestLocationMapper implements RowMapper<VehicleLatestLocation> {
        @Override
        public VehicleLatestLocation mapRow(ResultSet rs, int rowNum) throws SQLException {
            VehicleLatestLocation v = new VehicleLatestLocation();
            v.setCMngNo(rs.getString("C_MNG_NO"));
            v.setCarCode(rs.getString("CAR_CODE"));
            v.setTransmCd(rs.getString("TRANSM_CD"));
            v.setRDate(rs.getString("R_DATE"));
            v.setRTime(rs.getString("R_TIME"));
            v.setGpsX(rs.getString("GPS_X"));
            v.setGpsY(rs.getString("GPS_Y"));
            v.setGpsEvent(rs.getString("GPS_Event"));
            v.setGpsReal(rs.getString("GPS_REAL"));
            v.setStatus(rs.getString("STATUS"));
            v.setElapsedMinute(rs.getInt("ELAPSED_MINUTE"));
            return v;
        }
    }

    // ✅ 차량 궤적 조회 (FN_TANK_GPS_LINE 함수)
    //todo: UB_GPS_DATA table에서 조회해온다!
    //todo: UB_GPS_DATA table에서 조회해온다!
    //todo: UB_GPS_DATA table에서 조회해온다!
    @GetMapping("/trajectory")
    public List<VehicleTrajectory> getVehicleTrajectory(
            @RequestParam String cMngNo,
            @RequestParam String carCode,
            @RequestParam String date
    ) {
        String sql = "SELECT * FROM FN_TANK_GPS_LINE(?, ?, ?) ORDER BY CAR_CODE, R_DATE, R_TIME";

        // ✅ 콘솔에 쿼리 로그 출력
        System.out.println("📡 Executing SQL: " + sql);
        System.out.println("📌 Params: cMngNo=" + cMngNo + ", carCode=" + carCode + ", date=" + date);

        return jdbc.query(sql, new VehicleTrajectoryMapper(), cMngNo, carCode, date);
    }

    // ✅ 차량 최종 위치 조회 (wfn_Get_Latest_GPS 함수)
    @GetMapping("/latest-location")
    public List<VehicleLatestLocation> getLatestLocation(
            @RequestParam String cMngNo,
            @RequestParam String date,
            @RequestParam String carCode
    ) {
        String sql = "SELECT * FROM dbo.wfn_Get_Latest_GPS(?, ?, ?)";
        return jdbc.query(sql, new VehicleLatestLocationMapper(), cMngNo, date, carCode);
    }


}
