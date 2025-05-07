package com.remote.restservice.cylrinder.service.impl;

import com.remote.restservice.cylrinder.model.Cylrinder_Params;
import com.remote.restservice.cylrinder.service.CylrinderService;
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

@Service("CylrinderService")
@RequiredArgsConstructor
public class CylrinderServiceImpl implements CylrinderService {

    private static final Logger logger = LoggerFactory.getLogger(CylrinderServiceImpl.class);

    private DbHelper dbHelper;

    @Autowired
    public CylrinderServiceImpl(DbHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    /**
     * SP_321 : 용기 주간수신  (단일 업체코드 조회) (wsp_Week_Cylinder_V1)
     * @param params
     * @return
     * @throws SQLException
     */
    @Override
    public Map<String, Object> search(Map<String,Object> params) throws SQLException {
        List<String> listOfTableNames = List.of();
        Cylrinder_Params pCylrinder_Params = new Cylrinder_Params();

        for (String parameter : params.keySet()) {

            logger.info("===== 321-V1 ===========================");
            logger.info("parameter : " + parameter);
            logger.info("========================================");


            SpParameter oldSpParameter =  pCylrinder_Params.getSpParameterByName(SpParameter.SpType.QUERY, parameter);
            if(oldSpParameter==null) continue;
            oldSpParameter.setValue(params.get(parameter));
            pCylrinder_Params.replaceSpParameterByName(SpParameter.SpType.QUERY,parameter,oldSpParameter);
        }
        logger.info("====== 321-V1 ==================================");
        SpInfo spInfo = SpInfo.builder()
                .spName("wsp_Week_Cylinder_V1")
                .spParameterList(pCylrinder_Params.getListOfQuerySpParameters())
                .tableNames(listOfTableNames)
                .build();
        logger.info(spInfo.toString());
        return dbHelper.execute(spInfo);
    }

    /**
     * SP_321 : 집계건수, 리스트  (wsp_Week_Cylinder_V2)
     * @param params
     * @return
     * @throws SQLException
     */
    @Override
    public Map<String, Object> searchV2(Map<String,Object> params) throws SQLException {
        List<String> listOfTableNames = List.of();
        Cylrinder_Params pCylrinder_Params = new Cylrinder_Params();

        for (String parameter : params.keySet()) {

            logger.info("====== 321-V2 ==========================");
            logger.info("parameter : " + parameter);
            logger.info("========================================");


            SpParameter oldSpParameter =  pCylrinder_Params.getSpParameterByName(SpParameter.SpType.QUERY, parameter);
            if(oldSpParameter==null) continue;
            oldSpParameter.setValue(params.get(parameter));
            pCylrinder_Params.replaceSpParameterByName(SpParameter.SpType.QUERY,parameter,oldSpParameter);
        }
        logger.info("====== 321-v2 ===============================");
        SpInfo spInfo = SpInfo.builder()
                .spName("wsp_Week_Cylinder_V2")
                .spParameterList(pCylrinder_Params.getListOfQuerySpParameters())
                .tableNames(listOfTableNames)
                .build();
        logger.info(spInfo.toString());
        return dbHelper.execute(spInfo);
    }

    /**
     * SP_323 : 용기 거래처 상세 수신이력 (기간별 상세)
     * @param params
     * @return
     * @throws SQLException
     */
    @Override
    public Map<String, Object> jcustRcvDetail(Map<String,Object> params) throws SQLException {
        List<String> listOfTableNames = List.of();
        Cylrinder_Params pCylrinder_Params = new Cylrinder_Params();

        for (String parameter : params.keySet()) {

            logger.info("======= 323 ============================");
            logger.info("parameter : " + parameter);
            logger.info("========================================");


            SpParameter oldSpParameter =  pCylrinder_Params.getSpParameterByName(SpParameter.SpType.FIND, parameter);
            if(oldSpParameter==null) continue;
            oldSpParameter.setValue(params.get(parameter));
            pCylrinder_Params.replaceSpParameterByName(SpParameter.SpType.FIND,parameter,oldSpParameter);
        }
        logger.info("====== 323 ===============================");
        SpInfo spInfo = SpInfo.builder()
                .spName("wsp_JCUST_RCV_Detail_V2")
                .spParameterList(pCylrinder_Params.getListOfFindSpParameters())
                .tableNames(listOfTableNames)
                .build();
        logger.info(spInfo.toString());
        return dbHelper.execute(spInfo);
    }

    /**
     * SP_324 : 용기 거래처 정보 , 달력
     * @param params
     * @return
     * @throws SQLException
     */
    @Override
    public Map<String, Object> jcustRcvMonth(Map<String,Object> params) throws SQLException {
        List<String> listOfTableNames = List.of();
        Cylrinder_Params pCylrinder_Params = new Cylrinder_Params();

        for (String parameter : params.keySet()) {

            logger.info("====== 324 =============================");
            logger.info("parameter : " + parameter);
            logger.info("========================================");


            SpParameter oldSpParameter =  pCylrinder_Params.getSpParameterByName(SpParameter.SpType.FIND2, parameter);
            if(oldSpParameter==null) continue;
            oldSpParameter.setValue(params.get(parameter));
            pCylrinder_Params.replaceSpParameterByName(SpParameter.SpType.FIND2,parameter,oldSpParameter);
        }
        logger.info("======= 324 ================================");
        SpInfo spInfo = SpInfo.builder()
                .spName("wsp_JCust_RCV_Month")
                .spParameterList(pCylrinder_Params.getListOfFind2SpParameters())
                .tableNames(listOfTableNames)
                .build();
        logger.info(spInfo.toString());
        return dbHelper.execute(spInfo);
    }
}
