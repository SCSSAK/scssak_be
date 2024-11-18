package com.example.scsa_community2.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
//import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.core.sync.RequestBody;
import org.springframework.web.multipart.MultipartFile;

@Service
public class S3Service {

    private static final Logger logger = LoggerFactory.getLogger(S3Service.class);


    private final S3Client s3Client;
    @Value("${cloud.aws.s3.bucket}") // application.properties의 S3_BUCKET 값 주입
    private String bucketName;

    public S3Service(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public String uploadFile(MultipartFile file) {
        try {
            String keyName = "uploads/" + file.getOriginalFilename();

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(keyName)
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));

            // AWS SDK를 통해 URL 생성
            return s3Client.utilities().getUrl(builder ->
                    builder.bucket(bucketName).key(keyName)).toExternalForm();
        } catch (Exception e) {
            throw new RuntimeException("S3 파일 업로드에 실패했습니다.", e);
        }
    }


    // 파일 삭제
    public void deleteFile(String fileUrl) {
        if (fileUrl == null || fileUrl.isEmpty()) {
            logger.warn("잘못된 파일 URL: 삭제 요청이 생략됩니다.");
            return; // 파일 URL이 없으면 삭제 작업 생략
        }

        // 리전 정보 포함된 URL 처리
        String region = "ap-northeast-2"; // S3 버킷 리전
        String bucketUrl = "https://" + bucketName + ".s3." + region + ".amazonaws.com/";
        String keyName = fileUrl.replace(bucketUrl, "");

        try {
            s3Client.deleteObject(builder -> builder.bucket(bucketName).key(keyName).build());
            logger.info("S3 파일 삭제 성공: {}", fileUrl);
        } catch (Exception e) {
            logger.error("S3 파일 삭제 실패: {}", e.getMessage());
            throw new RuntimeException("S3 파일 삭제에 실패했습니다.", e);
        }
    }



}
