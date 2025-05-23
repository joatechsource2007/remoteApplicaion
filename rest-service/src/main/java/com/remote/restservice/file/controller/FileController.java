package com.remote.restservice.file.controller;


import com.remote.restservice.file.service.FileService;
import com.remote.restservice.common.CommonResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import java.io.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;


@RestController
@RequestMapping("/file")
public class FileController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileController.class);

    private final FileService service;

    @Value("${spring.servlet.multipart.location}")
    private String uploadPath;

    @Value("${spring.servlet.multipart.location}")
    private String imgPath;

    public FileController(FileService service) {
        this.service = service;
    }

    /**
     * 파일업로드
     * @param files
     * @return
     */

    @RequestMapping(value="/uploadFile", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE })
    public CommonResponse insert(@RequestPart (value="files",required = false) MultipartFile[] files, @RequestPart(value="params",required = false) Map<String,Object> params) {
        LOGGER.info("FileController.insert() accepted on {} {}", files, params);
        try {
            return CommonResponse.builder()
                    .code("SUCCESS")
                    .status(HttpStatus.OK.value())
                    .message("파일마스터 저장")
                    .data(service.insert(files,params))
                    .build();
        } catch (RuntimeException | SQLException e) {
            return CommonResponse.builder()
                    .code("FAIL")
                    .status(HttpStatus.OK.value())
                    .message(e.getMessage())
                    .data(null)
                    .build();
        }
    }
    @RequestMapping(value="/saveFile", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE })
    public CommonResponse save(@RequestPart (value="files",required = false) MultipartFile[] files, @RequestPart(value="params",required = false) Map<String,Object> params) {
        LOGGER.info("FileController.save() accepted on {} {}", files, params);
        try {
            return CommonResponse.builder()
                    .code("SUCCESS")
                    .status(HttpStatus.OK.value())
                    .message("파일마스터 수정")
                    .data(service.save(files,params))
                    .build();
        } catch (RuntimeException | SQLException e) {
            return CommonResponse.builder()
                    .code("FAIL")
                    .status(HttpStatus.OK.value())
                    .message(e.getMessage())
                    .data(null)
                    .build();
        }
    }

    @RequestMapping("/{AttFileNo}")
    public CommonResponse list(@PathVariable long AttFileNo) {
        LOGGER.info("FileController.list() accepted on {}", AttFileNo);
        try {
            return CommonResponse.builder()
                    .code("SUCCESS")
                    .status(HttpStatus.OK.value())
                    .message("파일마스터 조회")
                    .data(service.search(AttFileNo))
                    .build();
        } catch (RuntimeException | SQLException e) {
            return CommonResponse.builder()
                    .code("FAIL")
                    .status(HttpStatus.OK.value())
                    .message(e.getMessage())
                    .data(null)
                    .build();
        }
    }
    @RequestMapping("/deletefileDetail/{fileId}")
    public CommonResponse deleteDetail(@PathVariable String fileId) throws UnsupportedEncodingException {
        LOGGER.info("FileController.deleteDetail() accepted on {}", fileId);
        long AttFileNo = Long.parseLong(fileId.split("-")[0]);
        long SeqNo = Long.parseLong(fileId.split("-")[1]);
        try {
            return CommonResponse.builder()
                    .code("SUCCESS")
                    .status(HttpStatus.OK.value())
                    .message("파일마스터 저장")
                    .data(service.deleteDetail(AttFileNo, SeqNo))
                    .build();
        } catch (RuntimeException | SQLException e) {
            return CommonResponse.builder()
                    .code("FAIL")
                    .status(HttpStatus.OK.value())
                    .message(e.getMessage())
                    .data(null)
                    .build();
        }
    }

    /**
     * 파일 다운로드
     * @param fileId
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping("/downloadFile/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileId) throws UnsupportedEncodingException {
        UrlResource resource;
        LOGGER.info("FileController.downloadFile() accepted on {}", fileId);
        try {
            long AttFileNo = Long.parseLong(fileId.split("-")[0]);
            Map<String, Object> list = service.search(AttFileNo);
            LOGGER.info("fileList :  {}", list);
            List<Map<String, Object>> files = (List<Map<String, Object>>) list.get("TABLE0");
            Map<String, Object> result = null;
            for (int i = 0; i < files.size(); i++) {
                Map<String, Object> r = files.get(i);
                if(((String)r.get("FileID")).equals(fileId)){
                    result = r;
                    break;
                }
            }
            if(result !=null){
                String EDPDateTime = result.get("EDPDateTime").toString();
                String FileName = (String) result.get("FileName");
                String FileID = (String) result.get("FileID");
                LOGGER.info("FileID :  {}, FileName :  {}, EDPDateTime :  {}, ", FileID, FileName, EDPDateTime);
                resource = new UrlResource("file:"+ uploadPath +"/"+ EDPDateTime.substring(0,10).replaceAll("-","")+"/"+fileId);
                String orgFileName = encodeFileName(FileName);
                return ResponseEntity.ok()
                        //.contentType(MediaType.valueOf(Files.probeContentType(Paths.get(uploadPath +"/"+ EDPDateTime.substring(0,10).replaceAll("-","")+"/"+fileId))))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" +orgFileName  + "\"")
                        .body(resource);
            }

        } catch (RuntimeException | SQLException | IOException e) {
            LOGGER.info("오류가 발생하였습니다.");
        }

        return null;
    }

    /**
     * 파일 다운로드
     * @param fileId
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping("/imgdownloadfile/{fileId}")
    public ResponseEntity<Resource> imgDownloadFile(@PathVariable String fileId) throws UnsupportedEncodingException {
        UrlResource resource;
        LOGGER.info("FileController.imgDownloadFile() accepted on {}", fileId);
        try {
                LOGGER.info("FileID :  {} ", fileId);
                resource = new UrlResource("file:"+ imgPath + "/" +fileId);

                return ResponseEntity.ok()
                        //.contentType(MediaType.valueOf(Files.probeContentType(Paths.get(uploadPath +"/"+ EDPDateTime.substring(0,10).replaceAll("-","")+"/"+fileId))))
                        .contentType(MediaType.IMAGE_JPEG)  // 이미지 타입 설정 (여기서는 JPEG로 설정)
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" +resource.getFilename()  + "\"")
                        .body(resource);
        } catch (RuntimeException | IOException e) {
            LOGGER.info("오류가 발생하였습니다.");
        }

        return null;
    }

    @RequestMapping("/imgloadfile/{fileId}")
    public static BufferedImage imgloadfile(@PathVariable String fileId) throws UnsupportedEncodingException {
        //UrlResource resource;
        String imgPaths = "c:/18.JOA_RemoteMoniter/Tank_IMG";
        LOGGER.info("FileController.imgDownloadFile() accepted on {}", fileId);
        try {
            LOGGER.info("FileID :  {} ", fileId);
            LOGGER.info("Image_path :  {} ", imgPaths);

            File inputImage = new File(imgPaths + "/" +fileId);
            return ImageIO.read(inputImage);

        } catch (RuntimeException | IOException e) {
            LOGGER.info("오류가 발생하였습니다.");
        }

        return null;
    }
    /**
     * 파일명 인코딩
     * @param fileName
     * @return
     * @throws UnsupportedEncodingException
     */
    private  String encodeFileName(String fileName) throws UnsupportedEncodingException {
        return URLEncoder.encode(fileName, "UTF-8").replace("+", "%20");
    }
    
}
