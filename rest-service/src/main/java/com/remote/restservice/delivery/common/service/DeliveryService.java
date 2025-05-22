package com.remote.restservice.delivery.common.service;

import com.remote.restservice.utils.database.DbHelper;
import com.remote.restservice.utils.database.SpInfo;
import com.remote.restservice.utils.database.SpParameter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.JDBCType;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.remote.restservice.common.query_util.DatabaseQueryUtil.buildFinalSql;

@Service("DeliveryService")
@RequiredArgsConstructor
public class DeliveryService {

    private static final Logger logger = LoggerFactory.getLogger(DeliveryService.class);

    private DbHelper dbHelper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public DeliveryService(DbHelper dbHelper) {
        this.dbHelper = dbHelper;
    }


    public Map<String, Object> selectTankIN(Map<String, Object> params) throws SQLException {
        List<SpParameter> spParams = new ArrayList<>();

        for (Map.Entry<String, Object> entry : params.entrySet()) {
            logger.info("📌 PARAM: {} = {}", entry.getKey(), entry.getValue());

            spParams.add(SpParameter.builder()
                    .name(entry.getKey())
                    .value(entry.getValue())
                    .jdbcType(JDBCType.VARCHAR) // 대부분 varchar 사용. 상황에 따라 Integer, NUMERIC 등으로 변경 가능
                    .direction(SpParameter.Direction.IN)
                    .build());
        }

        SpInfo spInfo = SpInfo.builder()
                .spName("wsp_TANK_INSP_SELECT")
                .spParameterList(spParams)
                .tableNames(List.of())
                .build();

        logger.info("🌀 SP CALL: {}", spInfo);
        return dbHelper.execute(spInfo);
    }




    public List<Map<String, Object>> selectJaegoList(String cMngNo, String jaeLast) {
        String sql = "SELECT * FROM UB_JAEGO WHERE C_MNG_NO = ? AND JAE_LAST = ?";
        Object[] params = {cMngNo, jaeLast};

        // 완성된 쿼리처럼 출력 (컬러 강조 포함)
        logger.info("🧾 \u001B[35m최종 실행 SQL:\u001B[0m {}", buildFinalSql(sql, params));


        return jdbcTemplate.query(sql, params, (rs, rowNum) -> {
            int columnCount = rs.getMetaData().getColumnCount();
            Map<String, Object> row = new HashMap<>();
            for (int i = 1; i <= columnCount; i++) {
                row.put(rs.getMetaData().getColumnName(i), rs.getObject(i));
            }
            return row;
        });
    }


}
