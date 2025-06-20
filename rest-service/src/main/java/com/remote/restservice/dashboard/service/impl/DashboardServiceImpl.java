package com.remote.restservice.dashboard.service.impl;

import com.remote.restservice.dashboard.model.Dashboard_Params;
import com.remote.restservice.dashboard.service.DashBoardService;
import com.remote.restservice.utils.database.DbHelper;
import com.remote.restservice.utils.database.SpInfo;
import com.remote.restservice.utils.database.SpParameter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Service("DashboardService")
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashBoardService {

    private static final Logger logger = LoggerFactory.getLogger(DashboardServiceImpl.class);

    private DbHelper dbHelper;

    @Autowired
    public DashboardServiceImpl(DbHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    /**
     * A102	: wsp_Get_CAR_Drive_SW
     * @param params
     * @return
     * @throws SQLException
     */
    @Override
    public Map<String, Object> cardriver(Map<String,Object> params) throws SQLException {
        List<String> listOfTableNames = List.of();
        Dashboard_Params pMeter_Params = new Dashboard_Params();

        for (String parameter : params.keySet()) {

            logger.info("===== A102 ==============================");
            logger.info("parameter : " + parameter);
            logger.info("========================================");


            SpParameter oldSpParameter =  pMeter_Params.getSpParameterByName(SpParameter.SpType.QUERY, parameter);
            if(oldSpParameter==null) continue;
            oldSpParameter.setValue(params.get(parameter));
            pMeter_Params.replaceSpParameterByName(SpParameter.SpType.QUERY,parameter,oldSpParameter);
        }
        logger.info("====== 331 ==================================");
        SpInfo spInfo = SpInfo.builder()
                .spName("wsp_Get_CAR_DRIVER_SW")
                .spParameterList(pMeter_Params.getListOfQuerySpParameters())
                .tableNames(listOfTableNames)
                .build();
        logger.info(spInfo.toString());
        return dbHelper.execute(spInfo);
    }

    /**
     * A103  wep_Get_Tank_LastData_Summary
     * @param params
     * @return
     * @throws SQLException
     */
    @Override
    public Map<String, Object> tanksummary(Map<String,Object> params) throws SQLException {
        List<String> listOfTableNames = List.of();
        Dashboard_Params pMeter_Params = new Dashboard_Params();

        for (String parameter : params.keySet()) {

            logger.info("======== A103 ================================");
            logger.info("parameter : " + parameter);
            logger.info("========================================");


            SpParameter oldSpParameter =  pMeter_Params.getSpParameterByName(SpParameter.SpType.FIND, parameter);
            if(oldSpParameter==null) continue;
            oldSpParameter.setValue(params.get(parameter));
            pMeter_Params.replaceSpParameterByName(SpParameter.SpType.FIND,parameter,oldSpParameter);
        }
        logger.info("============ A103 ============================");
        SpInfo spInfo = SpInfo.builder()
                .spName("wsp_Get_Tank_LastData_Summary")
                .spParameterList(pMeter_Params.getListOfFindSpParameters())
                .tableNames(listOfTableNames)
                .build();
        logger.info(spInfo.toString());
        return dbHelper.execute(spInfo);
    }


}
