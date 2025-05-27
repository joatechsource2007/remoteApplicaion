package com.joatech.upload.uploadservcenew.tank2013;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
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
        String customerCode = req.getCustomerCode();

        // 1. 기존 데이터 조회
        String selectSql = "SELECT * FROM CustomerImageJson WHERE CustomerCode = ?";
        List<CustomerImageRequest> existingList = jdbc.query(selectSql, new CustomerImageRowMapper(), customerCode);

        ObjectMapper mapper = new ObjectMapper();
        try {
            List<ObjectNode> newImages = mapper.readValue(req.getImageList(), new TypeReference<>() {});

            if (existingList.isEmpty()) {
                // 2-1. 기존 데이터 없으면 INSERT
                String insertSql = """
                INSERT INTO CustomerImageJson (CustomerCode, CustomerName, ImageList)
                VALUES (?, ?, ?)
            """;
                return jdbc.update(insertSql, customerCode, req.getCustomerName(), mapper.writeValueAsString(newImages));
            } else {
                // 2-2. 기존 데이터 있으면 기존 이미지 + 새 이미지 합치기
                CustomerImageRequest existing = existingList.get(0);
                List<ObjectNode> existingImages = mapper.readValue(existing.getImageList(), new TypeReference<>() {});

                existingImages.addAll(newImages); // 병합
                String updatedJson = mapper.writeValueAsString(existingImages);

                String updateSql = "UPDATE CustomerImageJson SET CustomerName = ?, ImageList = ? WHERE CustomerCode = ?";
                return jdbc.update(updateSql, req.getCustomerName(), updatedJson, customerCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
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


    @Data
    public static class DeleteImageRequest {
        private String customerCode;
        private String imageUrl;
    }

    @PostMapping("/delete")
    public int deleteSingleImage(@RequestBody DeleteImageRequest req) {
        String sql = "SELECT * FROM CustomerImageJson WHERE CustomerCode = ?";
        List<CustomerImageRequest> results = jdbc.query(sql, new CustomerImageRowMapper(), req.getCustomerCode());

        if (results.isEmpty()) return 0;

        CustomerImageRequest record = results.get(0);
        String imageListJson = record.getImageList();

        try {
            ObjectMapper mapper = new ObjectMapper();
            List<ObjectNode> imageList = mapper.readValue(imageListJson, new TypeReference<>() {});

            // 기존 목록 중 삭제할 이미지 제외
            List<ObjectNode> updatedList = imageList.stream()
                    .filter(node -> !req.getImageUrl().equals(node.get("originalUrl").asText()))
                    .toList();

            String updatedJson = mapper.writeValueAsString(updatedList);

            // DB 업데이트
            String updateSql = "UPDATE CustomerImageJson SET ImageList = ? WHERE CustomerCode = ?";
            return jdbc.update(updateSql, updatedJson, req.getCustomerCode());

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
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
