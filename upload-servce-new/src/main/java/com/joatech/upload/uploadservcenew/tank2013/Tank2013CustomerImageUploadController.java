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

    // ✅ INSERT 이미지 등록
    @PostMapping
    public int insertTankImages(@RequestParam("customerCode") String customerCode,
                                @RequestParam("tankCode") String tankCode,
                                @RequestParam("imageList") String imageListJson,
                                @RequestParam("imageType") String imageType) {
        try {
            List<ObjectNode> imageList = objectMapper.readValue(imageListJson, new TypeReference<>() {});

            int insertedCount = 0;
            for (ObjectNode image : imageList) {
                String orgUrl = image.get("originalUrl").asText();
                String smallUrl = image.get("thumbnailUrl").asText();

                // URL 정제
                String orgPath = orgUrl.replaceFirst("https?://[^/]+", "");
                String smallPath = smallUrl.replaceFirst("https?://[^/]+", "");
                String imgName = orgPath.substring(orgPath.lastIndexOf('/') + 1);

                String insertSql = """
                    INSERT INTO GasMax_EYE.dbo.TANK_IMG (
                        DEL_FLAG, C_MNG_NO, CUST_TYPE, CUST_CODE, TRANSM_CD,
                        TANK_CODE, IMG_NAME, IMG_TYPE, SMALL_IMG_URL, ORG_IMG_URL,
                        ORG_IMG_SIZE, WK_ID, WK_DATE
                    ) VALUES (
                        'N', NULL, NULL, ?, NULL,
                        ?, ?, ?, ?, ?, 0, 'SYSTEM', GETDATE()
                    )
                """;

                insertedCount += jdbc.update(insertSql, customerCode, tankCode, imgName, imageType, smallUrl, orgUrl);
            }

            return insertedCount;

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // ✅ READ 전체 이미지
    @PostMapping("/list")
    public List<TankImageRecord> getAllTankImages() {
        String sql = "SELECT * FROM GasMax_EYE.dbo.TANK_IMG ORDER BY AUTO_ID DESC";
        return jdbc.query(sql, new TankImageRowMapper());
    }

    // ✅ READ 단일 탱크 + 고객 이미지
    @PostMapping("/by-code")
    public List<TankImageRecord> getByTankCode(@RequestParam("customerCode") String customerCode,
                                               @RequestParam("tankCode") String tankCode) {
        String sql = "SELECT * FROM GasMax_EYE.dbo.TANK_IMG WHERE TANK_CODE = ? AND CUST_CODE = ? ORDER BY AUTO_ID DESC";
        return jdbc.query(sql, new TankImageRowMapper(), tankCode, customerCode);
    }

    // ✅ DELETE
    @PostMapping("/delete")
    public int deleteByImageUrl(@RequestBody DeleteRequest req) {
        String sql = "DELETE FROM GasMax_EYE.dbo.TANK_IMG WHERE TANK_CODE = ? AND CUST_CODE = ? AND ORG_IMG_URL = ?";
        return jdbc.update(sql, req.getTankCode(), req.getCustomerCode(), req.getImageUrl());
    }

    // ✅ 삭제 요청용 DTO
    @Data
    public static class DeleteRequest {
        private String customerCode;
        private String tankCode;
        private String imageUrl;
    }

    // ✅ 조회 DTO
    @Data
    public static class TankImageRecord {
        private int autoId;
        private String tankCode;
        private String customerCode; // 🔹 추가됨
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
            record.setCustomerCode(rs.getString("CUST_CODE")); // 🔹 바인딩 추가
            record.setOrgImgUrl(rs.getString("ORG_IMG_URL"));
            record.setSmallImgUrl(rs.getString("SMALL_IMG_URL"));
            record.setImgName(rs.getString("IMG_NAME"));
            record.setImgType(rs.getString("IMG_TYPE"));
            return record;
        }
    }

    // ✅ UPDATE 이미지 정보 수정
    @PostMapping("/update")
    public int updateTankImage(@RequestBody UpdateRequest req) {
        try {
            // URL 정제
            String orgPath = req.getOrgImgUrl().replaceFirst("https?://[^/]+", "");
            String smallPath = req.getSmallImgUrl().replaceFirst("https?://[^/]+", "");
            String imgName = orgPath.substring(orgPath.lastIndexOf('/') + 1);

            String sql = """
            UPDATE GasMax_EYE.dbo.TANK_IMG
            SET IMG_NAME = ?,
                SMALL_IMG_URL = ?,
                ORG_IMG_URL = ?,
                WK_ID = 'SYSTEM',
                WK_DATE = GETDATE()
            WHERE TANK_CODE = ? AND CUST_CODE = ? AND IMG_TYPE = ?
        """;

            return jdbc.update(sql, imgName, smallPath, orgPath, req.getTankCode(), req.getCustomerCode(), req.getImageType());
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // ✅ UPDATE 요청 DTO
    @Data
    public static class UpdateRequest {
        private String customerCode;
        private String tankCode;
        private String imageType;     // "0" ~ "3"
        private String orgImgUrl;     // 새 원본 이미지
        private String smallImgUrl;   // 새 썸네일 이미지
    }

}
