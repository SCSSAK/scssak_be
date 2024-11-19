package com.example.scsa_community2.control;

import com.example.scsa_community2.exception.BaseException;
import com.example.scsa_community2.jwt.PrincipalDetails;
import com.example.scsa_community2.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/like")
@RequiredArgsConstructor
public class LikeControl {

    private final LikeService likeService;

    @PostMapping("/{articleId}")
    public ResponseEntity<Void> toggleLike(
            @PathVariable("articleId") Long articleId,
            @AuthenticationPrincipal PrincipalDetails userDetails) {

        // 로그인하지 않은 경우
        if (userDetails == null || userDetails.getUser() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 401 Unauthorized
        }

        try {
            // 좋아요 토글 로직
            likeService.toggleLike(articleId, userDetails.getUser());
            return ResponseEntity.status(HttpStatus.OK).build(); // 200 OK
        } catch (BaseException e) {
            return ResponseEntity.status(HttpStatus.valueOf(e.getErrorCode().getErrorCode())).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500 Internal Server Error
        }
    }
}

