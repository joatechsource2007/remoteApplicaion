package com.joatech.upload.uploadservcenew.tank2013;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.bind.annotation.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api/tank2013/customer-images")
@RequiredArgsConstructor
public class Tank2013CustomerImageUploadController {

    private final JdbcTemplate jdbc;

    // ✅ CREATE
    @PostMapping
    public int insertCustomerImages(@RequestBody CustomerImageRequest req) {
        String sql = """
            INSERT INTO CustomerImageJson (CustomerCode, CustomerName, ImageList)
            VALUES (?, ?, ?)
        """;
        return jdbc.update(sql, req.getCustomerCode(), req.getCustomerName(), req.getImageList());
    }

    // ✅ READ (전체 목록)
    @GetMapping
    public List<CustomerImageRequest> getAllCustomerImages() {
        String sql = "SELECT * FROM CustomerImageJson ORDER BY Id DESC";
        return jdbc.query(sql, new CustomerImageRowMapper());
    }

    // ✅ READ (단건 조회 by customerCode)
    @GetMapping("/{customerCode}")
    public List<CustomerImageRequest> getByCustomerCode(@PathVariable String customerCode) {
        String sql = "SELECT * FROM CustomerImageJson WHERE CustomerCode = ?";
        return jdbc.query(sql, new CustomerImageRowMapper(), customerCode);
    }

    // ✅ UPDATE (customerCode 기준)
    @PutMapping("/{customerCode}")
    public int updateImageList(@PathVariable String customerCode, @RequestBody CustomerImageRequest req) {
        String sql = """
            UPDATE CustomerImageJson SET CustomerName = ?, ImageList = ? WHERE CustomerCode = ?
        """;
        return jdbc.update(sql, req.getCustomerName(), req.getImageList(), customerCode);
    }

    // ✅ DELETE (customerCode 기준)
    @DeleteMapping("/{customerCode}")
    public int deleteByCustomerCode(@PathVariable String customerCode) {
        String sql = "DELETE FROM CustomerImageJson WHERE CustomerCode = ?";
        return jdbc.update(sql, customerCode);
    }

    // ✅ DTO
    @Data
    public static class CustomerImageRequest {
        private String customerCode;
        private String customerName;
        private String imageList;
    }

    // ✅ RowMapper
    private static class CustomerImageRowMapper implements RowMapper<CustomerImageRequest> {
        @Override
        public CustomerImageRequest mapRow(ResultSet rs, int rowNum) throws SQLException {
            CustomerImageRequest req = new CustomerImageRequest();
            req.setCustomerCode(rs.getString("CustomerCode"));
            req.setCustomerName(rs.getString("CustomerName"));
            req.setImageList(rs.getString("ImageList"));
            return req;
        }
    }
}
