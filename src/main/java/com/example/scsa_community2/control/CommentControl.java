package com.example.scsa_community2.control;

import com.example.scsa_community2.dto.request.CommentRequest;
import com.example.scsa_community2.exception.error.BaseException;
import com.example.scsa_community2.exception.error.ErrorCode;
import com.example.scsa_community2.jwt.PrincipalDetails;
import com.example.scsa_community2.service.CommentService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/comment")
@RequiredArgsConstructor
public class CommentControl {

    private final CommentService commentService;

    @PostMapping("/{article_id}")
    public ResponseEntity<Void> createComment(
            @PathVariable("article_id") Long articleId,
            @RequestBody CommentRequest request,
            @AuthenticationPrincipal PrincipalDetails userDetails) {

        if (userDetails == null || userDetails.getUser() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 401 Unauthorized
        }

        try {
            commentService.createComment(articleId, request, userDetails.getUser());
            return ResponseEntity.status(HttpStatus.OK).build(); // 200 OK
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500 Internal Server Error
        }
    }

    @DeleteMapping("/{article_id}/{comment_id}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable("article_id") Long articleId,
            @PathVariable("comment_id") Long commentId,
            @AuthenticationPrincipal PrincipalDetails userDetails) {

        // 로그인 여부 확인
        if (userDetails == null || userDetails.getUser() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 401 Unauthorized
        }

        try {
            commentService.deleteComment(articleId, commentId, userDetails.getUser());
            return ResponseEntity.status(HttpStatus.OK).build(); // 200 OK
        } catch (BaseException e) {
            // 비즈니스 로직 예외 처리
            if (e.getErrorCode() == ErrorCode.NOT_FOUND_DATA) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 Not Found
            }
            if (e.getErrorCode() == ErrorCode.NOT_PRIVILEGED) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 401 Unauthorized
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 기타 예외 500
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500 Internal Server Error
        }
    }

}
