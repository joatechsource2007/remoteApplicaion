package com.remote.restservice.delivery.vehicle;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class VehicleMapper implements RowMapper<Vehicle> {
    @Override
    public Vehicle mapRow(ResultSet rs, int rowNum) throws SQLException {
        Vehicle v = new Vehicle();

        v.setId(rs.getInt("Id"));
        v.setCarNumber(rs.getString("CarNumber"));
        v.setDriverName(rs.getString("DriverName"));
        v.setWorkTime(rs.getString("WorkTime"));
        v.setAddress(rs.getString("Address"));
        v.setStatus(rs.getString("Status"));
        v.setSupplyCount(rs.getInt("SupplyCount"));
        v.setSupplyAmount(rs.getInt("SupplyAmount"));
        v.setTotalQty(rs.getInt("TotalQty"));

        // ✅ 날짜 필드 추가
        v.setCreatedAt(rs.getTimestamp("CreatedAt").toLocalDateTime());
        v.setUpdatedAt(rs.getTimestamp("UpdatedAt").toLocalDateTime());

        return v;
    }
}
