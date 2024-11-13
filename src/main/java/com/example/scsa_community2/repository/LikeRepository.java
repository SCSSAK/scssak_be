package com.example.scsa_community2.repository;

import com.example.scsa_community2.entity.Like;
import com.example.scsa_community2.entity.LikeId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, LikeId> {
    // 필요한 경우 커스텀 쿼리 메서드 추가 가능
}
