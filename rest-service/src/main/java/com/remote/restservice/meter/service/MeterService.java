package com.remote.restservice.meter.service;

import java.sql.SQLException;
import java.util.Map;


public interface MeterService {

    //Map<String, Object> init() throws SQLException;

    /**
     * 331	wsp	검침	wsp_Week_Meter_V1	주간수신 검침
     */
    Map<String, Object> search(Map<String,Object> param) throws SQLException;

    Map<String, Object> getMeterDetail(Map<String,Object> param) throws SQLException;

    Map<String, Object> getMeterGroup(Map<String,Object> param) throws SQLException;
    Map<String, Object> getMeterGroupApp(Map<String,Object> param) throws SQLException;
    Map<String, Object> mcustRcvAvg(Map<String,Object> param) throws SQLException;

}
