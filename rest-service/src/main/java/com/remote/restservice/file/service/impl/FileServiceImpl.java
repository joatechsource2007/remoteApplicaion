package com.remote.restservice.file.service.impl;

import com.remote.restservice.file.model.FileDetail_Params;
import com.remote.restservice.file.model.File_Params;
import com.remote.restservice.file.service.FileService;
import com.remote.restservice.utils.database.DbHelper;
import com.remote.restservice.utils.database.SpInfo;
import com.remote.restservice.utils.database.SpParameter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.JDBCType;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("FileService")
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private static final Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    @Value("${spring.servlet.multipart.location}")
    private String uploadPath;

    private DbHelper dbHelper;

    @Autowired
    public FileServiceImpl(DbHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    /**
     * 화면초기화 메서드 OPT=50
     * @return
     * @throws SQLException
     */
    @Override
    public Map<String, Object> init(Map<String,Object> params) throws SQLException {
        List<String> listOfTableNames = List.of();
        File_Params pParams = new File_Params();
        for (String parameter : params.keySet()) {
            SpParameter oldSpParameter =  pParams.getSpParameterByName(SpParameter.SpType.ALL, parameter);
            if(oldSpParameter==null) continue;
            oldSpParameter.setValue(params.get(parameter));
            pParams.replaceSpParameterByName(SpParameter.SpType.ALL,parameter,oldSpParameter);
        }
        SpInfo spInfo = SpInfo.builder()
                .spName("usp_COMMON_AttachedFile")
                .spParameterList(pParams.getListOfAllSpParameters())
                .tableNames(listOfTableNames)
                .build();
        logger.info(spInfo.toString());
        return dbHelper.execute(spInfo);
    }

    /**
     * 조회 - 첨부파일 목록조회
     * @param AttFileNo
     * @return
     * @throws SQLException
     */
    @Override
    public Map<String, Object> search(long AttFileNo) throws SQLException {
        List<String> listOfTableNames = List.of();
        List<SpParameter> listOfAllSpParameters = new ArrayList<SpParameter>();
        listOfAllSpParameters.add(SpParameter.builder().name("AttFileNo").direction(SpParameter.Direction.IN).value(AttFileNo)  .jdbcType(JDBCType.INTEGER).build());
        listOfAllSpParameters.add(SpParameter.builder().name("OPT").direction(SpParameter.Direction.IN).value(50)  .jdbcType(JDBCType.CHAR).build());
        SpInfo spInfo = SpInfo.builder()
                .spName("usp_COMMON_AttachedFile")
                .spParameterList(listOfAllSpParameters)
                .tableNames(listOfTableNames)
                .build();
        logger.info(spInfo.toString());
        return dbHelper.execute(spInfo);
    }

    /**
     * 등록 동일  OPT=110,111
     * @param params
     * @return
     * @throws SQLException
     */
    @Override
    public Map<String, Object> insert(MultipartFile[] files, Map<String, Object> params) throws SQLException {
        //파일마스터 첨부파일 아이디 생성
        List<String> listOfTableNames = List.of();
        File_Params pParams = new File_Params();
        for (String parameter : params.keySet()) {
            SpParameter oldSpParameter =  pParams.getSpParameterByName(SpParameter.SpType.ALL, parameter);
            if(oldSpParameter==null) continue;
            oldSpParameter.setValue(params.get(parameter));
            pParams.replaceSpParameterByName(SpParameter.SpType.ALL,parameter,oldSpParameter);
        }
        SpInfo spInfo = SpInfo.builder()
                .spName("usp_COMMON_AttachedFile")
                .spParameterList(pParams.getListOfAllSpParameters())
                .tableNames(listOfTableNames)
                .build();
        logger.info(spInfo.toString());
        Map<String, Object> master = dbHelper.executeInAndOutParams(spInfo);
        List<Map<String, Object>> details = new ArrayList<>();
        long AttFileNo = 0L;
        if(master.get("AttFileNo") instanceof BigDecimal){
            AttFileNo = ((BigDecimal) master.get("AttFileNo")).longValue();
        }else{
            AttFileNo = Long.parseLong((String)master.get("AttFileNo"));
        }
        for (int i = 0; i < files.length; i++) {

            details.add(store(files[i], AttFileNo));
        }
        Map<String, Object> result = new HashMap<>();
        result.put("master",master);
        result.put("details", details);

        return result;
    }
    /**
     * 등록 동일  OPT=110,111
     * @param params
     * @return
     * @throws SQLException
     */
    @Override
    public Map<String, Object> save(MultipartFile[] files, Map<String, Object> params) throws SQLException {
        //파일마스터 첨부파일 아이디 생성
        List<String> listOfTableNames = List.of();
        File_Params pParams = new File_Params();
        for (String parameter : params.keySet()) {
            SpParameter oldSpParameter =  pParams.getSpParameterByName(SpParameter.SpType.ALL, parameter);
            if(oldSpParameter==null) continue;
            oldSpParameter.setValue(params.get(parameter));
            pParams.replaceSpParameterByName(SpParameter.SpType.ALL,parameter,oldSpParameter);
        }
        SpInfo spInfo = SpInfo.builder()
                .spName("usp_COMMON_AttachedFile")
                .spParameterList(pParams.getListOfAllSpParameters())
                .tableNames(listOfTableNames)
                .build();
        logger.info(spInfo.toString());
        Map<String, Object> master = dbHelper.executeInAndOutParams(spInfo);
        List<Map<String, Object>> details = new ArrayList<>();

        long AttFileNo = 0L;
        if( params.get("AttFileNo") != null  ) {
            AttFileNo = Long.parseLong(String.valueOf(params.get("AttFileNo")));
            for (int i = 0; i < files.length; i++) {
                details.add(storeSave(files[i], AttFileNo));
            }
        }else{
            if(master.get("AttFileNo") instanceof BigDecimal){
                AttFileNo = ((BigDecimal) master.get("AttFileNo")).longValue();
            }else{
                AttFileNo = Long.parseLong((String)master.get("AttFileNo"));
            }
            for (int i = 0; i < files.length; i++) {
                details.add(store(files[i], AttFileNo));
            }
        }
        Map<String, Object> result = new HashMap<>();
        result.put("master",master);
        result.put("details", details);

        return result;
    }

    public Map<String, Object> deleteDetail(long AttFileNo, long seqNo) throws SQLException {
        List<String> listOfTableNames = List.of();
        List<SpParameter> listOfAllSpParameters = new ArrayList<SpParameter>();
        listOfAllSpParameters.add(SpParameter.builder().name("AttFileNo").direction(SpParameter.Direction.IN).value(AttFileNo)  .jdbcType(JDBCType.INTEGER).build());
        listOfAllSpParameters.add(SpParameter.builder().name("OPT").direction(SpParameter.Direction.IN).value(31)  .jdbcType(JDBCType.CHAR).build());
        listOfAllSpParameters.add(SpParameter.builder().name("SEQ").value(seqNo).direction(SpParameter.Direction.INOUT).jdbcType(JDBCType.INTEGER).build());
        SpInfo spInfo = SpInfo.builder()
                .spName("usp_COMMON_AttachedFile_DETAIL")
                .spParameterList(listOfAllSpParameters)
                .tableNames(listOfTableNames)
                .build();
        logger.info(spInfo.toString());
        return dbHelper.execute(spInfo);
    }


    public Map<String, Object> initDetail(Map<String,Object> params) throws SQLException {
        List<String> listOfTableNames = List.of();
        FileDetail_Params pParams = new FileDetail_Params();
        for (String parameter : params.keySet()) {
            SpParameter oldSpParameter =  pParams.getSpParameterByName(SpParameter.SpType.ALL, parameter);
            if(oldSpParameter==null) continue;
            oldSpParameter.setValue(params.get(parameter));
            pParams.replaceSpParameterByName(SpParameter.SpType.ALL,parameter,oldSpParameter);
        }
        SpInfo spInfo = SpInfo.builder()
                .spName("usp_COMMON_AttachedFile_DETAIL")
                .spParameterList(pParams.getListOfAllSpParameters())
                .tableNames(listOfTableNames)
                .build();
        logger.info(spInfo.toString());

        return dbHelper.execute(spInfo);
    }

    /**
     * 조회 - 쿼리조건에 따라 단건/다건 조회
     * @param params
     * @return
     * @throws SQLException
     */
    @Override
    public Map<String, Object> searchDetail(Map<String,Object> params) throws SQLException {
        List<String> listOfTableNames = List.of();
        FileDetail_Params pFile_Params = new FileDetail_Params();
        for (String parameter : params.keySet()) {
            SpParameter oldSpParameter =  pFile_Params.getSpParameterByName(SpParameter.SpType.ALL, parameter);
            if(oldSpParameter==null) continue;
            oldSpParameter.setValue(params.get(parameter));
            pFile_Params.replaceSpParameterByName(SpParameter.SpType.ALL,parameter,oldSpParameter);
        }
        SpInfo spInfo = SpInfo.builder()
                .spName("usp_COMMON_AttachedFile_DETAIL")
                .spParameterList(pFile_Params.getListOfAllSpParameters())
                .tableNames(listOfTableNames)
                .build();
        logger.info(spInfo.toString());
        return dbHelper.execute(spInfo);
    }

    /**
     * 등록 동일
     * @param params
     * @return
     * @throws SQLException
     */
    @Override
    public Map<String, Object> insertDetail(Map<String,Object> params) throws SQLException {
        List<String> listOfTableNames = List.of();
        FileDetail_Params pParams = new FileDetail_Params();
        for (String parameter : params.keySet()) {
            SpParameter oldSpParameter =  pParams.getSpParameterByName(SpParameter.SpType.ALL, parameter);
            if(oldSpParameter==null) continue;
            oldSpParameter.setValue(params.get(parameter));
            pParams.replaceSpParameterByName(SpParameter.SpType.ALL,parameter,oldSpParameter);
        }
        SpInfo spInfo = SpInfo.builder()
                .spName("usp_COMMON_AttachedFile_DETAIL")
                .spParameterList(pParams.getListOfAllSpParameters())
                .tableNames(listOfTableNames)
                .build();
        logger.info(spInfo.toString());
        return dbHelper.executeInAndOutParams(spInfo);
    }

    public Map<String, Object> store(MultipartFile file, long AttFileNo) throws SQLException {

        List<String> listOfTableNames = List.of();
        FileDetail_Params pParams = new FileDetail_Params();
        List<SpParameter> listOfAllSpParameters = new ArrayList<SpParameter>();

        listOfAllSpParameters.add(SpParameter.builder().name("OPT").value(11).direction(SpParameter.Direction.IN).jdbcType(JDBCType.INTEGER).build());
        listOfAllSpParameters.add(SpParameter.builder().name("SEQ").value(0).direction(SpParameter.Direction.INOUT).jdbcType(JDBCType.INTEGER).build());
        listOfAllSpParameters.add(SpParameter.builder().name("AttFileNo").value(AttFileNo).direction(SpParameter.Direction.IN).jdbcType(JDBCType.NUMERIC).build());
        listOfAllSpParameters.add(SpParameter.builder().name("FileName").value(file.getOriginalFilename()).direction(SpParameter.Direction.IN).jdbcType(JDBCType.VARCHAR).build());
        listOfAllSpParameters.add(SpParameter.builder().name("FileExtention").value(file.getOriginalFilename().split("\\.")[1]).direction(SpParameter.Direction.IN).jdbcType(JDBCType.VARCHAR).build());
        listOfAllSpParameters.add(SpParameter.builder().name("FileSize").value(file.getSize()).direction(SpParameter.Direction.IN).jdbcType(JDBCType.DECIMAL).build());

        SpInfo spInfo = SpInfo.builder()
                .spName("usp_COMMON_AttachedFile_DETAIL")
                .spParameterList(listOfAllSpParameters)
                .tableNames(listOfTableNames)
                .build();
        logger.info(spInfo.toString());
        Map<String, Object> detail = dbHelper.executeInAndOutParams(spInfo);
        String seq = String.valueOf(detail.get("SEQ"));
        try {
            if (file.isEmpty()) {
                throw new Exception("파일내용이 없습니다.");
            }
            Path root = Paths.get(uploadPath);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
            //원하는 데이터 포맷 지정
            String subFolder = simpleDateFormat.format(new Date());
            Path targetFoler =Paths.get(uploadPath +"/"+ subFolder);

            if (!Files.exists(targetFoler)) {
                try {
                    Files.createDirectories(targetFoler);
                } catch (IOException e) {
                    throw new RuntimeException("업로드폴더를 생성할수 없습니다.");
                }
            }

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, targetFoler.resolve(AttFileNo+"-"+seq),
                        StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (Exception e) {
            throw new RuntimeException("파일저장에 실패하였습니다.: " + e.getMessage());
        }

        return detail;
    }
    public Map<String, Object> storeSave(MultipartFile file, long AttFileNo) throws SQLException {

        List<String> listOfTableNames = List.of();
        FileDetail_Params pParams = new FileDetail_Params();
        List<SpParameter> listOfAllSpParameters = new ArrayList<SpParameter>();

        listOfAllSpParameters.add(SpParameter.builder().name("OPT").value(11).direction(SpParameter.Direction.IN).jdbcType(JDBCType.INTEGER).build());
        listOfAllSpParameters.add(SpParameter.builder().name("SEQ").value(0).direction(SpParameter.Direction.INOUT).jdbcType(JDBCType.INTEGER).build());
        listOfAllSpParameters.add(SpParameter.builder().name("AttFileNo").value(AttFileNo).direction(SpParameter.Direction.IN).jdbcType(JDBCType.NUMERIC).build());
        listOfAllSpParameters.add(SpParameter.builder().name("FileName").value(file.getOriginalFilename()).direction(SpParameter.Direction.IN).jdbcType(JDBCType.VARCHAR).build());
        listOfAllSpParameters.add(SpParameter.builder().name("FileExtention").value(file.getOriginalFilename().split("\\.")[1]).direction(SpParameter.Direction.IN).jdbcType(JDBCType.VARCHAR).build());
        listOfAllSpParameters.add(SpParameter.builder().name("FileSize").value(file.getSize()).direction(SpParameter.Direction.IN).jdbcType(JDBCType.DECIMAL).build());

        SpInfo spInfo = SpInfo.builder()
                .spName("usp_COMMON_AttachedFile_DETAIL")
                .spParameterList(listOfAllSpParameters)
                .tableNames(listOfTableNames)
                .build();
        logger.info(spInfo.toString());
        Map<String, Object> detail = dbHelper.executeInAndOutParams(spInfo);
        String seq = String.valueOf(detail.get("SEQ"));
        try {
            if (file.isEmpty()) {
                throw new Exception("파일내용이 없습니다.");
            }
            Path root = Paths.get(uploadPath);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
            //원하는 데이터 포맷 지정
           // String subFolder = simpleDateFormat.format(new Date());
            String subFolder =  dbHelper.selectSubFolder(AttFileNo);
            System.out.println(subFolder);
            Path targetFoler =Paths.get(uploadPath +"/"+ subFolder);

            if (!Files.exists(targetFoler)) {
                try {
                    Files.createDirectories(targetFoler);
                } catch (IOException e) {
                    throw new RuntimeException("업로드폴더를 생성할수 없습니다.");
                }
            }

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, targetFoler.resolve(AttFileNo+"-"+seq),
                        StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (Exception e) {
            throw new RuntimeException("파일저장에 실패하였습니다.: " + e.getMessage());
        }

        return detail;
    }
}
