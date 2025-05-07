package com.remote.restservice.tank.service;

import java.sql.SQLException;
import java.util.Map;


public interface TankService {

    //Map<String, Object> init() throws SQLException;

    Map<String, Object> search(Map<String,Object> param) throws SQLException;


    Map<String, Object> tcustRcvMonth(Map<String,Object> param) throws SQLException;

    Map<String, Object> tcustRcvDetail(Map<String,Object> param) throws SQLException;

    Map<String, Object> tcustRcvDetailV2(Map<String,Object> param) throws SQLException;
    Map<String, Object> cuChargeList(Map<String,Object> param) throws SQLException;

    Map<String, Object> tankinsplist(Map<String,Object> param) throws SQLException;

    Map<String, Object> tankinsphist(Map<String,Object> param) throws SQLException;

    Map<String, Object> tankinspselect(Map<String,Object> param) throws SQLException;


    //Map<String, Object> update(Map<String,Object> param) throws SQLException;

    //Map<String, Object> insert(Map<String,Object> param) throws SQLException;
}
