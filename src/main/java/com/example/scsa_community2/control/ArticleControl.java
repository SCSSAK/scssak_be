package com.example.scsa_community2.control;

import com.example.scsa_community2.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/article")
public class ArticleControl {
    private final ArticleService articleService;

//    @PostMapping
//    public ResponseEntity<> createArticle
//            //TODO

}
