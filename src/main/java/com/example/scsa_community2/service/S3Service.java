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

    // 파일 삭제
    public void deleteFile(String fileUrl) {
        try {
            String keyName = fileUrl.replace("https://" + bucketName + ".s3.amazonaws.com/", "");
            s3Client.deleteObject(builder ->
                    builder.bucket(bucketName).key(keyName).build());
            logger.info("S3 파일 삭제 성공: {}", fileUrl);
        } catch (Exception e) {
            logger.error("S3 파일 삭제 실패: {}", e.getMessage());
            throw new RuntimeException("S3 파일 삭제에 실패했습니다.", e);
        }
    }

}
