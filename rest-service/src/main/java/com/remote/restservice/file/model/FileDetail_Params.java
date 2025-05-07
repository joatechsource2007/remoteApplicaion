package com.remote.restservice.file.model;

import com.remote.restservice.utils.database.SpParameter;
import lombok.Data;

import java.sql.JDBCType;
import java.util.ArrayList;
import java.util.List;

@Data
public class FileDetail_Params {

    private List<SpParameter> listOfAllSpParameters = new ArrayList<SpParameter>();
    private List<SpParameter> listOfQuerySpParameters = new ArrayList<SpParameter>();
    private List<SpParameter> listOfInitSpParameters = new ArrayList<SpParameter>();


    //생성자
    public FileDetail_Params(){
        //전부
        {
            listOfAllSpParameters.add(SpParameter.builder().name("OPT").value(11).direction(SpParameter.Direction.IN).jdbcType(JDBCType.INTEGER).build());
            listOfAllSpParameters.add(SpParameter.builder().name("SEQ").value(0).direction(SpParameter.Direction.INOUT).jdbcType(JDBCType.INTEGER).build());
            listOfAllSpParameters.add(SpParameter.builder().name("AttFileNo").value(0).direction(SpParameter.Direction.IN).jdbcType(JDBCType.NUMERIC).build());
            listOfAllSpParameters.add(SpParameter.builder().name("FileName").value("").direction(SpParameter.Direction.IN).jdbcType(JDBCType.VARCHAR).build());
            listOfAllSpParameters.add(SpParameter.builder().name("FileExtention").value("").direction(SpParameter.Direction.IN).jdbcType(JDBCType.VARCHAR).build());
            listOfAllSpParameters.add(SpParameter.builder().name("FileSize").value(0).direction(SpParameter.Direction.IN).jdbcType(JDBCType.DECIMAL).build());
        }
    }

    public SpParameter getSpParameterByName(SpParameter.SpType spType, String name) {
        if(spType.equals(SpParameter.SpType.ALL)){
            return listOfAllSpParameters.stream().filter(spParameter -> name.equals(spParameter.getName())).findFirst().orElse(null);
        }else if(spType.equals(SpParameter.SpType.INIT)){
            return listOfInitSpParameters.stream().filter(spParameter -> name.equals(spParameter.getName())).findFirst().orElse(null);
        }else if(spType.equals(SpParameter.SpType.QUERY)){
            return listOfQuerySpParameters.stream().filter(spParameter -> name.equals(spParameter.getName())).findFirst().orElse(null);
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
            }
        }catch (NullPointerException e){
            System.out.println("오류가 발생하였습니다.");
            throw e;
        }
    }

}
