package com.remote.restservice.customer.service;

import java.sql.SQLException;
import java.util.Map;


public interface CustomerService {

    //Map<String, Object> init() throws SQLException;

    /**
     * 351 : wsp_Get_Customer_FIND : 전체거래처 리스트 조건초회 (A전체, T.탱크, C.용기, M 검침,   TC,TM,CM, TCM)
     */
    Map<String, Object> find(Map<String,Object> param) throws SQLException;

    /**
     *301	wsp	전체	wsp_CUST_INFO	거래처정보  (T,C,M) 탱크,용기,검침고객정보, 탱크리스트,검침리스트, 이미지 LIST
     */
    Map<String, Object> custinfo(Map<String,Object> param) throws SQLException;

    /**
     *401	wsp	전체		wsp_Select_Data	데이터 조회 (Select)
     */
    Map<String, Object> selectdata(Map<String,Object> param) throws SQLException;

    /**
     *401	wsp	전체		wsp_Modify_Data	데이터 등록,수정,삭제( INSERT', 'UPDATE', 'DELETE')
     */
    Map<String, Object> modifydata(Map<String,Object> param) throws SQLException;

    /**
     *402	wsp	전체		wsp_Generate_PK	Insert PK 항번생성
     */
    Map<String, Object> generatepk(Map<String,Object> param) throws SQLException;

    /**
     * 300	wsp_Get_Base_Code	기초코드
     */
    Map<String, Object> getbasecode(Map<String,Object> param) throws SQLException;

}
