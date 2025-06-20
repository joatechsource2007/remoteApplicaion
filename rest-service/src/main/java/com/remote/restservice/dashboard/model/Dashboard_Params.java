package com.remote.restservice.dashboard.model;

import com.remote.restservice.utils.database.SpParameter;
import lombok.Data;

import java.sql.JDBCType;
import java.util.ArrayList;
import java.util.List;

@Data
public class Dashboard_Params {

    private List<SpParameter> listOfQuerySpParameters = new ArrayList<SpParameter>();
    private List<SpParameter> listOfFindSpParameters = new ArrayList<SpParameter>();

    //생성자
    public Dashboard_Params(){
        //검색
        {
            listOfQuerySpParameters.add(SpParameter.builder().name("p_C_MNG_NO").direction(SpParameter.Direction.IN).jdbcType(JDBCType.VARCHAR).build());
            listOfQuerySpParameters.add(SpParameter.builder().name("p_DATA").direction(SpParameter.Direction.IN).jdbcType(JDBCType.VARCHAR).build());
            listOfQuerySpParameters.add(SpParameter.builder().name("p_SW_CODE").direction(SpParameter.Direction.IN).jdbcType(JDBCType.VARCHAR).build());
        }
        {
            listOfFindSpParameters.add(SpParameter.builder().name("p_USERNO").direction(SpParameter.Direction.IN).jdbcType(JDBCType.VARCHAR).build());
        }

        //초기화
        //listOfInitSpParameters.add(SpParameter.builder().name("OPT").value(60).direction(SpParameter.Direction.IN).jdbcType(JDBCType.INTEGER).build());
    }

    public SpParameter getSpParameterByName(SpParameter.SpType spType, String name) {
        if(spType.equals(SpParameter.SpType.QUERY)){
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
            if(spType.equals(SpParameter.SpType.QUERY)){
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
