package com.remote.restservice.utils.database;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SpInfo {

    private String spName;

    private List<SpParameter> spParameterList;

    private List<String> tableNames;

    public SpInfo(){}

    public SpInfo(String spName){
        this.spName = spName;
    }
    public SpInfo(String spName, List<SpParameter> spParameterList){
        this(spName);
        this.spParameterList = spParameterList;
    }
    public SpInfo(String spName, List<SpParameter> spParameterList, List<String> tableNames){
        this(spName,spParameterList);
        this.tableNames = tableNames;
    }

    @Override
    public String toString(){
        StringBuffer sb = new StringBuffer();
        sb.append("\n=========================================\n");
        sb.append(String.format("EXEC %s\n",this.spName));
        boolean first=true;
        for (SpParameter parameter : this.getSpParameterList()) {
            if(!first){
                sb.append(",");
            }else{
                sb.append(" ");
            }
            sb.append("@"+ parameter.getName()+" = " + convParamToSpData(parameter)+"\n");
            first = false;
        }
        sb.append("===========긁어서 실행하시오==============\n");
        return sb.toString();
    }

    private Object convParamToSpData(SpParameter spParameter){

        return switch (spParameter.getJdbcType()) {
            case TIMESTAMP, DATE -> {
                yield spParameter.getValue()==null ? "''" : "'"+spParameter.getValue().toString().replaceAll("[-/]","")+"'";
            }
            case VARCHAR, NVARCHAR, CHAR, NCHAR-> {
                yield spParameter.getValue()==null ? "''" : "'"+spParameter.getValue()+"'";
            }
            case INTEGER , DECIMAL, TINYINT, NUMERIC -> {
                yield spParameter.getValue()==null ? 0 : spParameter.getValue();
            }
            default->{
                yield spParameter.getValue()==null? "null" : spParameter.getValue();
            }
        };
    }
}
