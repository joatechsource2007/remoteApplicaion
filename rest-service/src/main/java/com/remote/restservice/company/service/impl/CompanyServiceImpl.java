package com.remote.restservice.company.service.impl;

import com.remote.restservice.company.model.Company_Params;
import com.remote.restservice.company.service.CompanyService;
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

@Service("CompanyService")
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private static final Logger logger = LoggerFactory.getLogger(CompanyServiceImpl.class);

    private DbHelper dbHelper;

    @Autowired
    public CompanyServiceImpl(DbHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    /**
     * 조회 - 쿼리조건에 따라 단건/다건 조회
     * @param params
     * @return
     * @throws SQLException
     */
    @Override
    public Map<String, Object> find(Map<String,Object> params) throws SQLException {
        List<String> listOfTableNames = List.of();
        Company_Params pCompany_Params = new Company_Params();

        for (String parameter : params.keySet()) {

            logger.info("========================================");
            logger.info("parameter : " + parameter);
            logger.info("========================================");


            SpParameter oldSpParameter =  pCompany_Params.getSpParameterByName(SpParameter.SpType.QUERY, parameter);
            if(oldSpParameter==null) continue;
            oldSpParameter.setValue(params.get(parameter));
            pCompany_Params.replaceSpParameterByName(SpParameter.SpType.QUERY,parameter,oldSpParameter);
        }
        logger.info("========================================");
        SpInfo spInfo = SpInfo.builder()
                .spName("wsp_Company_Info")
                .spParameterList(pCompany_Params.getListOfQuerySpParameters())
                .tableNames(listOfTableNames)
                .build();
        logger.info(spInfo.toString());
        return dbHelper.execute(spInfo);
    }
}
