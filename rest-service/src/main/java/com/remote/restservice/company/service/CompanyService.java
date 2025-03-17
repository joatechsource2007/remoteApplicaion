package com.remote.restservice.company.service;

import java.sql.SQLException;
import java.util.Map;


public interface CompanyService {

    //Map<String, Object> init() throws SQLException;

    Map<String, Object> find(Map<String,Object> param) throws SQLException;

    //Map<String, Object> update(Map<String,Object> param) throws SQLException;

    //Map<String, Object> insert(Map<String,Object> param) throws SQLException;
}
