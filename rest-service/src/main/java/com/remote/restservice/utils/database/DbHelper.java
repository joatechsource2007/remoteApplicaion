package com.remote.restservice.utils.database;

import org.jooq.tools.json.JSONArray;
import org.jooq.tools.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

@Component
public class DbHelper {

    private static final Logger logger = LoggerFactory.getLogger(DbHelper.class);
    private final DataSource dataSource;

    public DbHelper(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * SpInfo에 프로시저명과 SpParameter 정보까지 세팅되어 있을때 사용
     * 트랜잭션 관리 포함
     * @param spInfo
     * @return
     * @throws SQLException
     */
    public Map<String, Object> execute(SpInfo spInfo) throws SQLException {
        Connection con = dataSource.getConnection();
        AutoSetAutoCommit sac = new AutoSetAutoCommit(con,false);
        AutoRollback tm = new AutoRollback(con);

        Statement stmt = con.createStatement();
        StringBuffer sb = new StringBuffer();
        int start = 0;
        for (SpParameter parameter : spInfo.getSpParameterList()){
            if( spInfo.toString().indexOf("OPT = 50") > -1 || spInfo.toString().indexOf("OPT = '50'") > -1 ){
                sb.append(String.format("%s@%s=%s ", (start > 0 ? "," : ""), parameter.getName(), convParamToSpDataSearch(parameter) ));
                start++;
            }else {
                if (parameter.getValue() != null && !parameter.getValue().equals("")) { // fix default param apppend error
                    sb.append(String.format("%s@%s=%s ", (start > 0 ? "," : ""), parameter.getName(), convParamToSpData(parameter)));
                    start++;
                }
            }
        }
        String sqlStringWithUnknownResults = String.format("EXEC %s %s",spInfo.getSpName(), sb);
        logger.info(String.format("\n Executed Statement \n%s",sqlStringWithUnknownResults));

        Map<String, Object> resultMap = new HashMap<>();
        try (con; stmt; sac; tm) {

                boolean results = stmt.execute(sqlStringWithUnknownResults);
                int count = 0;
                int resultSetCount = 0;
                do {
                    if (results) {
                        ResultSet rs = stmt.getResultSet();
                        ResultSetMetaData md = rs.getMetaData();
                        int numCols = md.getColumnCount();
                        List<String> colNames = IntStream.range(0, numCols)
                                .mapToObj(i -> {
                                    try {
                                        return md.getColumnName(i + 1).equals("")? String.valueOf(i) : md.getColumnName(i + 1);
                                    } catch (SQLException e) {
                                        logger.info("오류가 발생하였습니다.1");
                                        return "?";
                                    }
                                })
                                .toList();

                        JSONArray result = new JSONArray();
                        while (rs.next()) {
                            JSONObject row = new JSONObject();
                            colNames.forEach(cn -> {
                                try {
                                    if(isNumeric(cn) && Integer.parseInt(cn) <100 ){ //로직없음. 누더기
                                        int col = Integer.parseInt(cn);
                                        row.put("col"+cn, rs.getObject(col));
                                    }else{
                                        row.put(cn, rs.getObject(cn));
                                    }

                                } catch (SQLException e) {
                                    logger.info("오류가 발생하였습니다.2");
                                }
                            });
                            result.add(row);
                        }
                        Optional<List<String>> listTableNames = Optional.ofNullable(spInfo.getTableNames());
                        String tableName="";
                        if(listTableNames.isPresent()){
                            List<String> l = listTableNames.get();
                            tableName = l.size() > resultSetCount ? l.get(resultSetCount) : "TABLE" + String.valueOf(resultSetCount);
                        }else{
                            tableName = "TABLE" + String.valueOf(resultSetCount);
                        }
                        resultMap.put( tableName, result);
                        resultSetCount++;
                        logger.info("Result set data displayed here.");
                        System.out.println("Result set data displayed here.");
                    }
                    else {
                        count = stmt.getUpdateCount();
                        if (count >= 0) {
                            resultMap.put( "RowAffected", count);
                            System.out.println("DDL or update data displayed here.");
                            System.out.println("RowAffected:"+count);
                        }
                        else {
                            resultMap.put( "RowAffected", count);
                            System.out.println("No more results to process.");
                        }
                    }
                    results = stmt.getMoreResults();
                    logger.info("more result = {}",results);
                }
                while (results || count != -1);

                tm.commit();

        }
        // Handle any errors that may have occurred.
        catch (SQLException e) {
            logger.info("오류가 발생하였습니다.3");
            throw e;
        }

        return resultMap;
    }

    public Map<String, Object> execute2(SpInfo spInfo) throws SQLException {
        Connection con = dataSource.getConnection();
        AutoSetAutoCommit sac = new AutoSetAutoCommit(con,false);
        AutoRollback tm = new AutoRollback(con);

        Statement stmt = con.createStatement();
        StringBuffer sb = new StringBuffer();
        int start = 0;
        for (SpParameter parameter : spInfo.getSpParameterList()){
            if( spInfo.toString().indexOf("OPT = 50") > -1 || spInfo.toString().indexOf("OPT = '50'") > -1 ){
                sb.append(String.format("%s@%s=%s ", (start > 0 ? "," : ""), parameter.getName(), convParamToSpDataSearch(parameter) ));
                start++;
            }else {
                if (parameter.getValue() != null && !parameter.getValue().equals("")) { // fix default param apppend error
                    sb.append(String.format("%s@%s=%s ", (start > 0 ? "," : ""), parameter.getName(), convParamToSpData(parameter)));
                    start++;
                }
            }
        }
        String sqlStringWithUnknownResults = String.format("EXEC %s %s",spInfo.getSpName(), sb);
        logger.info(String.format("\n Executed Statement \n%s",sqlStringWithUnknownResults));

        Map<String, Object> resultMap = new HashMap<>();
        try (con; stmt; sac; tm) {
            boolean results = stmt.execute(sqlStringWithUnknownResults);
            int count = 0;
            int resultSetCount = 0;
            do {
                if (results) {
                    ResultSet rs = stmt.getResultSet();
                    ResultSetMetaData md = rs.getMetaData();
                    int numCols = md.getColumnCount();
                    List<String> colNames = IntStream.range(0, numCols)
                            .mapToObj(i -> {
                                try {
                                    return md.getColumnName(i + 1).equals("")? String.valueOf(i) : md.getColumnName(i + 1);
                                } catch (SQLException e) {
                                    logger.info("오류가 발생하였습니다.");
                                    return "?";
                                }
                            })
                            .toList();

                    JSONArray result = new JSONArray();
                    while (rs.next()) {
                        JSONObject row = new JSONObject();
                        colNames.forEach(cn -> {
                            try {
                                if(isNumeric(cn) && Integer.parseInt(cn) <100 ){ //로직없음. 누더기
                                    int col = Integer.parseInt(cn);
                                    row.put("col"+cn, rs.getObject(col));
                                }else{
                                    row.put(cn, rs.getObject(cn));
                                }

                            } catch (SQLException e) {
                                logger.info("오류가 발생하였습니다.");
                            }
                        });
                        result.add(row);
                    }
                    Optional<List<String>> listTableNames = Optional.ofNullable(spInfo.getTableNames());
                    String tableName="";
                    if(listTableNames.isPresent()){
                        List<String> l = listTableNames.get();
                        tableName = l.size() > resultSetCount ? l.get(resultSetCount) : "TABLE" + String.valueOf(resultSetCount);
                    }else{
                        tableName = "TABLE" + String.valueOf(resultSetCount);
                    }
                    resultMap.put( tableName, result);
                    resultSetCount++;
                    System.out.println("Result set data displayed here.");
                }
                else {
                    count = stmt.getUpdateCount();
                    if (count >= 0) {
                        resultMap.put( "RowAffected", count);
                        System.out.println("DDL or update data displayed here.");
                        System.out.println("RowAffected:"+count);
                    }
                    else {
                        resultMap.put( "RowAffected", count);
                        System.out.println("No more results to process.");
                    }
                }
                results = stmt.getMoreResults();
                logger.info("more result = {}",results);
            }
            while (results || count != -1);

            tm.commit();
        }
        // Handle any errors that may have occurred.
        catch (SQLException e) {
            logger.info("오류가 발생하였습니다.");
            throw e;
        }

        return resultMap;
    }
    public static int getSqlType(String type) {
        switch (type) {
            case "DATETIME":
                return Types.TIMESTAMP;
            case "NVARCHAR":
                return Types.NVARCHAR;
            case "CHAR":
                return Types.CHAR;
            case "NUMERIC":
                return Types.NUMERIC;
        }
        return 0;
    }

    public Map<String, Object> executeOutput(SpInfo spInfo) throws SQLException {
        Connection con = dataSource.getConnection();
        AutoSetAutoCommit sac = new AutoSetAutoCommit(con,false);
        AutoRollback tm = new AutoRollback(con);

        Statement stmt = con.createStatement();
        StringBuffer sb = new StringBuffer();
        int start = 0;
        for (SpParameter parameter : spInfo.getSpParameterList()){
                if (parameter.getValue() != null && !parameter.getValue().equals("")) { // fix default param apppend error
                    sb.append(String.format("%s ",  "?,"));
                        start++;
                }
        }
        String sqlStringWithUnknownResults = String.format("{call %s (%s)}",spInfo.getSpName(), sb != null ? sb.substring(0,sb.length()-2) : "");
        logger.info(String.format("\n Executed Statement \n%s",sqlStringWithUnknownResults));

        CallableStatement cs =con.prepareCall(sqlStringWithUnknownResults);
        int startObject = 0;
        for (SpParameter parameter : spInfo.getSpParameterList()){
            if (parameter.getValue() != null && !parameter.getValue().equals("")) { // fix default param apppend error
                    switch (parameter.getDirection()) {
                        case IN:
                            startObject++;
                            cs.setObject(startObject,parameter.getName());
                            break;
                    }
            }
        }
        for (SpParameter parameter : spInfo.getSpParameterList()){
            if (parameter.getValue() != null && !parameter.getValue().equals("")) { // fix default param apppend error
                switch (parameter.getDirection()) {
                        case OUT:
                            startObject++;
                            logger.info(String.format("\n Executed Statement \n[%n]%s",startObject, parameter.getJdbcType()));
                            cs.registerOutParameter(startObject,   getSqlType(parameter.getJdbcType().toString()));
                            break;
                }

            }
        }

        Map<String, Object> resultMap = new HashMap<>();
        try (con; stmt; sac; tm) {
            //boolean results = stmt.execute(sqlStringWithUnknownResults);
            boolean results = cs.execute();
            int count = cs.getUpdateCount();
            int resultSetCount = 0;
            do {
                if (results) {
                    ResultSet rs = stmt.getResultSet();
                    ResultSetMetaData md = rs.getMetaData();
                    int numCols = md.getColumnCount();
                    List<String> colNames = IntStream.range(0, numCols)
                            .mapToObj(i -> {
                                try {
                                    return md.getColumnName(i + 1).equals("")? String.valueOf(i) : md.getColumnName(i + 1);
                                } catch (SQLException e) {
                                    logger.info("오류가 발생하였습니다.");
                                    return "?";
                                }
                            })
                            .toList();

                    JSONArray result = new JSONArray();
                    while (rs.next()) {
                        JSONObject row = new JSONObject();
                        colNames.forEach(cn -> {
                            try {
                                if(isNumeric(cn)){
                                    int col = Integer.parseInt(cn);
                                    row.put("col"+cn, rs.getObject(col));
                                }else{
                                    row.put(cn, rs.getObject(cn));
                                }

                            } catch (SQLException e) {
                                logger.info("오류가 발생하였습니다.");
                            }
                        });
                        result.add(row);
                    }
                    Optional<List<String>> listTableNames = Optional.ofNullable(spInfo.getTableNames());
                    String tableName="";
                    if(listTableNames.isPresent()){
                        List<String> l = listTableNames.get();
                        tableName = l.size() > resultSetCount ? l.get(resultSetCount) : "TABLE" + String.valueOf(resultSetCount);
                    }else{
                        tableName = "TABLE" + String.valueOf(resultSetCount);
                    }
                    resultMap.put( tableName, result);
                    resultSetCount++;
                    System.out.println("Result set data displayed here.");
                }
                else {
                    count = stmt.getUpdateCount();
                    if (count >= 0) {
                        resultMap.put( "RowAffected", count);
                        System.out.println("DDL or update data displayed here.");
                        System.out.println("RowAffected:"+count);
                    }
                    else {
                        resultMap.put( "RowAffected", count);
                        System.out.println("No more results to process.");
                    }
                }
                results = stmt.getMoreResults();
                logger.info("more result = {}",results);
            }
            while (results || count != -1);

            tm.commit();
        }
        // Handle any errors that may have occurred.
        catch (SQLException e) {
            logger.info("오류가 발생하였습니다.");
            throw e;
        }

        return resultMap;
    }


    public Map<String, Object> executeCode(SpInfo spInfo) throws SQLException {
        Connection con = dataSource.getConnection();
        AutoSetAutoCommit sac = new AutoSetAutoCommit(con,false);
        AutoRollback tm = new AutoRollback(con);

        Statement stmt = con.createStatement();
        StringBuffer sb = new StringBuffer();
        int start = 0;
        for (SpParameter parameter : spInfo.getSpParameterList()){
            if(parameter.getValue() != null){
                sb.append(String.format("%s@%s=%s ",(start > 0 ?",":""), parameter.getName(), convParamToSpData(parameter)));
                start++;
            }
        }
        String sqlStringWithUnknownResults = String.format("EXEC %s %s",spInfo.getSpName(), sb);
        logger.info(String.format("\n Executed Statement \n%s",sqlStringWithUnknownResults));

        Map<String, Object> resultMap = new HashMap<>();
        try (con; stmt; sac; tm) {
            boolean results = stmt.execute(sqlStringWithUnknownResults);
            int count = 0;
            int resultSetCount = 0;
            do {
                if (results) {
                    ResultSet rs = stmt.getResultSet();
                    ResultSetMetaData md = rs.getMetaData();
                    int numCols = md.getColumnCount();
                    List<String> colNames = IntStream.range(0, numCols)
                            .mapToObj(i -> {
                                try {
                                    return md.getColumnName(i + 1).equals("")? String.valueOf(i) : md.getColumnName(i + 1);
                                } catch (SQLException e) {
                                    logger.info("md.getColumnName 오류가 발생하였습니다.");
                                    return "?";
                                }
                            })
                            .toList();
                    resultMap.put( "colName", colNames);
                    JSONArray result = new JSONArray();
                    while (rs.next()) {
                        JSONObject row = new JSONObject();
                        colNames.forEach(cn -> {
                            try {
                                if(isNumeric(cn)){
                                    int col = Integer.parseInt(cn);
                                    row.put("col"+cn, rs.getObject(col));
                                }else{
                                    row.put(cn, rs.getObject(cn));
                                }

                            } catch (SQLException e) {
                                logger.info("colNames.forEach 오류가 발생하였습니다.");
                            }
                        });
                        result.add(row);
                    }
                    Optional<List<String>> listTableNames = Optional.ofNullable(spInfo.getTableNames());
                    String tableName="";
                    if(listTableNames.isPresent()){
                        List<String> l = listTableNames.get();
                        tableName = l.size() > resultSetCount ? l.get(resultSetCount) : "TABLE" + String.valueOf(resultSetCount);
                    }else{
                        tableName = "TABLE" + String.valueOf(resultSetCount);
                    }
                    resultMap.put( tableName, result);
                    resultSetCount++;
                    System.out.println("Result set data displayed here.");
                }
                else {
                    count = stmt.getUpdateCount();
                    if (count >= 0) {
                        resultMap.put( "RowAffected", count);
                        System.out.println("DDL or update data displayed here.");
                        System.out.println("RowAffected:"+count);
                    }
                    else {
                        resultMap.put( "RowAffected", count);
                        System.out.println("No more results to process.");
                    }
                }
                results = stmt.getMoreResults();
                logger.info("more result = {}",results);
            }
            while (results || count != -1);

            tm.commit();
        }
        // Handle any errors that may have occurred.
        catch (SQLException e) {
            logger.info("오류가 발생하였습니다.");
            throw e;
        }

        return resultMap;
    }

    /**
     * SpInfo 에는 프로시져명 SpParameter 정보는 두번째 파라미터로 제공
     * 트랜잭션 관리 포함
     * @param spInfo
     * @param listOfSpParameters
     * @return
     * @throws SQLException
     */
    public Map<String, Object> execute(SpInfo spInfo, List<SpParameter> listOfSpParameters) throws SQLException {
        Connection con = dataSource.getConnection();
        AutoSetAutoCommit sac = new AutoSetAutoCommit(con,false);
        AutoRollback tm = new AutoRollback(con);

        Statement stmt = con.createStatement();
        StringBuffer sb = new StringBuffer();
        int start = 0;
        for (SpParameter parameter : listOfSpParameters){
            if(parameter.getValue() != null && !parameter.getValue().equals("")){
                sb.append(String.format("%s@%s=%s ",(start > 0 ?",":""), parameter.getName(), convParamToSpData(parameter)));
                start++;
            }
        }
        String sqlStringWithUnknownResults = String.format("EXEC %s %s",spInfo.getSpName(), sb);
        logger.info("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        logger.info(String.format("\n Executed Statement \n%s",sqlStringWithUnknownResults));
        logger.info("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

        Map<String, Object> resultMap = new HashMap<>();
        try (con; stmt; sac; tm) {
            boolean results = stmt.execute(sqlStringWithUnknownResults);
            int count = 0;
            int resultSetCount = 0;
            do {
                if (results) {
                    ResultSet rs = stmt.getResultSet();
                    ResultSetMetaData md = rs.getMetaData();
                    int numCols = md.getColumnCount();
                    List<String> colNames = IntStream.range(0, numCols)
                            .mapToObj(i -> {
                                try {
                                    return md.getColumnName(i + 1);
                                } catch (SQLException e) {
                                    logger.info("오류가 발생하였습니다.");
                                    return "?";
                                }
                            })
                            .toList();

                    JSONArray result = new JSONArray();
                    while (rs.next()) {
                        JSONObject row = new JSONObject();
                        colNames.forEach(cn -> {
                            try {
                                row.put(cn, rs.getObject(cn));
                            } catch (SQLException e) {
                                logger.info("오류가 발생하였습니다.");
                            }
                        });
                        result.add(row);
                    }
                    Optional<List<String>> listTableNames = Optional.ofNullable(spInfo.getTableNames());
                    String tableName="";
                    if(listTableNames.isPresent()){
                        List<String> l = listTableNames.get();
                        tableName = l.size() > resultSetCount ? l.get(resultSetCount) : "TABLE" + String.valueOf(resultSetCount);
                    }else{
                        tableName = "TABLE" + String.valueOf(resultSetCount);
                    }
                    resultMap.put( tableName, result);
                    resultSetCount++;
                    System.out.println("Result set data displayed here.");
                }
                else {
                    count = stmt.getUpdateCount();
                    if (count >= 0) {
                        resultMap.put( "RowAffected", count);
                        System.out.println("DDL or update data displayed here.");
                        System.out.println("RowAffected:"+count);
                    }
                    else {
                        resultMap.put( "RowAffected", count);
                        System.out.println("No more results to process.");
                    }
                }
                results = stmt.getMoreResults();
                logger.info("more result = {}",results);
            }
            while (results || count != -1);

            tm.commit();
        }
        // Handle any errors that may have occurred.
        catch (SQLException e) {
            logger.info("오류가 발생하였습니다.");
            throw e;
        }

        return resultMap;
    }

    public String selectSubFolder (long AttFileNo ) throws SQLException {
        Connection conn = dataSource.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs;
        String FolderName = "";
        rs = stmt.executeQuery(
                "SELECT CONVERT(CHAR(8), EDPDateTime, 112) AS FolderName  FROM CM_AttFiles_H  WHERE AttFileNo = " + AttFileNo);
        while ( rs.next() ) {
            FolderName = rs.getString("FolderName");
            System.out.println(FolderName);
        }
        conn.close();
        return FolderName;
    }



    /**
     * SpInfo 에는 프로시져명 N개의 SpParameter 정보 제공시 사용
     * 트랜잭션 관리 포함
     * @param spInfo
     * @param spParameters
     * @return
     * @throws SQLException
     */
    public Map<String, Object> execute(SpInfo spInfo, SpParameter...spParameters) throws SQLException {
        Connection con = dataSource.getConnection();
        AutoSetAutoCommit sac = new AutoSetAutoCommit(con,false);
        AutoRollback tm = new AutoRollback(con);

        Statement stmt = con.createStatement();
        StringBuffer sb = new StringBuffer();
        int start = 0;
        for (SpParameter parameter : spParameters){
            if(parameter.getValue() != null && !parameter.getValue().equals("")){
                sb.append(String.format("%s@%s=%s ",(start > 0 ?",":""), parameter.getName(), convParamToSpData(parameter)));
                start++;
            }
        }
        String sqlStringWithUnknownResults = String.format("EXEC %s %s",spInfo.getSpName(), sb);
        logger.info(String.format("\n Executed Statement \n%s",sqlStringWithUnknownResults));

        Map<String, Object> resultMap = new HashMap<>();
        try (con; stmt; sac; tm) {
            boolean results = stmt.execute(sqlStringWithUnknownResults);
            int count = 0;
            int resultSetCount = 0;
            do {
                if (results) {
                    ResultSet rs = stmt.getResultSet();
                    ResultSetMetaData md = rs.getMetaData();
                    int numCols = md.getColumnCount();
                    List<String> colNames = IntStream.range(0, numCols)
                            .mapToObj(i -> {
                                try {
                                    return md.getColumnName(i + 1);
                                } catch (SQLException e) {
                                    logger.info("오류가 발생하였습니다.");
                                    return "?";
                                }
                            })
                            .toList();

                    JSONArray result = new JSONArray();
                    while (rs.next()) {
                        JSONObject row = new JSONObject();
                        colNames.forEach(cn -> {
                            try {
                                row.put(cn, rs.getObject(cn));
                            } catch (SQLException e) {
                                logger.info("오류가 발생하였습니다.");
                            }
                        });
                        result.add(row);
                    }
                    Optional<List<String>> listTableNames = Optional.ofNullable(spInfo.getTableNames());
                    String tableName="";
                    if(listTableNames.isPresent()){
                        List<String> l = listTableNames.get();
                        tableName = l.size() > resultSetCount ? l.get(resultSetCount) : "TABLE" + String.valueOf(resultSetCount);
                    }else{
                        tableName = "TABLE" + String.valueOf(resultSetCount);
                    }
                    resultMap.put( tableName, result);
                    resultSetCount++;
                    System.out.println("Result set data displayed here.");
                }
                else {
                    count = stmt.getUpdateCount();
                    if (count >= 0) {
                        resultMap.put( "RowAffected", count);
                        System.out.println("DDL or update data displayed here.");
                        System.out.println("RowAffected:"+count);
                    }
                    else {
                        resultMap.put( "RowAffected", count);
                        System.out.println("No more results to process.");
                    }
                }
                results = stmt.getMoreResults();
                logger.info("more result = {}",results);
            }
            while (results || count != -1);

            tm.commit();
        }
        // Handle any errors that may have occurred.
        catch (SQLException e) {
            logger.info("오류가 발생하였습니다.");
            throw e;
        }

        return resultMap;
    }


    /**
     * NonQuery sp 실행하고 OUT변수 리턴받을때 사용
     * @param spInfo
     * @param listOfSpParameters
     * @return
     * @throws SQLException
     */
    public Map<String,Object>  executeNonQuery(SpInfo spInfo, List<SpParameter> listOfSpParameters) throws SQLException{
        Connection con = dataSource.getConnection();
        AutoSetAutoCommit sac = new AutoSetAutoCommit(con,false);
        AutoRollback tm = new AutoRollback(con);

        int paramCount = 1;

        StringBuffer sqlCall = new StringBuffer("{call " + spInfo.getSpName());
        for (SpParameter parameter : listOfSpParameters){
            sqlCall.append((paramCount++ > 1 ? ", " : "(") + "?");
        }
        sqlCall.append(")}");

        String sql = sqlCall.toString();
        logger.info("EXECUTED SQL : {}", sql);
        CallableStatement cs =con.prepareCall(sql);
        paramCount = 1;
        for (SpParameter parameter : listOfSpParameters){
            logger.info("PARAMETER {} : @{} = {}", paramCount, parameter.getName(), parameter.getValue());
            if(parameter.getDirection().equals(SpParameter.Direction.OUT) || parameter.getDirection().equals(SpParameter.Direction.INOUT)){
                cs.registerOutParameter(paramCount++, parameter.getJdbcType());
            }else{
                cs.setObject(paramCount++, parameter.getValue());
            }
        }
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Object> tables = new HashMap<>();
        try (con; cs; sac; tm) {
            boolean results = cs.execute();
            int count = 0;
            int resultSetCount = 0;
            do {
                if (results) {
                    ResultSet rs = cs.getResultSet();
                    ResultSetMetaData md = rs.getMetaData();
                    int numCols = md.getColumnCount();
                    List<String> colNames = IntStream.range(0, numCols)
                            .mapToObj(i -> {
                                try {
                                    return md.getColumnName(i + 1);
                                } catch (SQLException e) {
                                    logger.info("오류가 발생하였습니다.");
                                    return "?";
                                }
                            })
                            .toList();

                    JSONArray result = new JSONArray();
                    while (rs.next()) {
                        JSONObject row = new JSONObject();
                        colNames.forEach(cn -> {
                            try {
                                row.put(cn, rs.getObject(cn));
                            } catch (SQLException e) {
                                logger.info("오류가 발생하였습니다.");
                            }
                        });
                        result.add(row);
                    }
                    Optional<List<String>> listTableNames = Optional.ofNullable(spInfo.getTableNames());
                    String tableName="";
                    if(listTableNames.isPresent()){
                        List<String> l = listTableNames.get();
                        tableName = l.size() > resultSetCount ? l.get(resultSetCount) : "TABLE" + String.valueOf(resultSetCount);
                    }else{
                        tableName = "TABLE" + String.valueOf(resultSetCount);
                    }
                    tables.put( tableName, result);
                    resultSetCount++;

                    logger.info("**************** Execute Result ****************");
                    logger.info(tableName+"\n"+result);
                    logger.info("**********************************************");
                } else {
                    count = cs.getUpdateCount();
                    if (count >= 0) {
                        System.out.println("DDL or update data displayed here.");
                        paramCount = 1;
                        for (SpParameter parameter : listOfSpParameters){
                            if(parameter.getDirection().equals(SpParameter.Direction.OUT) || parameter.getDirection().equals(SpParameter.Direction.INOUT)){
                                String name = parameter.getName();
                                Object value = cs.getObject(paramCount++);
                                resultMap.put(name, value);
                                logger.info("OUT PARAMETER {} :  {}", name, value);
                            }else{
                                paramCount++;
                            }
                        }
                    } else {
                        System.out.println("No more results to process.");
                    }
                }
                results = cs.getMoreResults();
                logger.info("more result = {}", results);
            }
            while (results || count != -1);

            tm.commit();
        }
        // Handle any errors that may have occurred.
        catch (SQLException e) {
            logger.info("오류가 발생하였습니다.");
            throw e;
        }
        resultMap.put("tables", tables);
        return resultMap;
    }

    /**
     * 프로시져가 여러개의 OUT변수를 리턴하고 추가로 1개의 ResultSet를 리턴할때 사용
     * @param spInfo
     * @param spParameters
     * @return
     * @throws SQLException
     */
    public Map<String, Object> executeInAndOutParamsWithResultSet(SpInfo spInfo, SpParameter...spParameters) throws SQLException {

        Connection con = dataSource.getConnection();
        int paramCount = 1;

        StringBuffer sqlCall = new StringBuffer("{call " + spInfo.getSpName());
        for (SpParameter parameter : spInfo.getSpParameterList()){
            sqlCall.append((paramCount++ > 1 ? ", " : "(") + "?");
        }
        sqlCall.append(")}");

        String sql = sqlCall.toString();
        logger.info("EXECUTED SQL : {}", sql);
        // e.g., {call BreakfastSP(?,?,?)}
        CallableStatement cs =con.prepareCall(sql);
        paramCount = 1;
        for (SpParameter parameter : spInfo.getSpParameterList()){
            logger.info("PARAMETER {} : @{} = {}", paramCount, parameter.getName(), parameter.getValue());
            if(parameter.getDirection() != null && parameter.getDirection().equals(SpParameter.Direction.OUT)){
                cs.registerOutParameter(paramCount++, parameter.getJdbcType());
            }else{
                cs.setObject(paramCount++, parameter.getValue());
            }

        }

        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Object> tables = new HashMap<>();
        try (con; cs;) {
            boolean results = cs.execute();
            int count = 0;
            int resultSetCount = 0;
            do {
                if (results) {
                    ResultSet rs = cs.getResultSet();
                    ResultSetMetaData md = rs.getMetaData();
                    int numCols = md.getColumnCount();
                    List<String> colNames = IntStream.range(0, numCols)
                            .mapToObj(i -> {
                                try {
                                    return md.getColumnName(i + 1);
                                } catch (SQLException e) {
                                    logger.info("오류가 발생하였습니다.");
                                    return "?";
                                }
                            })
                            .toList();

                    JSONArray result = new JSONArray();
                    while (rs.next()) {
                        JSONObject row = new JSONObject();
                        colNames.forEach(cn -> {
                            try {
                                row.put(cn, rs.getObject(cn));
                            } catch (SQLException e) {
                                logger.info("오류가 발생하였습니다.");
                            }
                        });
                        result.add(row);
                    }
                    Optional<List<String>> listTableNames = Optional.ofNullable(spInfo.getTableNames());
                    String tableName="";
                    if(listTableNames.isPresent()){
                        List<String> l = listTableNames.get();
                        tableName = l.size() > resultSetCount ? l.get(resultSetCount) : "TABLE" + String.valueOf(resultSetCount);
                    }else{
                        tableName = "TABLE" + String.valueOf(resultSetCount);
                    }
                    tables.put( tableName, result);
                    resultSetCount++;

                    logger.info("**************** Execute Result ****************");
                    logger.info(tableName+"\n"+result);

                    logger.info("**********************************************");
                } else {
                    count = cs.getUpdateCount();
                    if (count >= 0) {
                        System.out.println("DDL or update data displayed here.");
                        paramCount = 1;
                        for (SpParameter parameter : spInfo.getSpParameterList()){
                            if(parameter.getDirection() != null && parameter.getDirection().equals(SpParameter.Direction.OUT)){
                                String name = parameter.getName();
                                Object value = cs.getObject(paramCount++);
                                resultMap.put(name, value);
                                logger.info("OUT PARAMETER {} :  {}", name, value);
                            }else{
                                paramCount++;
                            }
                        }
                    } else {
                        System.out.println("No more results to process.");
                    }
                }
                results = cs.getMoreResults();
                logger.info("more result = {}", results);
            }
            while (results || count != -1);

            //추가 ResultSet(단일)를 가져온다.
            JSONArray result = getJsonArrayFromCallableStatement(cs);

            Optional<List<String>> listTableNames = Optional.ofNullable(spInfo.getTableNames());
            String tableName="";
            if(listTableNames.isPresent()){
                List<String> l = listTableNames.get();
                tableName = l.size() > resultSetCount ? l.get(resultSetCount) : "TABLE" + String.valueOf(resultSetCount);
            }else{
                tableName = "TABLE" + String.valueOf(resultSetCount);
            }
            tables.put( tableName, result);

            logger.info("**************** Execute Result ****************");
            logger.info(tableName+"\n"+result);

            logger.info("**********************************************");
        }
        // Handle any errors that may have occurred.
        catch (SQLException e) {
            logger.info("오류가 발생하였습니다.");
            throw e;
        }
        resultMap.put("tables", tables);
        return resultMap;
    }

    public Map<String, Object> executeInAndOutParams(SpInfo spInfo) throws SQLException {

        Connection con = dataSource.getConnection();
        AutoSetAutoCommit sac = new AutoSetAutoCommit(con,false);
        AutoRollback tm = new AutoRollback(con);
        int paramCount = 1;

        StringBuffer sqlCall = new StringBuffer("{call " + spInfo.getSpName());
        for (SpParameter parameter : spInfo.getSpParameterList()){
            sqlCall.append((paramCount++ > 1 ? ", " : "(") + "?");
        }
        sqlCall.append(")}");

        String sql = sqlCall.toString();
        logger.info("EXECUTED SQL : {}", sql);
        // e.g., {call BreakfastSP(?,?,?)}
        CallableStatement cs =con.prepareCall(sql);
        paramCount = 1;
        for (SpParameter parameter : spInfo.getSpParameterList()){
            logger.info("PARAMETER {} : @{} = {}", paramCount, parameter.getName(), parameter.getValue());
            if(parameter.getDirection() != null && (parameter.getDirection().equals(SpParameter.Direction.OUT) || parameter.getDirection().equals(SpParameter.Direction.INOUT))){
                cs.registerOutParameter(paramCount, parameter.getJdbcType());
                cs.setObject(paramCount, parameter.getValue());
                paramCount++;
            }else{
                cs.setObject(paramCount++, parameter.getValue());
            }

        }

        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Object> tables = new HashMap<>();
        try (con; cs; sac; tm) {
            boolean results = cs.execute();
            int count = 0;
            int resultSetCount = 0;
            do {
                if (results) {
                    ResultSet rs = cs.getResultSet();
                    ResultSetMetaData md = rs.getMetaData();
                    int numCols = md.getColumnCount();
                    List<String> colNames = IntStream.range(0, numCols)
                            .mapToObj(i -> {
                                try {
                                    return md.getColumnName(i + 1);
                                } catch (SQLException e) {
                                    logger.info("오류가 발생하였습니다.");
                                    return "?";
                                }
                            })
                            .toList();

                    JSONArray result = new JSONArray();
                    while (rs.next()) {
                        JSONObject row = new JSONObject();
                        colNames.forEach(cn -> {
                            try {
                                row.put(cn, rs.getObject(cn));
                            } catch (SQLException e) {
                                logger.info("오류가 발생하였습니다.");
                            }
                        });
                        result.add(row);
                    }
                    Optional<List<String>> listTableNames = Optional.ofNullable(spInfo.getTableNames());
                    String tableName="";
                    if(listTableNames.isPresent()){
                        List<String> l = listTableNames.get();
                        tableName = l.size() > resultSetCount ? l.get(resultSetCount) : "TABLE" + String.valueOf(resultSetCount);
                    }else{
                        tableName = "TABLE" + String.valueOf(resultSetCount);
                    }
                    tables.put( tableName, result);
                    resultSetCount++;

                    logger.info("**************** Execute Result ****************");
                    logger.info(tableName+"\n"+result);

                    logger.info("**********************************************");
                } else {
                    count = cs.getUpdateCount();
                    if (count >= 0) {
                        System.out.println("DDL or update data displayed here.");
                        paramCount = 1;
                        for (SpParameter parameter : spInfo.getSpParameterList()){
                            if(parameter.getDirection() != null && (parameter.getDirection().equals(SpParameter.Direction.OUT) || parameter.getDirection().equals(SpParameter.Direction.INOUT))){
                                String name = parameter.getName();
                                Object value = cs.getObject(paramCount++);
                                resultMap.put(name, value);
                                logger.info("OUT PARAMETER {} :  {}", name, value);
                            }else{
                                paramCount++;
                            }
                        }
                    } else {
                        logger.info("UPDATE COUNT {}", count);
                        paramCount = 1;
                        for (SpParameter parameter : spInfo.getSpParameterList()){
                            if(parameter.getDirection() != null && (parameter.getDirection().equals(SpParameter.Direction.OUT) || parameter.getDirection().equals(SpParameter.Direction.INOUT))){
                                String name = parameter.getName();
                                Object value = cs.getObject(paramCount++);
                                resultMap.put(name, value);
                                logger.info("OUT PARAMETER {} :  {}", name, value);
                            }else{
                                paramCount++;
                            }
                        }
                        logger.info("================= NO MORE OUT PARAMETER ================");
                    }
                }
                results = cs.getMoreResults();
                logger.info("more result = {}", results);
            }
            while (results || count != -1);


            tm.commit();
        }
        // Handle any errors that may have occurred.
        catch (SQLException e) {
            logger.info("오류가 발생하였습니다.--1");
            throw e;
        }
        resultMap.put("tables", tables);
        return resultMap;
    }

    public Map<String, Object> executeSpWithInParamsAndReturnResultSet(SpInfo spInfo, SpParameter...spParameters) throws SQLException {

        Connection con = dataSource.getConnection();
        int paramCount = 1;

        StringBuffer sqlCall = new StringBuffer("{call " + spInfo.getSpName());
        for (SpParameter parameter : spParameters){
            sqlCall.append((paramCount++ > 1 ? ", " : "(") + "?");
        }
        sqlCall.append(")}");

        String sql = sqlCall.toString();
        logger.info("EXECUTED SQL : {}", sql);
        // e.g., {call BreakfastSP(?,?,?)}
        CallableStatement cs =con.prepareCall(sql);
        paramCount = 1;
        for (SpParameter parameter : spParameters){
            logger.info("PARAMETER {} : @{} = {}", paramCount, parameter.getName(), parameter.getValue());
            if(parameter.getDirection() != null && parameter.getDirection().equals(SpParameter.Direction.OUT)){
                cs.registerOutParameter(paramCount++, parameter.getJdbcType());
            }else{
                cs.setObject(paramCount++, parameter.getValue());
            }

        }

        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Object> tables = new HashMap<>();
        try (con; cs;) {
            boolean results = cs.execute();
            int count = 0;
            int resultSetCount = 0;
            do {
                if (results) {
                    ResultSet rs = cs.getResultSet();
                    ResultSetMetaData md = rs.getMetaData();
                    int numCols = md.getColumnCount();
                    List<String> colNames = IntStream.range(0, numCols)
                            .mapToObj(i -> {
                                try {
                                    return md.getColumnName(i + 1);
                                } catch (SQLException e) {
                                    logger.info("오류가 발생하였습니다.");
                                    return "?";
                                }
                            })
                            .toList();

                    JSONArray result = new JSONArray();
                    while (rs.next()) {
                        JSONObject row = new JSONObject();
                        colNames.forEach(cn -> {
                            try {
                                row.put(cn, rs.getObject(cn));
                            } catch (SQLException e) {
                                logger.info("오류가 발생하였습니다.");
                            }
                        });
                        result.add(row);
                    }
                    Optional<List<String>> listTableNames = Optional.ofNullable(spInfo.getTableNames());
                    String tableName="";
                    if(listTableNames.isPresent()){
                        List<String> l = listTableNames.get();
                        tableName = l.size() > resultSetCount ? l.get(resultSetCount) : "TABLE" + String.valueOf(resultSetCount);
                    }else{
                        tableName = "TABLE" + String.valueOf(resultSetCount);
                    }
                    tables.put( tableName, result);
                    resultSetCount++;

                    logger.info("**************** Execute Result ****************");
                    logger.info(tableName+"\n"+result);

                    logger.info("**********************************************");
                } else {
                    count = cs.getUpdateCount();
                    if (count >= 0) {
                        System.out.println("DDL or update data displayed here.");
                        paramCount = 1;
                        for (SpParameter parameter : spParameters){
                            if(parameter.getDirection() != null && parameter.getDirection().equals(SpParameter.Direction.OUT)){
                                String name = parameter.getName();
                                Object value = cs.getObject(paramCount++);
                                resultMap.put(name, value);
                                logger.info("OUT PARAMETER {} :  {}", name, value);
                            }else{
                                paramCount++;
                            }
                        }
                    } else {
                        System.out.println("No more results to process.");
                    }
                }
                results = cs.getMoreResults();
                logger.info("more result = {}", results);
            }
            while (results || count != -1);

        }
        // Handle any errors that may have occurred.
        catch (SQLException e) {
            throw e;
        }
        resultMap.put("tables", tables);
        return resultMap;
    }

    /**
     * CallableStatement 로 단일 ResultSet ->JsonArray 얻음.
     * @param cs
     * @return
     * @throws SQLException
     */
    private static JSONArray getJsonArrayFromCallableStatement(CallableStatement cs) throws SQLException {
        ResultSet rs = cs.executeQuery();
        ResultSetMetaData md = rs.getMetaData();
        int numCols = md.getColumnCount();
        List<String> colNames = IntStream.range(0, numCols)
                .mapToObj(i -> {
                    try {
                        return md.getColumnName(i + 1);
                    } catch (SQLException e) {
                        logger.info("오류가 발생하였습니다.");
                        return "?";
                    }
                })
                .toList();

        JSONArray result = new JSONArray();
        while (rs.next()) {
            JSONObject row = new JSONObject();
            colNames.forEach(cn -> {
                try {
                    row.put(cn, rs.getObject(cn));
                } catch (SQLException e) {
                    logger.info("오류가 발생하였습니다.");
                }
            });
            result.add(row);
        }
        return result;
    }

    private Object convParamToSpData(SpParameter spParameter){

        return switch (spParameter.getJdbcType()) {
            case TIMESTAMP, DATE -> {
                yield "'"+spParameter.getValue().toString().replaceAll("[-/]","")+"'";
            }
            case VARCHAR, NVARCHAR, CHAR, NCHAR-> {
                yield "'"+spParameter.getValue()+"'";
            }
            case INTEGER , DECIMAL, TINYINT, NUMERIC -> {
                yield spParameter.getValue();
            }
            default->{
                yield spParameter.getValue();
            }
        };
    }

    private Object convParamToSpDataSearch(SpParameter spParameter){

        return switch (spParameter.getJdbcType()) {
            case TIMESTAMP, DATE -> {
                yield spParameter.getValue()==null || spParameter.getValue().equals("") ? "''" : "'"+spParameter.getValue().toString().replaceAll("[-/]","")+"'";
            }
            case VARCHAR, NVARCHAR, CHAR, NCHAR-> {
                yield spParameter.getValue()==null || spParameter.getValue().equals("") ? "''" : "'"+spParameter.getValue()+"'";
            }
            case INTEGER , DECIMAL, TINYINT, NUMERIC -> {
                yield spParameter.getValue()==null || spParameter.getValue().equals("")  ? 0 :  spParameter.getValue();
            }
            default->{
                yield spParameter.getValue()==null || spParameter.getValue().equals("")  ? 0: spParameter.getValue();
            }
        };
    }


    private Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");

    public boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        return pattern.matcher(strNum).matches();
    }
}
