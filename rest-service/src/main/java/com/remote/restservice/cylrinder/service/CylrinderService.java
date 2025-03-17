package com.remote.restservice.cylrinder.service;

import java.sql.SQLException;
import java.util.Map;


public interface CylrinderService {

    //Map<String, Object> init() throws SQLException;

    Map<String, Object> search(Map<String,Object> param) throws SQLException;
    Map<String, Object> searchV2(Map<String,Object> param) throws SQLException;

    Map<String, Object> jcustRcvDetail(Map<String,Object> param) throws SQLException;

    //Map<String, Object> update(Map<String,Object> param) throws SQLException;

    //Map<String, Object> insert(Map<String,Object> param) throws SQLException;
}
