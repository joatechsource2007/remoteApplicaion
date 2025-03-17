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
     * 화면초기화 메서드로 9개 테이블 리턴함.
     * @return
     * @throws SQLException
     */
//    @Override
//    public Map<String, Object> init() throws SQLException {
//        List<String> listOfTableNames = List.of();
//        Tank_Params pTank_Params = new Tank_Params();
//
//        SpInfo spInfo = SpInfo.builder()
//                .spName("usp_F11BM010_FSConfigs")
//                .spParameterList(pTank_Params.getListOfInitSpParameters())
//                .tableNames(listOfTableNames)
//                .build();
//        logger.info(spInfo.toString());
//        return dbHelper.execute(spInfo, pTank_Params.getListOfInitSpParameters());
//    }

    /**
     * 조회 - 쿼리조건에 따라 단건/다건 조회
     * @param params
     * @return
     * @throws SQLException
     */
    @Override
    public Map<String, Object> search(Map<String,Object> params) throws SQLException {
        List<String> listOfTableNames = List.of();
        Meter_Params pMeter_Params = new Meter_Params();

        for (String parameter : params.keySet()) {

            logger.info("========================================");
            logger.info("parameter : " + parameter);
            logger.info("========================================");


            SpParameter oldSpParameter =  pMeter_Params.getSpParameterByName(SpParameter.SpType.QUERY, parameter);
            if(oldSpParameter==null) continue;
            oldSpParameter.setValue(params.get(parameter));
            pMeter_Params.replaceSpParameterByName(SpParameter.SpType.QUERY,parameter,oldSpParameter);
        }
        logger.info("========================================");
        SpInfo spInfo = SpInfo.builder()
                .spName("wsp_Week_METER_V1")
                .spParameterList(pMeter_Params.getListOfQuerySpParameters())
                .tableNames(listOfTableNames)
                .build();
        logger.info(spInfo.toString());
        return dbHelper.execute(spInfo);
    }

    /**
     * 조회 - 쿼리조건에 따라 단건/다건 조회
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

    @Override
    public Map<String, Object> getMeterGroup(Map<String,Object> params) throws SQLException {
        List<String> listOfTableNames = List.of();
        Meter_Params pMeter_Params = new Meter_Params();

        for (String parameter : params.keySet()) {

            logger.info("======== 332 ================================");
            logger.info("parameter : " + parameter);
            logger.info("========================================");


            SpParameter oldSpParameter =  pMeter_Params.getSpParameterByName(SpParameter.SpType.FIND2, parameter);
            if(oldSpParameter==null) continue;
            oldSpParameter.setValue(params.get(parameter));
            pMeter_Params.replaceSpParameterByName(SpParameter.SpType.FIND2,parameter,oldSpParameter);
        }
        logger.info("========================================");
        SpInfo spInfo = SpInfo.builder()
                .spName("wsp_Get_Meter_GROUP")
                .spParameterList(pMeter_Params.getListOfFind2SpParameters())
                .tableNames(listOfTableNames)
                .build();
        logger.info(spInfo.toString());
        return dbHelper.execute(spInfo);
    }

    /**
     * 수정
     * @param params
     * @return
     * @throws SQLException
     */
//    @Override
//    public Map<String, Object> update(Map<String,Object> params) throws SQLException {
//
//        F11BM010_Params pF11BM010_Params = new F11BM010_Params();
//
//        for (String parameter : params.keySet()) {
//            SpParameter oldSpParameter =  pF11BM010_Params.getSpParameterByName(SpParameter.SpType.ALL, parameter);
//            if(oldSpParameter!=null){
//                oldSpParameter.setValue(params.get(parameter));
//                pF11BM010_Params.replaceSpParameterByName(SpParameter.SpType.ALL,parameter,oldSpParameter);
//            }
//        }
//        SpInfo spInfo = SpInfo.builder()
//                .spName("usp_F11BM010_FSConfigs")
//                .spParameterList(pF11BM010_Params.getListOfAllSpParameters())
//                .build();
//        logger.info(spInfo.toString());
//        return dbHelper.execute(spInfo);
//    }

    /**
     * 수정과 동일
     * @param params
     * @return
     * @throws SQLException
     */
//    @Override
//    public Map<String, Object> insert(Map<String,Object> params) throws SQLException {
//        F11BM010_Params pF11BM010_Params = new F11BM010_Params();
//
//        for (String parameter : params.keySet()) {
//            SpParameter oldSpParameter =  pF11BM010_Params.getSpParameterByName(SpParameter.SpType.ALL, parameter);
//            if(oldSpParameter!=null){
//                oldSpParameter.setValue(params.get(parameter));
//                pF11BM010_Params.replaceSpParameterByName(SpParameter.SpType.ALL,parameter,oldSpParameter);
//            }
//        }
//        SpInfo spInfo = SpInfo.builder()
//                .spName("usp_F11BM010_FSConfigs")
//                .spParameterList(pF11BM010_Params.getListOfAllSpParameters())
//                .build();
//        logger.info(spInfo.toString());
//        return dbHelper.execute(spInfo);
//    }
}
