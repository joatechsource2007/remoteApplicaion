package com.remote.restservice.delivery.service;

import com.remote.restservice.tank.model.Tank_Insp_Params;
import com.remote.restservice.utils.database.DbHelper;
import com.remote.restservice.utils.database.SpInfo;
import com.remote.restservice.utils.database.SpParameter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.JDBCType;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service("DeliveryService")
@RequiredArgsConstructor
public class DeliveryService {

    private static final Logger logger = LoggerFactory.getLogger(DeliveryService.class);

    private DbHelper dbHelper;

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

}
