package com.joatech.upload.uploadservcenew.main;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

@RestController
public class ImageUploadController {

    @Value("${image.upload-dir:uploaded-images}")
    private String baseUploadDir;

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(Paths.get(baseUploadDir));
            System.out.println("✅ 업로드 디렉토리 생성됨: " + Paths.get(baseUploadDir).toAbsolutePath());
        } catch (IOException e) {
            throw new RuntimeException("디렉토리 생성 실패: " + baseUploadDir, e);
        }
    }

    @GetMapping("/images/test")
    public String test() {
        return "image upload controller alive";
    }


    @CrossOrigin(origins = "*")
    @PostMapping("/images/upload")
    public ResponseEntity<Object> uploadImages(
            @RequestParam("file") MultipartFile[] files,
            HttpServletRequest request
    ) {
        if (files == null || files.length == 0) {
            return ResponseEntity.badRequest().body(Map.of("error", "파일이 비어 있습니다."));
        }

        List<Map<String, String>> uploadedResults = new ArrayList<>();

        //String serverUrl = request.getScheme() + "://" + getPublicIp() + ":" + request.getServerPort();

        try {
            for (MultipartFile file : files) {
                if (file.isEmpty()) continue;

                String baseName = UUID.randomUUID().toString();
                String originalFileName = baseName + ".jpg";      // 강제 확장자
                String thumbnailFileName = baseName + "_thumb.jpg";

                Path originalPath = Paths.get(baseUploadDir, originalFileName);
                Path thumbPath = Paths.get(baseUploadDir, thumbnailFileName);

                // ✅ 원본을 JPG로 저장
                Thumbnails.of(file.getInputStream())
                        .scale(1.0)
                        .outputFormat("jpg")
                        .outputQuality(0.92f)
                        .toFile(originalPath.toFile());

                // ✅ 썸네일 생성도 JPG로
                Thumbnails.of(originalPath.toFile())
                        .size(400, 400)
                        .outputFormat("jpg")
                        .outputQuality(0.85f)
                        .toFile(thumbPath.toFile());

                uploadedResults.add(Map.of(
                        "originalUrl", "/images/" + originalFileName,
                        "thumbnailUrl", "/images/" + thumbnailFileName
                ));
            }


            if (uploadedResults.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "업로드 가능한 이미지가 없습니다."));
            }

            return ResponseEntity.ok(uploadedResults);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Upload failed: " + e.getMessage()));
        }
    }

    @GetMapping("/images/{filename:.+}")
    public ResponseEntity<Object> serveImage(@PathVariable String filename) {
        try {
            // 경로 조작 방지
            if (filename.contains("..")) {
                return ResponseEntity.badRequest().body("잘못된 파일 요청입니다.");
            }

            Path filePath = Paths.get(baseUploadDir, filename);
            if (!Files.exists(filePath)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("파일이 존재하지 않습니다.");
            }

            String mimeType = Files.probeContentType(filePath);
            MediaType mediaType = (mimeType != null)
                    ? MediaType.parseMediaType(mimeType)
                    : MediaType.APPLICATION_OCTET_STREAM;

            ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(filePath));

            return ResponseEntity.ok()
                    .header(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*") // CORS 허용
                    .contentType(mediaType)
                    .contentLength(Files.size(filePath))
                    .body(resource);

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("이미지 로드 실패: " + e.getMessage());
        }
    }


    private String getPublicIp() {
        try {
            RestTemplate restTemplate = new RestTemplate();
            return restTemplate.getForObject("https://api.ipify.org", String.class);
        } catch (Exception e) {
            e.printStackTrace();
            return "127.0.0.1"; // fallback
        }
    }


    @GetMapping("/images/base64/{filename:.+}")
    public ResponseEntity<Object> serveImageAsBase64(@PathVariable String filename) {
        try {
            // 경로 조작 방지
            if (filename.contains("..")) {
                return ResponseEntity.badRequest().body("잘못된 파일 요청입니다.");
            }

            Path filePath = Paths.get(baseUploadDir, filename);
            if (!Files.exists(filePath)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("파일이 존재하지 않습니다.");
            }

            String mimeType = Files.probeContentType(filePath);
            byte[] fileBytes = Files.readAllBytes(filePath);
            String base64Encoded = Base64.getEncoder().encodeToString(fileBytes);

            Map<String, Object> response = new HashMap<>();
            response.put("filename", filename);
            response.put("contentType", mimeType);
            response.put("base64", "data:" + mimeType + ";base64," + base64Encoded);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Base64 인코딩 실패: " + e.getMessage()));
        }
    }

}
