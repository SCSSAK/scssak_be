package com.example.scsa_community2.repository;

import com.example.scsa_community2.entity.Semester;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SemesterRepository extends JpaRepository<Semester, Integer> {
    Optional<Semester> findBySemesterCpId(String semesterCpId);
}

