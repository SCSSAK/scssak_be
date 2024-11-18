package com.example.scsa_community2.repository;

import com.example.scsa_community2.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    boolean existsByUserId(String userId);
    @Query("SELECT u.userName FROM User u WHERE u.userSemester.semesterCpId = :cpId AND u.userId NOT IN (SELECT a.userId FROM Attendance a)")
    List<String> findAbsentUserNamesBySemesterCpId(@Param("cpId") String cpId);
    @Modifying
    @Query(value = "DELETE FROM attendance_tbl WHERE 1=1", nativeQuery = true)
    void deleteAllAttendances();

}

