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
import java.util.Map;
import java.util.UUID;

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
    public ResponseEntity<Object> uploadImage(
            @RequestParam("file") MultipartFile file,
            HttpServletRequest request
    ) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "업로드된 파일이 비어 있습니다."));
            }

            Files.createDirectories(Paths.get(baseUploadDir));

            String ext = StringUtils.getFilenameExtension(file.getOriginalFilename());
            if (ext == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "파일 확장자가 없습니다."));
            }

            String baseName = UUID.randomUUID().toString();
            String originalFileName = baseName + "." + ext;
            String thumbnailFileName = baseName + "_thumb." + ext;

            Path originalPath = Paths.get(baseUploadDir, originalFileName);
            Files.copy(file.getInputStream(), originalPath, StandardCopyOption.REPLACE_EXISTING);

            Path thumbPath = Paths.get(baseUploadDir, thumbnailFileName);
            Thumbnails.of(originalPath.toFile())
                    .size(200, 200)
                    .toFile(thumbPath.toFile());

            // ✅ 공인 IP 가져오기
            String publicIp = getPublicIp();
            String serverUrl = request.getScheme() + "://" + publicIp + ":" + request.getServerPort();

            String originalUrl = serverUrl + "/images/" + originalFileName;
            String thumbnailUrl = serverUrl + "/images/" + thumbnailFileName;

            Map<String, String> response = Map.of(
                    "originalUrl", originalUrl,
                    "thumbnailUrl", thumbnailUrl
            );

            return ResponseEntity.ok(response);

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

    // ✅ 외부 API로 공인 IP 가져오는 메서드
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
