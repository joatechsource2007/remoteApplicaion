package com.remote.restservice.common.query_util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.ResultSetMetaData;
import java.util.*;

@Component
public class DatabaseQueryUtil {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseQueryUtil.class);

    private final JdbcTemplate jdbcTemplate;

    public DatabaseQueryUtil(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 기존 execute(SpInfo spInfo) 메서드는 유지하고...

    // ✅ 공통 조건 기반 SELECT
    public List<Map<String, Object>> selectByConditions(String tableName, Map<String, Object> conditions) {
        StringBuilder sql = new StringBuilder("SELECT * FROM " + tableName);
        List<Object> values = new ArrayList<>();

        if (conditions != null && !conditions.isEmpty()) {
            sql.append(" WHERE ");
            int i = 0;
            for (Map.Entry<String, Object> entry : conditions.entrySet()) {
                if (i++ > 0) sql.append(" AND ");
                sql.append(entry.getKey()).append(" = ?");
                values.add(entry.getValue());
            }
        }

        logger.info("📦 실행 SQL: {}", sql);
        logger.info("📦 파라미터: {}", values);

        return jdbcTemplate.query(sql.toString(), values.toArray(), (rs, rowNum) -> {
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            Map<String, Object> row = new HashMap<>();
            for (int i = 1; i <= columnCount; i++) {
                row.put(metaData.getColumnName(i), rs.getObject(i));
            }
            return row;
        });
    }

    public static String buildFinalSql(String sql, Object[] params) {
        for (Object param : params) {
            String replacement;
            if (param == null) {
                replacement = "NULL";
            } else if (param instanceof Number) {
                replacement = param.toString();
            } else {
                replacement = "'" + param.toString().replace("'", "''") + "'";
            }
            sql = sql.replaceFirst("\\?", replacement);
        }
        return sql;
    }

}
