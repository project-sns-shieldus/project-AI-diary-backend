package com.restapi.project_AI_diary_backend.domain.image.service;

import com.restapi.project_AI_diary_backend.domain.diary.entity.Diary;
import com.restapi.project_AI_diary_backend.domain.diary.repository.DiaryRepository;
import com.restapi.project_AI_diary_backend.domain.image.entity.Image;
import com.restapi.project_AI_diary_backend.domain.image.repository.ImageRepository;
import com.restapi.project_AI_diary_backend.domain.image.service.ImageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;
    private final DiaryRepository diaryRepository;

    // 이미지가 저장될 기본 경로를 설정 파일에서 가져옵니다.
    // 파일이 저장될 경로
    private static final String uploadDir = "src/main/resources/static/uploads/";

    public ImageServiceImpl(ImageRepository imageRepository, DiaryRepository diaryRepository) {
        this.imageRepository = imageRepository;
        this.diaryRepository = diaryRepository;
    }

    @Override
    @Transactional
    public void saveImages(Long diaryId, List<String> imageUrls) throws IOException {
        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid diary ID: " + diaryId));

        // 업로드 디렉토리 존재 여부 확인 및 생성
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            boolean dirCreated = directory.mkdirs();
            if (!dirCreated) {
                throw new IOException("Failed to create upload directory: " + uploadDir);
            }
        }

        List<Image> imageEntities = new ArrayList<>();

        for (String imageUrl : imageUrls) {
            if (imageUrl != null && !imageUrl.isEmpty()) {
                // URL에서 파일 확장자 추출
                String extension = getFileExtensionFromUrl(imageUrl);
                if (!isAllowedExtension(extension)) {
                    throw new IllegalArgumentException("Unsupported file type: " + extension);
                }

                // 중복을 방지용 UUID 변환
                String savedFileName = UUID.randomUUID().toString() + extension;

                // 이미지 다운로드 후 로컬에 저장
                String filePath = uploadDir + File.separator + savedFileName;
                downloadImageFromUrl(imageUrl, filePath);

                // 데이터베이스에 파일명만 저장
                Image image = Image.builder()
                        .diary(diary)
                        .fileName(savedFileName) // 파일명만 저장
                        .build();

                imageEntities.add(image);
            }
        }

        imageRepository.saveAll(imageEntities);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Image> getImagesByDiaryId(Long diaryId) {
        return imageRepository.findByDiary_DiaryId(diaryId);
    }

    @Override
    @Transactional
    public void deleteImagesByDiaryId(Long diaryId) throws IOException {
        List<Image> images = imageRepository.findByDiary_DiaryId(diaryId);

        for (Image image : images) {
            // 로컬에 저장된 파일 삭제
            String filePath = uploadDir + File.separator + image.getFileName();
            File file = new File(filePath);
            if (file.exists()) {
                if (!file.delete()) {
                    throw new IOException("Failed to delete file: " + filePath);
                }
            }

            imageRepository.delete(image);
        }
    }

    // 허용된 파일 확장자 검증 메서드
    private boolean isAllowedExtension(String extension) {
        List<String> allowedExtensions = List.of(".jpg", ".jpeg", ".png", ".gif");
        return allowedExtensions.contains(extension.toLowerCase());
    }

    // URL에서 파일 확장자를 추출하는 메서드
    private String getFileExtensionFromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            String path = url.getPath(); // 쿼리 파라미터를 제외한 경로를 반환

            // 경로에서 마지막 '.' 이후의 문자열을 파일 확장자로 취급할거
            return path.substring(path.lastIndexOf("."));
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Invalid URL: " + imageUrl, e);
        }
    }


    // URL로부터 이미지를 다운로드하는 메서드
    private void downloadImageFromUrl(String imageUrl, String destinationFile) throws IOException {
        URL url = new URL(imageUrl);
        try (BufferedInputStream in = new BufferedInputStream(url.openStream());
             FileOutputStream fileOutputStream = new FileOutputStream(destinationFile)) {
            byte[] dataBuffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }
        } catch (IOException e) {
            throw new IOException("Failed to download image from URL: " + imageUrl, e);
        }
    }
}
