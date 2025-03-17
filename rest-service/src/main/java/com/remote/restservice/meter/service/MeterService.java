package com.remote.restservice.meter.service;

import java.sql.SQLException;
import java.util.Map;


public interface MeterService {

    //Map<String, Object> init() throws SQLException;

    Map<String, Object> search(Map<String,Object> param) throws SQLException;

    Map<String, Object> getMeterDetail(Map<String,Object> param) throws SQLException;

    Map<String, Object> getMeterGroup(Map<String,Object> param) throws SQLException;

}
