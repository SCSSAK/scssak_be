package com.example.scsa_community2.repository;

import com.example.scsa_community2.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, String> {
    // 필요에 따라 추가적인 메서드를 정의할 수 있습니다.
}
