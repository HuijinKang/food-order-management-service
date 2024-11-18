package org.sparta.foodordermanagementservice.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

    // 파일을 S3에 업로드하고 파일 URL을 반환
    String uploadFile(MultipartFile file);

    // 파일을 S3에서 삭제하고 데이터베이스에서도 삭제하는 메서드
    void deleteFile(String fileUrl);

    // 파일명을 생성하는 메서드
    String generateFileName(String originalFileName);

    // 파일 URL을 반환하는 메서드
    String getFileUrl(String fileName);
}
