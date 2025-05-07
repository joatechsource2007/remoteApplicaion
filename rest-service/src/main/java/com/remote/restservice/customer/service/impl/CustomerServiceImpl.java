package com.remote.restservice.customer.service.impl;

import com.remote.restservice.customer.model.Customer_Params;
import com.remote.restservice.customer.model.SearchTable_Params;
import com.remote.restservice.customer.service.CustomerService;
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

@Service("CustomerService")
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private static final Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);

    private DbHelper dbHelper;

    @Autowired
    public CustomerServiceImpl(DbHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    /**
     * 351 : wsp_Get_Customer_FIND : 전체거래처 리스트 조건초회 (A전체, T.탱크, C.용기, M 검침,   TC,TM,CM, TCM)
     * @param params
     * @return
     * @throws SQLException
     */
    @Override
    public Map<String, Object> find(Map<String,Object> params) throws SQLException {
        List<String> listOfTableNames = List.of();
        Customer_Params pCustomer_Params = new Customer_Params();

        for (String parameter : params.keySet()) {

            logger.info("====== 351 =============================");
            logger.info("parameter : " + parameter);
            logger.info("========================================");


            SpParameter oldSpParameter =  pCustomer_Params.getSpParameterByName(SpParameter.SpType.QUERY, parameter);
            if(oldSpParameter==null) continue;
            oldSpParameter.setValue(params.get(parameter));
            pCustomer_Params.replaceSpParameterByName(SpParameter.SpType.QUERY,parameter,oldSpParameter);
        }
        logger.info("====== 351 =================================");
        SpInfo spInfo = SpInfo.builder()
                .spName("wsp_Get_Customer_FIND")
                .spParameterList(pCustomer_Params.getListOfQuerySpParameters())
                .tableNames(listOfTableNames)
                .build();
        logger.info(spInfo.toString());
        return dbHelper.execute(spInfo);
    }

    /**
     * 301	wsp	전체	wsp_CUST_INFO	거래처정보  (T,C,M) 탱크,용기,검침고객정보, 탱크리스트,검침리스트, 이미지 LIST
     * @param params
     * @return
     * @throws SQLException
     */
    @Override
    public Map<String, Object> custinfo(Map<String,Object> params) throws SQLException {
        List<String> listOfTableNames = List.of();
        Customer_Params pCustomer_Params = new Customer_Params();

        for (String parameter : params.keySet()) {

            logger.info("===== 301 ==============================");
            logger.info("parameter : " + parameter);
            logger.info("========================================");


            SpParameter oldSpParameter =  pCustomer_Params.getSpParameterByName(SpParameter.SpType.FIND, parameter);
            if(oldSpParameter==null) continue;
            oldSpParameter.setValue(params.get(parameter));
            pCustomer_Params.replaceSpParameterByName(SpParameter.SpType.FIND,parameter,oldSpParameter);
        }
        logger.info("==== 301 ===============================");
        SpInfo spInfo = SpInfo.builder()
                .spName("wsp_CUST_INFO")
                .spParameterList(pCustomer_Params.getListOfFindSpParameters())
                .tableNames(listOfTableNames)
                .build();
        logger.info(spInfo.toString());
        return dbHelper.execute(spInfo);
    }

    /**
     * 401	wsp	전체	wsp_select_Data	 테이블 데이터 조회
     * @param params
     * @return
     * @throws SQLException
     */
    @Override
    public Map<String, Object> selectdata(Map<String,Object> params) throws SQLException {
        List<String> listOfTableNames = List.of();
        SearchTable_Params pSearchTable_Params = new SearchTable_Params();

        for (String parameter : params.keySet()) {


            SpParameter oldSpParameter =  pSearchTable_Params.getSpParameterByName(SpParameter.SpType.QUERY, parameter);

            if(oldSpParameter==null) continue;
            oldSpParameter.setValue(params.get(parameter));
            pSearchTable_Params.replaceSpParameterByName(SpParameter.SpType.QUERY,parameter,oldSpParameter);
        }

        logger.info("===== 401 ==============================");
        logger.info("parameter : " + pSearchTable_Params.getListOfQuerySpParameters());
        logger.info("========================================");

        logger.info("==== 401 select Data ===============================");
        SpInfo spInfo = SpInfo.builder()
                .spName("wsp_Select_Data_Test")
                .spParameterList(pSearchTable_Params.getListOfQuerySpParameters())
                .tableNames(listOfTableNames)
                .build();
        logger.info(spInfo.toString());

        return dbHelper.execute(spInfo);
    }

    /**
     * 401	wsp	전체	wsp_modify_Data	 테이블 데이터 조회
     * @param params
     * @return
     * @throws SQLException
     */
    @Override
    public Map<String, Object> modifydata(Map<String,Object> params) throws SQLException {
        List<String> listOfTableNames = List.of();
        SearchTable_Params pSearchTable_Params = new SearchTable_Params();

        for (String parameter : params.keySet()) {

            logger.info("===== 401 modify data ==================");
            logger.info("parameter : " + parameter);
            logger.info("========================================");

            SpParameter oldSpParameter =  pSearchTable_Params.getSpParameterByName(SpParameter.SpType.ALL, parameter);
            if(oldSpParameter==null) continue;
            oldSpParameter.setValue(params.get(parameter));
            pSearchTable_Params.replaceSpParameterByName(SpParameter.SpType.ALL,parameter,oldSpParameter);
        }
        logger.info("==== 401 modify Data ========================");
        SpInfo spInfo = SpInfo.builder()
                .spName("wsp_Modify_Data")
                .spParameterList(pSearchTable_Params.getListOfAllSpParameters())
                .tableNames(listOfTableNames)
                .build();
        logger.info(spInfo.toString());
        return dbHelper.execute(spInfo);
    }

    /**
     * 402	wsp	전체		wsp_Generate_PK	Insert PK 항번생성
     * @param params
     * @return
     * @throws SQLException
     */
    @Override
    public Map<String, Object> generatepk(Map<String,Object> params) throws SQLException {
        List<String> listOfTableNames = List.of();
        SearchTable_Params pSearchTable_Params = new SearchTable_Params();

        for (String parameter : params.keySet()) {

            logger.info("===== 402 generate PK ==================");
            logger.info("parameter : " + parameter);
            logger.info("========================================");

            SpParameter oldSpParameter =  pSearchTable_Params.getSpParameterByName(SpParameter.SpType.FIND, parameter);
            if(oldSpParameter==null) continue;
            oldSpParameter.setValue(params.get(parameter));
            pSearchTable_Params.replaceSpParameterByName(SpParameter.SpType.FIND,parameter,oldSpParameter);
        }
        logger.info("==== 402 generate PK ========================");
        SpInfo spInfo = SpInfo.builder()
                .spName("wsp_Generate_PK")
                .spParameterList(pSearchTable_Params.getListOfFindSpParameters())
                .tableNames(listOfTableNames)
                .build();
        logger.info(spInfo.toString());
        return dbHelper.execute(spInfo);
    }


    /**
     * 300	wsp_Get_Base_Code	기초코드
     * @param params
     * @return
     * @throws SQLException
     */
    @Override
    public Map<String, Object> getbasecode(Map<String,Object> params) throws SQLException {
        List<String> listOfTableNames = List.of();
        Customer_Params pCustomer_Params = new Customer_Params();

        for (String parameter : params.keySet()) {

            logger.info("====== 300 =============================");
            logger.info("parameter : " + parameter);
            logger.info("========================================");


            SpParameter oldSpParameter =  pCustomer_Params.getSpParameterByName(SpParameter.SpType.INIT, parameter);
            if(oldSpParameter==null) continue;
            oldSpParameter.setValue(params.get(parameter));
            pCustomer_Params.replaceSpParameterByName(SpParameter.SpType.INIT,parameter,oldSpParameter);
        }
        logger.info("======= 300 =================================");
        SpInfo spInfo = SpInfo.builder()
                .spName("wsp_Get_Base_Code")
                .spParameterList(pCustomer_Params.getListOfInitSpParameters())
                .tableNames(listOfTableNames)
                .build();
        logger.info(spInfo.toString());
        return dbHelper.execute(spInfo);
    }
}
