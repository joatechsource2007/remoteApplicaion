package com.remote.restservice.file.service;

import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLException;
import java.util.Map;


public interface FileService {

    Map<String, Object> init(Map<String,Object> param) throws SQLException;

    Map<String, Object> search(long AttFileNo) throws SQLException;

    Map<String, Object> insert(MultipartFile[] files, Map<String, Object> param) throws SQLException;

    Map<String, Object> save(MultipartFile[] files, Map<String, Object> param) throws SQLException;

    Map<String, Object> initDetail(Map<String,Object> param) throws SQLException;

    Map<String, Object> searchDetail(Map<String,Object> param) throws SQLException;

    Map<String, Object> insertDetail(Map<String,Object> param) throws SQLException;

    Map<String, Object> deleteDetail(long AttFileNo, long seqNo) throws SQLException;
}
