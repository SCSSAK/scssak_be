package com.example.scsa_community2.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
//import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.core.sync.RequestBody;
import org.springframework.web.multipart.MultipartFile;

@Service
public class S3Service {

    private final S3Client s3Client;
    @Value("${cloud.aws.s3.bucket}") // application.properties의 S3_BUCKET 값 주입
    private String bucketName;

    public S3Service(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public String uploadFile(MultipartFile file) {
        try {
            // 파일 이름을 S3에 저장할 고유 키로 사용 (필요에 따라 파일명을 가공 가능)
            String keyName = "uploads/" + file.getOriginalFilename();

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(keyName)
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));

            return "https://" + bucketName + ".s3.amazonaws.com/" + keyName;
        } catch (Exception e) {
            throw new RuntimeException("S3 파일 업로드에 실패했습니다.", e);
        }
    }

}
