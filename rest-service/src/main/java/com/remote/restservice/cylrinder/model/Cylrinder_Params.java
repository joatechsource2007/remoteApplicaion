package com.remote.restservice.cylrinder.model;

import com.remote.restservice.utils.database.SpParameter;
import lombok.Data;

import java.sql.JDBCType;
import java.util.ArrayList;
import java.util.List;

@Data
public class Cylrinder_Params {

    private List<SpParameter> listOfAllSpParameters = new ArrayList<SpParameter>();
    private List<SpParameter> listOfQuerySpParameters = new ArrayList<SpParameter>();
    private List<SpParameter> listOfInitSpParameters = new ArrayList<SpParameter>();
    private List<SpParameter> listOfFindSpParameters = new ArrayList<SpParameter>();

    //생성자
    public Cylrinder_Params(){
        //전부
        {
//        listOfAllSpParameters.add(SpParameter.builder().name("QueryFSCode").direction(SpParameter.Direction.IN).jdbcType(JDBCType.INTEGER).build());
//        listOfAllSpParameters.add(SpParameter.builder().name("QueryFSName").direction(SpParameter.Direction.IN).jdbcType(JDBCType.VARCHAR).build());
//        listOfAllSpParameters.add(SpParameter.builder().name("QueryAllYN").direction(SpParameter.Direction.IN).jdbcType(JDBCType.CHAR).build());
//        listOfAllSpParameters.add(SpParameter.builder().name("FSCode").direction(SpParameter.Direction.INOUT).jdbcType(JDBCType.INTEGER).build());
//        listOfAllSpParameters.add(SpParameter.builder().name("FSName").direction(SpParameter.Direction.IN).jdbcType(JDBCType.VARCHAR).build());
//        listOfAllSpParameters.add(SpParameter.builder().name("FSType").direction(SpParameter.Direction.IN).jdbcType(JDBCType.INTEGER).build());
//        listOfAllSpParameters.add(SpParameter.builder().name("RentType").direction(SpParameter.Direction.IN).jdbcType(JDBCType.INTEGER).build());
//        listOfAllSpParameters.add(SpParameter.builder().name("LesseeName").direction(SpParameter.Direction.IN).jdbcType(JDBCType.VARCHAR).build());
//        listOfAllSpParameters.add(SpParameter.builder().name("MainFSCode").direction(SpParameter.Direction.IN).jdbcType(JDBCType.INTEGER).build());
//        listOfAllSpParameters.add(SpParameter.builder().name("DeptID").direction(SpParameter.Direction.IN).jdbcType(JDBCType.VARCHAR).build());
//        listOfAllSpParameters.add(SpParameter.builder().name("ENISCustID").direction(SpParameter.Direction.IN).jdbcType(JDBCType.INTEGER).build());
//        listOfAllSpParameters.add(SpParameter.builder().name("ENISCustID_E1").direction(SpParameter.Direction.IN).jdbcType(JDBCType.INTEGER).build());
//        listOfAllSpParameters.add(SpParameter.builder().name("POSType").direction(SpParameter.Direction.IN).jdbcType(JDBCType.INTEGER).build());
//        listOfAllSpParameters.add(SpParameter.builder().name("FillingCount").direction(SpParameter.Direction.IN).jdbcType(JDBCType.TINYINT).build());
//        listOfAllSpParameters.add(SpParameter.builder().name("Permit_Qty").direction(SpParameter.Direction.IN).jdbcType(JDBCType.NUMERIC).build());
//        listOfAllSpParameters.add(SpParameter.builder().name("Permit_Amt").direction(SpParameter.Direction.IN).jdbcType(JDBCType.NUMERIC).build());
//        listOfAllSpParameters.add(SpParameter.builder().name("ShippingBlockType").direction(SpParameter.Direction.IN).jdbcType(JDBCType.INTEGER).build());
//        listOfAllSpParameters.add(SpParameter.builder().name("ShippingBlockDate").direction(SpParameter.Direction.IN).jdbcType(JDBCType.TIMESTAMP).build());
//        listOfAllSpParameters.add(SpParameter.builder().name("PrinterPort_Req").direction(SpParameter.Direction.IN).jdbcType(JDBCType.TINYINT).build());
//        listOfAllSpParameters.add(SpParameter.builder().name("PrinterPort_Sale").direction(SpParameter.Direction.IN).jdbcType(JDBCType.TINYINT).build());
//        listOfAllSpParameters.add(SpParameter.builder().name("BasicPriceType_C3").direction(SpParameter.Direction.IN).jdbcType(JDBCType.INTEGER).build());
//        listOfAllSpParameters.add(SpParameter.builder().name("BasicPriceType_C4").direction(SpParameter.Direction.IN).jdbcType(JDBCType.INTEGER).build());
//        listOfAllSpParameters.add(SpParameter.builder().name("RebateYN").direction(SpParameter.Direction.IN).jdbcType(JDBCType.CHAR).build());
//        listOfAllSpParameters.add(SpParameter.builder().name("RebateCalType").direction(SpParameter.Direction.IN).jdbcType(JDBCType.INTEGER).build());
//        listOfAllSpParameters.add(SpParameter.builder().name("PointYN").direction(SpParameter.Direction.IN).jdbcType(JDBCType.CHAR).build());
//        listOfAllSpParameters.add(SpParameter.builder().name("KeepTicketYN").direction(SpParameter.Direction.IN).jdbcType(JDBCType.CHAR).build());
//        listOfAllSpParameters.add(SpParameter.builder().name("GSSystemYN").direction(SpParameter.Direction.IN).jdbcType(JDBCType.CHAR).build());
//        listOfAllSpParameters.add(SpParameter.builder().name("ReceiptSlipYN").direction(SpParameter.Direction.IN).jdbcType(JDBCType.CHAR).build());
//        listOfAllSpParameters.add(SpParameter.builder().name("FRFL_PrintYN").direction(SpParameter.Direction.IN).jdbcType(JDBCType.CHAR).build());
//        listOfAllSpParameters.add(SpParameter.builder().name("DirectInput_ReceiveAmt").direction(SpParameter.Direction.IN).jdbcType(JDBCType.CHAR).build());
//        listOfAllSpParameters.add(SpParameter.builder().name("POS_IP").direction(SpParameter.Direction.IN).jdbcType(JDBCType.VARCHAR).build());
//        listOfAllSpParameters.add(SpParameter.builder().name("ADP_IP").direction(SpParameter.Direction.IN).jdbcType(JDBCType.VARCHAR).build());
//        listOfAllSpParameters.add(SpParameter.builder().name("OpenDate").direction(SpParameter.Direction.IN).jdbcType(JDBCType.TIMESTAMP).build());
//        listOfAllSpParameters.add(SpParameter.builder().name("CloseDate").direction(SpParameter.Direction.IN).jdbcType(JDBCType.TIMESTAMP).build());
//        listOfAllSpParameters.add(SpParameter.builder().name("RentDate").direction(SpParameter.Direction.IN).jdbcType(JDBCType.TIMESTAMP).build());
//        listOfAllSpParameters.add(SpParameter.builder().name("AreaCode").direction(SpParameter.Direction.IN).jdbcType(JDBCType.INTEGER).build());
//        listOfAllSpParameters.add(SpParameter.builder().name("ZipCode").direction(SpParameter.Direction.IN).jdbcType(JDBCType.VARCHAR).build());
//        listOfAllSpParameters.add(SpParameter.builder().name("Address").direction(SpParameter.Direction.IN).jdbcType(JDBCType.VARCHAR).build());
//        listOfAllSpParameters.add(SpParameter.builder().name("TelNo").direction(SpParameter.Direction.IN).jdbcType(JDBCType.VARCHAR).build());
//        listOfAllSpParameters.add(SpParameter.builder().name("FaxNo").direction(SpParameter.Direction.IN).jdbcType(JDBCType.VARCHAR).build());
//        listOfAllSpParameters.add(SpParameter.builder().name("EMail").direction(SpParameter.Direction.IN).jdbcType(JDBCType.VARCHAR).build());
//        listOfAllSpParameters.add(SpParameter.builder().name("FSYN").direction(SpParameter.Direction.IN).jdbcType(JDBCType.CHAR).build());
//        listOfAllSpParameters.add(SpParameter.builder().name("UserID").direction(SpParameter.Direction.IN).jdbcType(JDBCType.VARCHAR).build());
//        listOfAllSpParameters.add(SpParameter.builder().name("ReportStartDate").direction(SpParameter.Direction.IN).jdbcType(JDBCType.TIMESTAMP).build());
//        listOfAllSpParameters.add(SpParameter.builder().name("PrintActiveYN").direction(SpParameter.Direction.IN).jdbcType(JDBCType.CHAR).build());
//        listOfAllSpParameters.add(SpParameter.builder().name("OPT").direction(SpParameter.Direction.IN).jdbcType(JDBCType.INTEGER).build());
        }
        //검색
        {
            listOfQuerySpParameters.add(SpParameter.builder().name("p_C_MNG_NO").direction(SpParameter.Direction.IN).jdbcType(JDBCType.VARCHAR).build());
            listOfQuerySpParameters.add(SpParameter.builder().name("p_FIND_TEXT").direction(SpParameter.Direction.IN).jdbcType(JDBCType.VARCHAR).build());
            listOfQuerySpParameters.add(SpParameter.builder().name("p_CUST_CODE").direction(SpParameter.Direction.IN).jdbcType(JDBCType.VARCHAR).build());
            listOfQuerySpParameters.add(SpParameter.builder().name("p_SW_CODE").direction(SpParameter.Direction.IN).jdbcType(JDBCType.VARCHAR).build());
            listOfQuerySpParameters.add(SpParameter.builder().name("p_C_AREA_CODE").direction(SpParameter.Direction.IN).jdbcType(JDBCType.VARCHAR).build());
            listOfQuerySpParameters.add(SpParameter.builder().name("p_C_TYPE_CODE").direction(SpParameter.Direction.IN).jdbcType(JDBCType.VARCHAR).build());
            listOfQuerySpParameters.add(SpParameter.builder().name("p_TURM_Day").direction(SpParameter.Direction.IN).jdbcType(JDBCType.INTEGER).build());
            listOfQuerySpParameters.add(SpParameter.builder().name("p_CUST_ALL_YN").direction(SpParameter.Direction.IN).jdbcType(JDBCType.VARCHAR).build());
            listOfQuerySpParameters.add(SpParameter.builder().name("p_DEL_YN").direction(SpParameter.Direction.IN).jdbcType(JDBCType.VARCHAR).build());
            listOfQuerySpParameters.add(SpParameter.builder().name("p_ORDERBY").direction(SpParameter.Direction.IN).jdbcType(JDBCType.INTEGER).build());
            listOfQuerySpParameters.add(SpParameter.builder().name("p_GPS_FIND_YN").direction(SpParameter.Direction.IN).jdbcType(JDBCType.VARCHAR).build());
            listOfQuerySpParameters.add(SpParameter.builder().name("p_GPS_EXTENT").direction(SpParameter.Direction.IN).jdbcType(JDBCType.INTEGER).build());
            listOfQuerySpParameters.add(SpParameter.builder().name("p_GPS_LAT").direction(SpParameter.Direction.IN).jdbcType(JDBCType.VARCHAR).build());
            listOfQuerySpParameters.add(SpParameter.builder().name("p_GPS_LONG").direction(SpParameter.Direction.IN).jdbcType(JDBCType.VARCHAR).build());
            listOfQuerySpParameters.add(SpParameter.builder().name("p_USERNO").direction(SpParameter.Direction.IN).jdbcType(JDBCType.VARCHAR).build());
            listOfQuerySpParameters.add(SpParameter.builder().name("p_app_cert").direction(SpParameter.Direction.IN).jdbcType(JDBCType.VARCHAR).build());


//            listOfQuerySpParameters.add(SpParameter.builder().name("pCMngNo").direction(SpParameter.Direction.IN).jdbcType(JDBCType.VARCHAR).build());
//            listOfQuerySpParameters.add(SpParameter.builder().name("pCustCode").direction(SpParameter.Direction.IN).jdbcType(JDBCType.VARCHAR).build());
//            listOfQuerySpParameters.add(SpParameter.builder().name("pCustCode").direction(SpParameter.Direction.IN).jdbcType(JDBCType.VARCHAR).build());
//            listOfQuerySpParameters.add(SpParameter.builder().name("pSwCode").direction(SpParameter.Direction.IN).jdbcType(JDBCType.VARCHAR).build());
//            listOfQuerySpParameters.add(SpParameter.builder().name("pCAreaCode").direction(SpParameter.Direction.IN).jdbcType(JDBCType.VARCHAR).build());
//            listOfQuerySpParameters.add(SpParameter.builder().name("pCTypeCode").direction(SpParameter.Direction.IN).jdbcType(JDBCType.VARCHAR).build());
//            listOfQuerySpParameters.add(SpParameter.builder().name("p_TURM_Day").direction(SpParameter.Direction.IN).jdbcType(JDBCType.INTEGER).build());
//            listOfQuerySpParameters.add(SpParameter.builder().name("pAllYN").direction(SpParameter.Direction.IN).jdbcType(JDBCType.VARCHAR).build());
//            listOfQuerySpParameters.add(SpParameter.builder().name("pDelYN").direction(SpParameter.Direction.IN).jdbcType(JDBCType.VARCHAR).build());
//            listOfQuerySpParameters.add(SpParameter.builder().name("pOrderby").direction(SpParameter.Direction.IN).jdbcType(JDBCType.INTEGER).build());
//            listOfQuerySpParameters.add(SpParameter.builder().name("pGpsFindYN").direction(SpParameter.Direction.IN).jdbcType(JDBCType.VARCHAR).build());
//            listOfQuerySpParameters.add(SpParameter.builder().name("pGpsExtent").direction(SpParameter.Direction.IN).jdbcType(JDBCType.INTEGER).build());
//            listOfQuerySpParameters.add(SpParameter.builder().name("pGpsLat").direction(SpParameter.Direction.IN).jdbcType(JDBCType.VARCHAR).build());
//            listOfQuerySpParameters.add(SpParameter.builder().name("pGpsLong").direction(SpParameter.Direction.IN).jdbcType(JDBCType.VARCHAR).build());
//            listOfQuerySpParameters.add(SpParameter.builder().name("pUserno").direction(SpParameter.Direction.IN).jdbcType(JDBCType.VARCHAR).build());
//            listOfQuerySpParameters.add(SpParameter.builder().name("pAppCert").direction(SpParameter.Direction.IN).jdbcType(JDBCType.VARCHAR).build());

        }
        // 상세 정보 , 달력 정보
        {
            listOfFindSpParameters.add(SpParameter.builder().name("p_C_MNG_NO").direction(SpParameter.Direction.IN).jdbcType(JDBCType.VARCHAR).build());
            listOfFindSpParameters.add(SpParameter.builder().name("p_CUST_CODE").direction(SpParameter.Direction.IN).jdbcType(JDBCType.VARCHAR).build());
            listOfFindSpParameters.add(SpParameter.builder().name("p_TRANSM_CD").direction(SpParameter.Direction.IN).jdbcType(JDBCType.VARCHAR).build());
            listOfFindSpParameters.add(SpParameter.builder().name("p_DATE_F").direction(SpParameter.Direction.IN).jdbcType(JDBCType.VARCHAR).build());
            listOfFindSpParameters.add(SpParameter.builder().name("p_DATE_T").direction(SpParameter.Direction.IN).jdbcType(JDBCType.VARCHAR).build());
            listOfFindSpParameters.add(SpParameter.builder().name("p_TURM_Day").direction(SpParameter.Direction.IN).jdbcType(JDBCType.VARCHAR).build());
            listOfFindSpParameters.add(SpParameter.builder().name("p_OrderBY").direction(SpParameter.Direction.IN).jdbcType(JDBCType.VARCHAR).build());
        }

        //초기화
        //listOfInitSpParameters.add(SpParameter.builder().name("OPT").value(60).direction(SpParameter.Direction.IN).jdbcType(JDBCType.INTEGER).build());
    }

    public SpParameter getSpParameterByName(SpParameter.SpType spType, String name) {
        if(spType.equals(SpParameter.SpType.ALL)){
            return listOfAllSpParameters.stream().filter(spParameter -> name.equals(spParameter.getName())).findFirst().orElse(null);
        }else if(spType.equals(SpParameter.SpType.INIT)){
            return listOfInitSpParameters.stream().filter(spParameter -> name.equals(spParameter.getName())).findFirst().orElse(null);
        }else if(spType.equals(SpParameter.SpType.QUERY)){
            return listOfQuerySpParameters.stream().filter(spParameter -> name.equals(spParameter.getName())).findFirst().orElse(null);
        }else if(spType.equals(SpParameter.SpType.FIND)){
            return listOfFindSpParameters.stream().filter(spParameter -> name.equals(spParameter.getName())).findFirst().orElse(null);
        }
        return null;
    }

    public void replaceSpParameterByName(SpParameter.SpType spType, String name, SpParameter newSpParameter){
        SpParameter oldSpParameter = getSpParameterByName(spType, name);
        int index=-1;

        try {
            if(spType.equals(SpParameter.SpType.ALL)){
                index = listOfAllSpParameters.indexOf(oldSpParameter);
                if(index >-1)
                    listOfAllSpParameters.set(index, newSpParameter);
            }else if(spType.equals(SpParameter.SpType.INIT)){
                index = listOfInitSpParameters.indexOf(oldSpParameter);
                if(index >-1)
                    listOfInitSpParameters.set(index, newSpParameter);
            }else if(spType.equals(SpParameter.SpType.QUERY)){
                index = listOfQuerySpParameters.indexOf(oldSpParameter);
                if(index >-1)
                    listOfQuerySpParameters.set(index, newSpParameter);
            }else if(spType.equals(SpParameter.SpType.FIND)){
                index = listOfFindSpParameters.indexOf(oldSpParameter);
                if(index >-1)
                    listOfFindSpParameters.set(index, newSpParameter);
            }
      }catch (NullPointerException e){
            System.out.println("오류가 발생하였습니다.");
            throw e;
        }
    }

}
