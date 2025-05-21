package com.joatech.upload.uploadservcenew;

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
import java.net.*;
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

    @PostMapping("/images/upload")
    public ResponseEntity<Object> uploadImages(
            @RequestParam("file") MultipartFile[] files,
            HttpServletRequest request
    ) {
        if (files == null || files.length == 0) {
            return ResponseEntity.badRequest().body(Map.of("error", "파일이 비어 있습니다."));
        }

        List<Map<String, String>> uploadedResults = new ArrayList<>();

        String serverUrl = request.getScheme() + "://" + getPublicIp() + ":" + request.getServerPort();

        try {
            for (MultipartFile file : files) {
                if (file.isEmpty()) continue;

                String ext = StringUtils.getFilenameExtension(file.getOriginalFilename());
                if (ext == null) continue;

                String baseName = UUID.randomUUID().toString();
                String originalFileName = baseName + "." + ext;
                String thumbnailFileName = baseName + "_thumb." + ext;

                Path originalPath = Paths.get(baseUploadDir, originalFileName);
                Files.copy(file.getInputStream(), originalPath, StandardCopyOption.REPLACE_EXISTING);

                Path thumbPath = Paths.get(baseUploadDir, thumbnailFileName);
                Thumbnails.of(originalPath.toFile())
                        .size(200, 200)
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
            Path filePath = Paths.get(baseUploadDir, filename);
            if (!Files.exists(filePath)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("파일이 존재하지 않습니다.");
            }

            String mimeType = Files.probeContentType(filePath);
            MediaType mediaType = (mimeType != null) ? MediaType.parseMediaType(mimeType) : MediaType.APPLICATION_OCTET_STREAM;

            ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(filePath));

            return ResponseEntity.ok()
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
}
