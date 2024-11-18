package com.example.scsa_community2.repository;

import com.example.scsa_community2.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Integer> {
    // Spring Data JPA 방식
    List<Notice> findByNoticeSemester_SemesterId(Integer semesterId);

    // JPQL 방식
    @Query("SELECT n FROM Notice n WHERE n.noticeSemester.semesterId = :semesterId")
    List<Notice> findBySemesterId(@Param("semesterId") Integer semesterId);
}
