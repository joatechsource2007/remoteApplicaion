package com.remote.restservice.customer.model;
import com.remote.restservice.utils.database.SpParameter;
import lombok.Data;

import java.sql.JDBCType;
import java.util.ArrayList;
import java.util.List;

@Data
public class SearchTable_Params {

    private List<SpParameter> listOfQuerySpParameters = new ArrayList<SpParameter>();
    private List<SpParameter> listOfAllSpParameters = new ArrayList<SpParameter>();
    private List<SpParameter> listOfFindSpParameters = new ArrayList<SpParameter>();

    public SearchTable_Params() {
        // wsp_Select_Data
        {
            listOfQuerySpParameters.add(SpParameter.builder().name("TableName").direction(SpParameter.Direction.IN).jdbcType(JDBCType.VARCHAR).build());
            listOfQuerySpParameters.add(SpParameter.builder().name("PKValue").direction(SpParameter.Direction.IN).jdbcType(JDBCType.VARCHAR).build());
            listOfQuerySpParameters.add(SpParameter.builder().name("ChangedBy").direction(SpParameter.Direction.IN).jdbcType(JDBCType.VARCHAR).build());
            listOfQuerySpParameters.add(SpParameter.builder().name("USERNO").direction(SpParameter.Direction.IN).jdbcType(JDBCType.VARCHAR).build());
            listOfQuerySpParameters.add(SpParameter.builder().name("App_cert").direction(SpParameter.Direction.IN).jdbcType(JDBCType.VARCHAR).build());
        }
        // wsp_Modity_Data
        {
            listOfAllSpParameters.add(SpParameter.builder().name("PageID").direction(SpParameter.Direction.IN).jdbcType(JDBCType.VARCHAR).build());
            listOfAllSpParameters.add(SpParameter.builder().name("TableName").direction(SpParameter.Direction.IN).jdbcType(JDBCType.VARCHAR).build());
            listOfAllSpParameters.add(SpParameter.builder().name("Operation").direction(SpParameter.Direction.IN).jdbcType(JDBCType.VARCHAR).build());
            listOfAllSpParameters.add(SpParameter.builder().name("PKValue").direction(SpParameter.Direction.IN).jdbcType(JDBCType.VARCHAR).build());
            listOfAllSpParameters.add(SpParameter.builder().name("NewData").direction(SpParameter.Direction.IN).jdbcType(JDBCType.VARCHAR).build());
            listOfAllSpParameters.add(SpParameter.builder().name("ChangedBy").direction(SpParameter.Direction.IN).jdbcType(JDBCType.VARCHAR).build());
            listOfAllSpParameters.add(SpParameter.builder().name("USERNO").direction(SpParameter.Direction.IN).jdbcType(JDBCType.VARCHAR).build());
            listOfAllSpParameters.add(SpParameter.builder().name("App_cert").direction(SpParameter.Direction.IN).jdbcType(JDBCType.VARCHAR).build());
        }
        //
        {
            listOfFindSpParameters.add(SpParameter.builder().name("TableName").direction(SpParameter.Direction.IN).jdbcType(JDBCType.VARCHAR).build());
            listOfFindSpParameters.add(SpParameter.builder().name("Condition").direction(SpParameter.Direction.IN).jdbcType(JDBCType.VARCHAR).build());
            //listOfFindSpParameters.add(SpParameter.builder().name("PKValue").direction(SpParameter.Direction.INOUT).jdbcType(JDBCType.VARCHAR).build());
        }


    }

    public SpParameter getSpParameterByName(SpParameter.SpType spType, String name) {

        if(spType.equals(SpParameter.SpType.QUERY)) {
            return listOfQuerySpParameters.stream().filter(spParameter -> name.equals(spParameter.getName())).findFirst().orElse(null);
        }else if(spType.equals(SpParameter.SpType.ALL)){
            return listOfAllSpParameters.stream().filter(spParameter -> name.equals(spParameter.getName())).findFirst().orElse(null);
        }else if(spType.equals(SpParameter.SpType.FIND)){
            return listOfFindSpParameters.stream().filter(spParameter -> name.equals(spParameter.getName())).findFirst().orElse(null);
        }
        return null;
    }

    public void replaceSpParameterByName(SpParameter.SpType spType, String name, SpParameter newSpParameter){
        SpParameter oldSpParameter = getSpParameterByName(spType, name);
        int index=-1;

        try {
            if(spType.equals(SpParameter.SpType.QUERY)){
                index = listOfQuerySpParameters.indexOf(oldSpParameter);
                if(index >-1)
                    listOfQuerySpParameters.set(index, newSpParameter);
            }else if(spType.equals(SpParameter.SpType.ALL)){
                index = listOfAllSpParameters.indexOf(oldSpParameter);
                if(index >-1)
                    listOfAllSpParameters.set(index, newSpParameter);
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
