package com.joatech.upload.uploadservcenew;

import jakarta.annotation.PostConstruct;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@RestController
@RequestMapping("/images")
public class ImageUploadController {

    @Value("${image.upload-dir:uploaded-images}")
    private String baseUploadDir;

    private final String originalDir = "original";
    private final String thumbnailDir = "thumbnail";

    @PostConstruct
    public void logInit() {
        System.out.println("✅ ImageUploadController 등록됨!");
    }

    @GetMapping("/test")
    public String test() {
        return "image upload controller alive";
    }

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(Paths.get(baseUploadDir, originalDir));
            Files.createDirectories(Paths.get(baseUploadDir, thumbnailDir));
        } catch (IOException e) {
            throw new RuntimeException("디렉토리 생성 실패", e);
        }
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            System.out.println(">>> 파일 이름: " + file.getOriginalFilename());
            System.out.println(">>> Content Type: " + file.getContentType());
            System.out.println(">>> 파일 크기: " + file.getSize());

            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("업로드된 파일이 비어 있습니다.");
            }

            // 디렉토리 재확인
            Files.createDirectories(Paths.get(baseUploadDir, originalDir));
            Files.createDirectories(Paths.get(baseUploadDir, thumbnailDir));

            String ext = StringUtils.getFilenameExtension(file.getOriginalFilename());
            if (ext == null) {
                return ResponseEntity.badRequest().body("파일 확장자가 없습니다.");
            }

            String uniqueName = UUID.randomUUID().toString() + "." + ext;

            Path originalPath = Paths.get(baseUploadDir, originalDir, uniqueName);
            Files.copy(file.getInputStream(), originalPath, StandardCopyOption.REPLACE_EXISTING);

            Path thumbPath = Paths.get(baseUploadDir, thumbnailDir, uniqueName.replace(".", "_thumb."));
            Thumbnails.of(originalPath.toFile())
                    .size(200, 200)
                    .toFile(thumbPath.toFile());

            return ResponseEntity.ok("Uploaded successfully. File name: " + uniqueName);

        } catch (Exception e) {
            e.printStackTrace(); // 콘솔에 전체 예외 출력
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Upload failed: " + e.getMessage());
        }
    }

    @GetMapping("/{type}/{filename:.+}")
    public ResponseEntity<Object> serveImage(
            @PathVariable String type,
            @PathVariable String filename) {

        if (!type.equals("original") && !type.equals("thumbnail")) {
            return ResponseEntity.badRequest().build();
        }

        try {
            Path filePath = Paths.get(baseUploadDir, type, filename);
            if (!Files.exists(filePath)) {
                return ResponseEntity.notFound().build();
            }

            ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(filePath));
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(resource);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
