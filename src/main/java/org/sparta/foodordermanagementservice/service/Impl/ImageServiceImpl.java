package org.sparta.foodordermanagementservice.service.Impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.sparta.foodordermanagementservice.entity.FileContentType;
import org.sparta.foodordermanagementservice.repository.MenuRepository;
import org.sparta.foodordermanagementservice.service.ImageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final AmazonS3 amazonS3;
    private final MenuRepository menuRepository;

    @Value("${S3_BUCKETNAME}")
    private String bucketName;

    // 파일을 S3에 업로드하고 파일 URL을 반환
    public String uploadFile(MultipartFile file) {
        try {
            // 파일 타입 검사
            String contentType = file.getContentType();
            if (FileContentType.getContentType(contentType) == null) {
                throw new IllegalArgumentException("허용되지 않는 파일 타입입니다: " + contentType);
            }

            // 파일 이름 생성
            String fileName = generateFileName(file.getOriginalFilename());

            // 메타데이터 설정
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());

            // S3에 파일 업로드
            amazonS3.putObject(bucketName, fileName, file.getInputStream(), metadata);

            // 파일 URL 생성
            String fileUrl = getFileUrl(fileName);

            return fileUrl;
        }
        catch (IOException e) {
            throw new IllegalArgumentException("이미지를 업로드할 수 없습니다. " + e.getMessage());
        }
    }

    // 파일을 S3에서 삭제하는 메서드
    public void deleteFile(String fileUrl) {
        try {
            // S3에서 객체 삭제
            String fileName = getFileNameFromUrl(fileUrl);  // URL에서 파일명 추출
            amazonS3.deleteObject(bucketName, fileName);
        }
        catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("이미지를 삭제할 수 없습니다. " + e.getMessage());
        }
    }

    // 파일명 생성 (UUID 사용)
    public String generateFileName(String originalFileName) {
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        return UUID.randomUUID().toString() + fileExtension;  // 예: 123e4567-e89b-12d3-a456-426614174000.jpg
    }

    // 파일 URL을 반환하는 메서드
    public String getFileUrl(String fileName) {
        if (!amazonS3.doesObjectExist(bucketName, fileName)) {
            throw new IllegalArgumentException("이미지를 업로드할 수 없습니다.");
        }
        return amazonS3.getUrl(bucketName, fileName).toString();
    }

    // URL에서 파일명을 추출하는 메서드 (이미지 삭제 시 사용)
    private String getFileNameFromUrl(String fileUrl) {
        return fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
    }

}
