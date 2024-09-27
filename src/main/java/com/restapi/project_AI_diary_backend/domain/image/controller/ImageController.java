package com.restapi.project_AI_diary_backend.domain.image.controller;

import com.restapi.project_AI_diary_backend.domain.image.entity.Image;
import com.restapi.project_AI_diary_backend.domain.image.service.ImageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    private final ImageService imageService;

    @Value("${image.upload.dir}")
    private String uploadDir;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    // 수정: consume 제거 및 @RequestParam 변경
    @PostMapping(value = "/{diaryId}")
    public ResponseEntity<String> uploadImages(@PathVariable("diaryId") Long diaryId,
                                               @RequestParam("imageUrls") List<String> imageUrls) {
        try {
            imageService.saveImages(diaryId, imageUrls);
            return ResponseEntity.ok("Images uploaded successfully.");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Failed to upload images.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/diary/{diaryId}")
    public ResponseEntity<List<Image>> getImagesByDiaryId(@PathVariable("diaryId") Long diaryId) {
        List<Image> images = imageService.getImagesByDiaryId(diaryId);
        if (images.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(images);
    }

    @GetMapping("/{fileName}")
    public ResponseEntity<Resource> getImage(@PathVariable String fileName) {
        try {
            String filePath = uploadDir + File.separator + fileName;
            File file = new File(filePath);
            if (!file.exists()) {
                return ResponseEntity.notFound().build();
            }
            Resource resource = new FileSystemResource(file);

            String contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
            if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
                contentType = MediaType.IMAGE_JPEG_VALUE;
            } else if (fileName.endsWith(".png")) {
                contentType = MediaType.IMAGE_PNG_VALUE;
            } else if (fileName.endsWith(".gif")) {
                contentType = MediaType.IMAGE_GIF_VALUE;
            }

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + "\"")
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @DeleteMapping("/diary/{diaryId}")
    public ResponseEntity<String> deleteImagesByDiaryId(@PathVariable("diaryId") Long diaryId) {
        try {
            imageService.deleteImagesByDiaryId(diaryId);
            return ResponseEntity.ok("Images deleted successfully.");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Failed to delete images.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
