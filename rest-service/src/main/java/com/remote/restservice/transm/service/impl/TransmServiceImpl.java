package com.remote.restservice.transm.service.impl;

import com.remote.restservice.transm.model.Transm_Params;
import com.remote.restservice.transm.service.TransmService;
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

@Service("TransmService")
@RequiredArgsConstructor
public class TransmServiceImpl implements TransmService {

    private static final Logger logger = LoggerFactory.getLogger(TransmServiceImpl.class);

    private DbHelper dbHelper;

    @Autowired
    public TransmServiceImpl(DbHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    /**
     * 331	wsp	검침	wsp_Week_Meter_V1	주간수신 검침
     * @param params
     * @return
     * @throws SQLException
     */
    @Override
    public Map<String, Object> transmlist(Map<String,Object> params) throws SQLException {
        List<String> listOfTableNames = List.of();
        Transm_Params pMeter_Params = new Transm_Params();

        for (String parameter : params.keySet()) {

            logger.info("===== 504 ==============================");
            logger.info("parameter : " + parameter);
            logger.info("========================================");


            SpParameter oldSpParameter =  pMeter_Params.getSpParameterByName(SpParameter.SpType.QUERY, parameter);
            if(oldSpParameter==null) continue;
            oldSpParameter.setValue(params.get(parameter));
            pMeter_Params.replaceSpParameterByName(SpParameter.SpType.QUERY,parameter,oldSpParameter);
        }
        logger.info("====== 504 ==================================");
        SpInfo spInfo = SpInfo.builder()
                .spName("wsp_TRANSM_LIST")
                .spParameterList(pMeter_Params.getListOfQuerySpParameters())
                .tableNames(listOfTableNames)
                .build();
        logger.info(spInfo.toString());
        return dbHelper.execute(spInfo);
    }

    /**
     * 331	wsp	검침	wsp_Week_Meter_V1	주간수신 검침
     * @param params
     * @return
     * @throws SQLException
     */
    @Override
    public Map<String, Object> otherlist(Map<String,Object> params) throws SQLException {
        List<String> listOfTableNames = List.of();
        Transm_Params pMeter_Params = new Transm_Params();

        for (String parameter : params.keySet()) {

            logger.info("===== 505 ==============================");
            logger.info("parameter : " + parameter);
            logger.info("========================================");


            SpParameter oldSpParameter =  pMeter_Params.getSpParameterByName(SpParameter.SpType.FIND, parameter);
            if(oldSpParameter==null) continue;
            oldSpParameter.setValue(params.get(parameter));
            pMeter_Params.replaceSpParameterByName(SpParameter.SpType.FIND,parameter,oldSpParameter);
        }
        logger.info("====== 505 ==================================");
        SpInfo spInfo = SpInfo.builder()
                .spName("wsp_OTHER_LIST")
                .spParameterList(pMeter_Params.getListOfFindSpParameters())
                .tableNames(listOfTableNames)
                .build();
        logger.info(spInfo.toString());
        return dbHelper.execute(spInfo);
    }

}
