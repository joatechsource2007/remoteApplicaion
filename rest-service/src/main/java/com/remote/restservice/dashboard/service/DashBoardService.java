package com.remote.restservice.dashboard.service;

import java.sql.SQLException;
import java.util.Map;


public interface DashBoardService {

    //Map<String, Object> init() throws SQLException;

    /**
     * 331	wsp	검침	wsp_Week_Meter_V1	주간수신 검침
     */
    Map<String, Object> cardriver(Map<String,Object> param) throws SQLException;

    Map<String, Object> tanksummary(Map<String,Object> param) throws SQLException;

}
