package com.remote.restservice.meter.service.impl;

import com.remote.restservice.meter.model.Meter_Params;
import com.remote.restservice.meter.service.MeterService;
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

@Service("MeterService")
@RequiredArgsConstructor
public class MeterServiceImpl implements MeterService {

    private static final Logger logger = LoggerFactory.getLogger(MeterServiceImpl.class);

    private DbHelper dbHelper;

    @Autowired
    public MeterServiceImpl(DbHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    /**
     * 331	wsp	검침	wsp_Week_Meter_V1	주간수신 검침
     * @param params
     * @return
     * @throws SQLException
     */
    @Override
    public Map<String, Object> search(Map<String,Object> params) throws SQLException {
        List<String> listOfTableNames = List.of();
        Meter_Params pMeter_Params = new Meter_Params();

        for (String parameter : params.keySet()) {

            logger.info("===== 331 ==============================");
            logger.info("parameter : " + parameter);
            logger.info("========================================");


            SpParameter oldSpParameter =  pMeter_Params.getSpParameterByName(SpParameter.SpType.QUERY, parameter);
            if(oldSpParameter==null) continue;
            oldSpParameter.setValue(params.get(parameter));
            pMeter_Params.replaceSpParameterByName(SpParameter.SpType.QUERY,parameter,oldSpParameter);
        }
        logger.info("====== 331 ==================================");
        SpInfo spInfo = SpInfo.builder()
                .spName("wsp_Week_METER_V1")
                .spParameterList(pMeter_Params.getListOfQuerySpParameters())
                .tableNames(listOfTableNames)
                .build();
        logger.info(spInfo.toString());
        return dbHelper.execute(spInfo);
    }

    /**
     * 332 	검침	wsp_Get_Meter_DETAIL	검침 상세수신이력, (실검침 이력 포함)
     * @param params
     * @return
     * @throws SQLException
     */
    @Override
    public Map<String, Object> getMeterDetail(Map<String,Object> params) throws SQLException {
        List<String> listOfTableNames = List.of();
        Meter_Params pMeter_Params = new Meter_Params();

        for (String parameter : params.keySet()) {

            logger.info("======== 332 ================================");
            logger.info("parameter : " + parameter);
            logger.info("========================================");


            SpParameter oldSpParameter =  pMeter_Params.getSpParameterByName(SpParameter.SpType.FIND, parameter);
            if(oldSpParameter==null) continue;
            oldSpParameter.setValue(params.get(parameter));
            pMeter_Params.replaceSpParameterByName(SpParameter.SpType.FIND,parameter,oldSpParameter);
        }
        logger.info("============ 332 ============================");
        SpInfo spInfo = SpInfo.builder()
                .spName("wsp_Get_Meter_DETAIL")
                .spParameterList(pMeter_Params.getListOfFindSpParameters())
                .tableNames(listOfTableNames)
                .build();
        logger.info(spInfo.toString());
        return dbHelper.execute(spInfo);
    }

    /**
     * 332 	wsp	검침	wsp_Get_Meter_GROUP	검침 (일,주,월) 그룹별 사용량 집계 (실검침 구분포함)
     * @param params
     * @return
     * @throws SQLException
     */
    @Override
    public Map<String, Object> getMeterGroup(Map<String,Object> params) throws SQLException {
        List<String> listOfTableNames = List.of();
        Meter_Params pMeter_Params = new Meter_Params();

        for (String parameter : params.keySet()) {

            logger.info("======== 332 ===========================");
            logger.info("parameter : " + parameter);
            logger.info("========================================");


            SpParameter oldSpParameter =  pMeter_Params.getSpParameterByName(SpParameter.SpType.FIND2, parameter);
            if(oldSpParameter==null) continue;
            oldSpParameter.setValue(params.get(parameter));
            pMeter_Params.replaceSpParameterByName(SpParameter.SpType.FIND2,parameter,oldSpParameter);
        }
        logger.info("====== 332 ==================================");
        SpInfo spInfo = SpInfo.builder()
                .spName("wsp_Get_Meter_GROUP")
                .spParameterList(pMeter_Params.getListOfFind2SpParameters())
                .tableNames(listOfTableNames)
                .build();
        logger.info(spInfo.toString());
        return dbHelper.execute(spInfo);
    }

    /**
     * 332 	wsp	검침	wsp_Get_Meter_GROUP	검침 (일,주,월) 그룹별 사용량 집계 (실검침 구분포함)
     * @param params
     * @return
     * @throws SQLException
     */
    @Override
    public Map<String, Object> getMeterGroupApp(Map<String,Object> params) throws SQLException {
        List<String> listOfTableNames = List.of();
        Meter_Params pMeter_Params = new Meter_Params();

        for (String parameter : params.keySet()) {

            logger.info("======== 332 ===========================");
            logger.info("parameter : " + parameter);
            logger.info("========================================");


            SpParameter oldSpParameter =  pMeter_Params.getSpParameterByName(SpParameter.SpType.FIND2, parameter);
            if(oldSpParameter==null) continue;
            oldSpParameter.setValue(params.get(parameter));
            pMeter_Params.replaceSpParameterByName(SpParameter.SpType.FIND2,parameter,oldSpParameter);
        }
        logger.info("====== 332 ==================================");
        SpInfo spInfo = SpInfo.builder()
                .spName("wsp_Get_Meter_GROUP_App")
                .spParameterList(pMeter_Params.getListOfFind2SpParameters())
                .tableNames(listOfTableNames)
                .build();
        logger.info(spInfo.toString());
        return dbHelper.execute(spInfo);
    }


    /**
     * 333	wsp	검침	wsp_MCUST_RCV_AVG	검침 거래처 정보 , 최종검침
     * @param params
     * @return
     * @throws SQLException
     */
    @Override
    public Map<String, Object> mcustRcvAvg(Map<String,Object> params) throws SQLException {
        List<String> listOfTableNames = List.of();
        Meter_Params pMeter_Params = new Meter_Params();

        for (String parameter : params.keySet()) {

            logger.info("======== 333 ===========================");
            logger.info("parameter : " + parameter);
            logger.info("========================================");


            SpParameter oldSpParameter =  pMeter_Params.getSpParameterByName(SpParameter.SpType.FIND3, parameter);
            if(oldSpParameter==null) continue;
            oldSpParameter.setValue(params.get(parameter));
            pMeter_Params.replaceSpParameterByName(SpParameter.SpType.FIND3,parameter,oldSpParameter);
        }
        logger.info("====== 333 ==================================");
        SpInfo spInfo = SpInfo.builder()
                .spName("wsp_MCUST_RCV_AVG")
                .spParameterList(pMeter_Params.getListOfFind3SpParameters())
                .tableNames(listOfTableNames)
                .build();
        logger.info(spInfo.toString());
        return dbHelper.execute(spInfo);
    }
}
