package org.sparta.foodordermanagementservice.service.Impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.sparta.foodordermanagementservice.common.exeption.CustomException;
import org.sparta.foodordermanagementservice.common.exeption.ErrorCode;
import org.sparta.foodordermanagementservice.entity.FileContentType;
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

    @Value("${S3_BUCKETNAME}")
    private String bucketName;

    public String uploadFile(MultipartFile file) {
        try {
            String contentType = file.getContentType();
            if (FileContentType.getContentType(contentType) == null) {
                throw new CustomException(ErrorCode.INVALID_FILE_TYPE);
            }

            String fileName = generateFileName(file.getOriginalFilename());

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());

            amazonS3.putObject(bucketName, fileName, file.getInputStream(), metadata);

            String fileUrl = getFileUrl(fileName);

            return fileUrl;
        }
        catch (IOException e) {
            throw new CustomException(ErrorCode.FILE_UPLOAD_FAILED);
        }
    }

    // 파일을 S3에서 삭제하는 메서드
    public void deleteFile(String fileUrl) {
        try {
            String fileName = getFileNameFromUrl(fileUrl);
            amazonS3.deleteObject(bucketName, fileName);
        }
        catch (Exception e) {
            throw new CustomException(ErrorCode.FILE_DELETE_FAILED);
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
            throw new CustomException(ErrorCode.FILE_UPLOAD_FAILED);
        }
        return amazonS3.getUrl(bucketName, fileName).toString();
    }

    // URL에서 파일명을 추출하는 메서드 (이미지 삭제 시 사용)
    private String getFileNameFromUrl(String fileUrl) {
        return fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
    }

}
