package com.remote.restservice.customer.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

@RestController
public class ImageController {

    /*================================================================
     * Private Members
     ================================================================*/
    private final Logger logger = LoggerFactory.getLogger(getClass());

    // 이미지가 저장된 경로
    private final Path imageFolder = Paths.get("C:\\18.JOA_RemoteMoniter\\Tank_IMG");

    @GetMapping("/image/{imageName}")
    public ResponseEntity<Resource> getImage(@PathVariable String imageName) throws IOException {
        // 이미지 파일의 경로 생성
        Path imagePath = imageFolder.resolve(imageName).normalize();
        Resource resource = new UrlResource(imagePath.toUri());

        // 파일이 존재하지 않으면 404 오류 반환
        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }

        // 이미지 반환 (Content-Type 설정)
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)  // 이미지 타입 설정 (여기서는 JPEG로 설정)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }


    @GetMapping("/singimage/{imageName}")
    public ResponseEntity<Resource> getSingImage(@PathVariable String imageName) throws IOException {
        // 이미지 파일의 경로 생성
        Path imagePath = imageFolder.resolve(imageName).normalize();
        Resource resource = new UrlResource(imagePath.toUri());

        // 파일이 존재하지 않으면 404 오류 반환
        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }

        // 이미지 반환 (Content-Type 설정)
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)  // 이미지 타입 설정 (여기서는 JPEG로 설정)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }


    public void CreateSignImg(String filename, String imgData){

        Path AnContSingImg =  imageFolder.resolve(filename).normalize();
        String filePathIng = AnContSingImg + ".jpg";
        String token[] = imgData.split(",");

        String base64String = token[1]; // 실제 Base64 문자열로 대체하세요
        base64String = base64String.replaceAll("\\r\\n|\\r|\\n","");

        // Base64 문자열을 바이트 배열로 디코딩
        byte[] imageBytes = Base64.getDecoder().decode(base64String);

        // 디코딩된 바이트 배열을 이미지 파일로 저장
        try (FileOutputStream fos = new FileOutputStream(filePathIng)) {
            fos.write(imageBytes);
            System.out.println("이미지 파일이 저장되었습니다.");
        } catch (IOException e) {
            e.printStackTrace();
        }


    }



}
