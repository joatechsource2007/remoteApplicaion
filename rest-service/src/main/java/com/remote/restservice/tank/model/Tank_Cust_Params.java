package com.remote.restservice.tank.model;

import com.remote.restservice.utils.database.SpParameter;
import lombok.Data;

import java.sql.JDBCType;
import java.util.ArrayList;
import java.util.List;

@Data
public class Tank_Cust_Params {

    private List<SpParameter> listOfFindSpParameters = new ArrayList<SpParameter>();
    private List<SpParameter> listOfFind2SpParameters = new ArrayList<SpParameter>();

    public Tank_Cust_Params() {

        {
            listOfFindSpParameters.add(SpParameter.builder().name("p_C_MNG_NO").direction(SpParameter.Direction.IN).jdbcType(JDBCType.VARCHAR).build());
            listOfFindSpParameters.add(SpParameter.builder().name("p_CUST_CODE").direction(SpParameter.Direction.IN).jdbcType(JDBCType.VARCHAR).build());
            listOfFindSpParameters.add(SpParameter.builder().name("p_DATE_F").direction(SpParameter.Direction.IN).jdbcType(JDBCType.VARCHAR).build());
            listOfFindSpParameters.add(SpParameter.builder().name("p_DATE_T").direction(SpParameter.Direction.IN).jdbcType(JDBCType.VARCHAR).build());
            listOfFindSpParameters.add(SpParameter.builder().name("p_TURM_Day").direction(SpParameter.Direction.IN).jdbcType(JDBCType.VARCHAR).build());
        }

        {
            listOfFind2SpParameters.add(SpParameter.builder().name("p_C_MNG_NO").direction(SpParameter.Direction.IN).jdbcType(JDBCType.VARCHAR).build());
            listOfFind2SpParameters.add(SpParameter.builder().name("p_CUST_CODE").direction(SpParameter.Direction.IN).jdbcType(JDBCType.VARCHAR).build());
            listOfFind2SpParameters.add(SpParameter.builder().name("p_Sale_TYPE").direction(SpParameter.Direction.IN).jdbcType(JDBCType.VARCHAR).build());
            listOfFind2SpParameters.add(SpParameter.builder().name("p_DATE_F").direction(SpParameter.Direction.IN).jdbcType(JDBCType.VARCHAR).build());
            listOfFind2SpParameters.add(SpParameter.builder().name("p_DATE_T").direction(SpParameter.Direction.IN).jdbcType(JDBCType.VARCHAR).build());
            listOfFind2SpParameters.add(SpParameter.builder().name("p_TURM_Day").direction(SpParameter.Direction.IN).jdbcType(JDBCType.VARCHAR).build());
        }

    }

    public SpParameter getSpParameterByName(SpParameter.SpType spType, String name) {
        if(spType.equals(SpParameter.SpType.FIND)){
            return listOfFindSpParameters.stream().filter(spParameter -> name.equals(spParameter.getName())).findFirst().orElse(null);
        } else if (spType.equals(SpParameter.SpType.FIND2)){
            return listOfFind2SpParameters.stream().filter(spParameter -> name.equals(spParameter.getName())).findFirst().orElse(null);
        }
        return null;
    }

    public void replaceSpParameterByName(SpParameter.SpType spType, String name, SpParameter newSpParameter){
        SpParameter oldSpParameter = getSpParameterByName(spType, name);
        int index=-1;

        try {
            if(spType.equals(SpParameter.SpType.FIND)){
                index = listOfFindSpParameters.indexOf(oldSpParameter);
                if(index >-1)
                    listOfFindSpParameters.set(index, newSpParameter);
            } else if (spType.equals(SpParameter.SpType.FIND2)){
                index = listOfFind2SpParameters.indexOf(oldSpParameter);
                if(index >-1)
                    listOfFind2SpParameters.set(index, newSpParameter);
            }
        }catch (NullPointerException e){
            System.out.println("오류가 발생하였습니다.");
            throw e;
        }
    }



}
