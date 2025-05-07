package com.remote.restservice.tank.service.impl;

import com.remote.restservice.tank.model.Tank_Insp_Params;
import com.remote.restservice.tank.model.Tank_Cust_Params;
import com.remote.restservice.tank.model.Tank_Params;
import com.remote.restservice.tank.service.TankService;
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

@Service("TankService")
@RequiredArgsConstructor
public class TankServiceImpl implements TankService {

    private static final Logger logger = LoggerFactory.getLogger(TankServiceImpl.class);

    private DbHelper dbHelper;

    @Autowired
    public TankServiceImpl(DbHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    /**
     * 조회 - 쿼리조건에 따라 단건/다건 조회
     * @param params
     * @return
     * @throws SQLException
     */
    @Override
    public Map<String, Object> search(Map<String,Object> params) throws SQLException {
        List<String> listOfTableNames = List.of();
        Tank_Params pTank_Params = new Tank_Params();

        for (String parameter : params.keySet()) {

            logger.info("========================================");
            logger.info("parameter : " + parameter);
            logger.info("========================================");


            SpParameter oldSpParameter =  pTank_Params.getSpParameterByName(SpParameter.SpType.QUERY, parameter);
            if(oldSpParameter==null) continue;
            oldSpParameter.setValue(params.get(parameter));
            pTank_Params.replaceSpParameterByName(SpParameter.SpType.QUERY,parameter,oldSpParameter);
        }
        logger.info("========================================");
        SpInfo spInfo = SpInfo.builder()
                .spName("wsp_Week_Tank_V1")
                .spParameterList(pTank_Params.getListOfQuerySpParameters())
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
    public Map<String, Object> tcustRcvMonth(Map<String,Object> params) throws SQLException {
        List<String> listOfTableNames = List.of();
        Tank_Cust_Params pTank_Cust_Params = new Tank_Cust_Params();

        for (String parameter : params.keySet()) {

            logger.info("==== 312 ====================================");
            logger.info("parameter : " + parameter);
            logger.info("========================================");


            SpParameter oldSpParameter =  pTank_Cust_Params.getSpParameterByName(SpParameter.SpType.FIND, parameter);
            if(oldSpParameter==null) continue;
            oldSpParameter.setValue(params.get(parameter));
            pTank_Cust_Params.replaceSpParameterByName(SpParameter.SpType.FIND,parameter,oldSpParameter);
        }
        logger.info("=== 312 =====================================");
        SpInfo spInfo = SpInfo.builder()
                .spName("wsp_TCust_RCV_Month")
                .spParameterList(pTank_Cust_Params.getListOfFindSpParameters())
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
    public Map<String, Object> tcustRcvDetail(Map<String,Object> params) throws SQLException {
        List<String> listOfTableNames = List.of();
        Tank_Params pTank_Params = new Tank_Params();

        for (String parameter : params.keySet()) {

            logger.info("==== 313 ====================================");
            logger.info("parameter : " + parameter);
            logger.info("========================================");


            SpParameter oldSpParameter =  pTank_Params.getSpParameterByName(SpParameter.SpType.FIND, parameter);
            if(oldSpParameter==null) continue;
            oldSpParameter.setValue(params.get(parameter));
            pTank_Params.replaceSpParameterByName(SpParameter.SpType.FIND,parameter,oldSpParameter);
        }
        logger.info("=== 313 =====================================");
        SpInfo spInfo = SpInfo.builder()
                .spName("wsp_TCust_RCV_Detail")
                .spParameterList(pTank_Params.getListOfFindSpParameters())
                .tableNames(listOfTableNames)
                .build();
        logger.info(spInfo.toString());
        return dbHelper.execute(spInfo);
    }

    @Override
    public Map<String, Object> tcustRcvDetailV2(Map<String,Object> params) throws SQLException {
        List<String> listOfTableNames = List.of();
        Tank_Params pTank_Params = new Tank_Params();

        for (String parameter : params.keySet()) {

            logger.info("==== 314 ====================================");
            logger.info("parameter : " + parameter);
            logger.info("========================================");


            SpParameter oldSpParameter =  pTank_Params.getSpParameterByName(SpParameter.SpType.FIND2, parameter);
            if(oldSpParameter==null) continue;
            oldSpParameter.setValue(params.get(parameter));
            pTank_Params.replaceSpParameterByName(SpParameter.SpType.FIND2,parameter,oldSpParameter);
        }
        logger.info("=== 314 =====================================");
        SpInfo spInfo = SpInfo.builder()
                .spName("wsp_TCust_RCV_Detail_V2")
                .spParameterList(pTank_Params.getListOfFind2SpParameters())
                .tableNames(listOfTableNames)
                .build();
        logger.info(spInfo.toString());
        return dbHelper.execute(spInfo);
    }

    /**
     * 탱크 거래처 상세 수신이력 (달력, 집계)
     * @param params
     * @return
     * @throws SQLException
     */

    @Override
    public Map<String, Object> cuChargeList(Map<String,Object> params) throws SQLException {
        List<String> listOfTableNames = List.of();
        Tank_Cust_Params pTank_Params = new Tank_Cust_Params();

        for (String parameter : params.keySet()) {

            logger.info("==== 314 ====================================");
            logger.info("parameter : " + parameter);
            logger.info("========================================");


            SpParameter oldSpParameter =  pTank_Params.getSpParameterByName(SpParameter.SpType.FIND2, parameter);
            if(oldSpParameter==null) continue;
            oldSpParameter.setValue(params.get(parameter));
            pTank_Params.replaceSpParameterByName(SpParameter.SpType.FIND2,parameter,oldSpParameter);
        }
        logger.info("=== 315 =====================================");
        SpInfo spInfo = SpInfo.builder()
                .spName("wsp_CU_CHARGE_LIST")
                .spParameterList(pTank_Params.getListOfFind2SpParameters())
                .tableNames(listOfTableNames)
                .build();
        logger.info(spInfo.toString());
        return dbHelper.execute(spInfo);
    }

    /**
     * 501	w네	전체		wsp_TANK_INSP_LIST	탱크 리스트 (master)
     * @param params
     * @return
     * @throws SQLException
     */
    @Override
    public Map<String, Object> tankinsplist(Map<String,Object> params) throws SQLException {
        List<String> listOfTableNames = List.of();
        Tank_Insp_Params pTank_Params = new Tank_Insp_Params();

        for (String parameter : params.keySet()) {

            logger.info("==== 501 ===============================");
            logger.info("parameter : " + parameter);
            logger.info("========================================");


            SpParameter oldSpParameter =  pTank_Params.getSpParameterByName(SpParameter.SpType.FIND, parameter);
            if(oldSpParameter==null) continue;
            oldSpParameter.setValue(params.get(parameter));
            pTank_Params.replaceSpParameterByName(SpParameter.SpType.FIND,parameter,oldSpParameter);
        }
        logger.info("=== 501 =====================================");
        SpInfo spInfo = SpInfo.builder()
                .spName("wsp_TANK_INSP_LIST")
                .spParameterList(pTank_Params.getListOfFindSpParameters())
                .tableNames(listOfTableNames)
                .build();
        logger.info(spInfo.toString());
        return dbHelper.execute(spInfo);
    }

    /**
     * 502	wsp	전체		wsp_TANK_INSP_HIST	탱크 검사현황 (Detail)
     * @param params
     * @return
     * @throws SQLException
     */
    @Override
    public Map<String, Object> tankinsphist(Map<String,Object> params) throws SQLException {
        List<String> listOfTableNames = List.of();
        Tank_Insp_Params pTank_Params = new Tank_Insp_Params();

        for (String parameter : params.keySet()) {

            logger.info("==== 502 ===============================");
            logger.info("parameter : " + parameter);
            logger.info("========================================");

            SpParameter oldSpParameter =  pTank_Params.getSpParameterByName(SpParameter.SpType.FIND2, parameter);
            if(oldSpParameter==null) continue;
            oldSpParameter.setValue(params.get(parameter));
            pTank_Params.replaceSpParameterByName(SpParameter.SpType.FIND2,parameter,oldSpParameter);
        }
        logger.info("=== 502 =====================================");
        SpInfo spInfo = SpInfo.builder()
                .spName("wsp_TANK_INSP_HIST")
                .spParameterList(pTank_Params.getListOfFind2SpParameters())
                .tableNames(listOfTableNames)
                .build();
        logger.info(spInfo.toString());
        return dbHelper.execute(spInfo);
    }

    /**
     * 503	wsp	전체		wsp_TANK_INSP_HIST	탱크 검사현황 (Detail)
     * @param params
     * @return
     * @throws SQLException
     */
    @Override
    public Map<String, Object> tankinspselect(Map<String,Object> params) throws SQLException {
        List<String> listOfTableNames = List.of();
        Tank_Insp_Params pTank_Params = new Tank_Insp_Params();

        for (String parameter : params.keySet()) {

            logger.info("==== 503 ===============================");
            logger.info("parameter : " + parameter);
            logger.info("========================================");

            SpParameter oldSpParameter =  pTank_Params.getSpParameterByName(SpParameter.SpType.FIND2, parameter);
            if(oldSpParameter==null) continue;
            oldSpParameter.setValue(params.get(parameter));
            pTank_Params.replaceSpParameterByName(SpParameter.SpType.FIND2,parameter,oldSpParameter);
        }
        logger.info("=== 503 =====================================");
        SpInfo spInfo = SpInfo.builder()
                .spName("wsp_TANK_INSP_SELECT")
                .spParameterList(pTank_Params.getListOfFind2SpParameters())
                .tableNames(listOfTableNames)
                .build();
        logger.info(spInfo.toString());
        return dbHelper.execute(spInfo);
    }


}
