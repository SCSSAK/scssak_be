package com.example.scsa_community2.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ArticleRequest {

    private String articleTitle;
    private String articleContent;
    private int articleType;
    private boolean articleIsOpen;
    private String userId;  // 사용자 ID
    private List<MultipartFile> images;

    // 생성자나 추가적인 필드가 필요할 수 있음
}
