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
    private final ObjectMapper objectMapper = new ObjectMapper();

    // ✅ INSERT
    @PostMapping
    public int insertTankImages(@RequestParam("customerCode") String customerCode,
                                @RequestParam("imageList") String imageListJson) {
        try {
            List<ObjectNode> imageList = objectMapper.readValue(imageListJson, new TypeReference<>() {});

            int insertedCount = 0;
            for (ObjectNode image : imageList) {
                String orgUrl = image.get("originalUrl").asText();
                String smallUrl = image.get("thumbnailUrl").asText();

                String insertSql = """
                    INSERT INTO GasMax_EYE.dbo.TANK_IMG (
                        DEL_FLAG, C_MNG_NO, CUST_TYPE, CUST_CODE, TRANSM_CD,
                        TANK_CODE, IMG_NAME, IMG_TYPE, SMALL_IMG_URL, ORG_IMG_URL,
                        ORG_IMG_SIZE, WK_ID, WK_DATE
                    ) VALUES (
                        'N', NULL, NULL, 'TEST-CODE', NULL,
                        ?, 'dummy.jpg', 'jpg', ?, ?, 0, 'SYSTEM', GETDATE()
                    )
                """;

                insertedCount += jdbc.update(insertSql, customerCode, smallUrl, orgUrl);
            }

            return insertedCount;

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // ✅ READ 전체
    @PostMapping("/list")
    public List<TankImageRecord> getAllTankImages() {
        String sql = "SELECT * FROM GasMax_EYE.dbo.TANK_IMG ORDER BY AUTO_ID DESC";
        return jdbc.query(sql, new TankImageRowMapper());
    }

    // ✅ READ 단건
    @PostMapping("/by-code")
    public List<TankImageRecord> getByTankCode(@RequestParam("customerCode") String customerCode) {
        String sql = "SELECT * FROM GasMax_EYE.dbo.TANK_IMG WHERE TANK_CODE = ? ORDER BY AUTO_ID DESC";
        return jdbc.query(sql, new TankImageRowMapper(), customerCode);
    }

    @Data
    public static class DeleteRequest {
        private String customerCode;
        private String imageUrl;
    }

    @PostMapping("/delete")
    public int deleteByImageUrl(@RequestBody DeleteRequest req) {
        String sql = "DELETE FROM GasMax_EYE.dbo.TANK_IMG WHERE TANK_CODE = ? AND ORG_IMG_URL = ?";
        return jdbc.update(sql, req.getCustomerCode(), req.getImageUrl());
    }

    // ✅ DTO (조회용 레코드)
    @Data
    public static class TankImageRecord {
        private int autoId;
        private String tankCode;
        private String orgImgUrl;
        private String smallImgUrl;
        private String imgName;
        private String imgType;
    }

    // ✅ RowMapper
    private static class TankImageRowMapper implements RowMapper<TankImageRecord> {
        @Override
        public TankImageRecord mapRow(ResultSet rs, int rowNum) throws SQLException {
            TankImageRecord record = new TankImageRecord();
            record.setAutoId(rs.getInt("AUTO_ID"));
            record.setTankCode(rs.getString("TANK_CODE"));
            record.setOrgImgUrl(rs.getString("ORG_IMG_URL"));
            record.setSmallImgUrl(rs.getString("SMALL_IMG_URL"));
            record.setImgName(rs.getString("IMG_NAME"));
            record.setImgType(rs.getString("IMG_TYPE"));
            return record;
        }
    }
}
