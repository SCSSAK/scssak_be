package com.example.scsa_community2.repository;

import com.example.scsa_community2.entity.Comment;
import com.example.scsa_community2.entity.CommentId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, CommentId> {
    // 필요한 경우 커스텀 쿼리 메서드 추가 가능
}
