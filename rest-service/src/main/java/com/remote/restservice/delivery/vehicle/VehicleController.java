package com.remote.restservice.delivery.vehicle;

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
public class VehicleController {

    private final JdbcTemplate jdbc;

    @GetMapping
    public List<Vehicle> getAll() {
        return jdbc.query("SELECT * FROM test_Vehicle ORDER BY Id DESC", new VehicleMapper());
    }

    @PostMapping
    public int create(@RequestBody Vehicle v) {
        return jdbc.update("""
        INSERT INTO test_Vehicle 
        (CarNumber, DriverName, WorkTime, Address, Status, SupplyCount, SupplyAmount, TotalQty, CreatedAt, UpdatedAt)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, GETDATE(), GETDATE())
        """,
                v.getCarNumber(), v.getDriverName(), v.getWorkTime(), v.getAddress(),
                v.getStatus(), v.getSupplyCount(), v.getSupplyAmount(), v.getTotalQty());
    }

    @PutMapping("/{id}")
    public int update(@PathVariable int id, @RequestBody Vehicle v) {
        return jdbc.update("UPDATE test_Vehicle SET CarNumber=?, DriverName=?, WorkTime=?, Address=?, Status=?, SupplyCount=?, SupplyAmount=?, TotalQty=? WHERE Id=?",
                v.getCarNumber(), v.getDriverName(), v.getWorkTime(), v.getAddress(), v.getStatus(), v.getSupplyCount(), v.getSupplyAmount(), v.getTotalQty(), id);
    }

    @DeleteMapping("/{id}")
    public int delete(@PathVariable int id) {
        return jdbc.update("DELETE FROM test_Vehicle WHERE Id = ?", id);
    }

    static class VehicleMapper implements RowMapper<Vehicle> {
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
            return v;
        }
    }

    @Data
    public static class Vehicle {
        private int id;
        private String carNumber;
        private String driverName;
        private String workTime;
        private String address;
        private String status;
        private int supplyCount;
        private int supplyAmount;
        private int totalQty;
    }
}
