package com.example.scsa_community2.repository;

import com.example.scsa_community2.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findById(String userId);
    @Query("SELECT u FROM User u WHERE u.userSemester.semesterId = :semesterId")
    List<User> findUsersBySemesterId(@Param("semesterId") Integer semesterId);

    @Query("""
    SELECT u.userName 
    FROM User u 
    LEFT JOIN Attendance a ON u.userId = a.user.userId
    WHERE u.userSemester.semesterTardyTime IS NOT NULL
      AND (a.attendanceTime IS NULL OR 
           a.attendanceTime > FUNCTION('TIMESTAMP', CURRENT_DATE, u.userSemester.semesterTardyTime))
""")
    List<String> findAbsentUsers();

    // 재학생만 조회하는 메서드
    List<User> findAllByUserIsStudentTrue();

    // 같은 학기 메일 유저들 이름 순서대로 반환하기
    @Query("SELECT u FROM User u WHERE u.userSemester.semesterId = :semester ORDER BY u.userName ASC")
    List<User> findUsersBySemesterIdSortedByName(@Param("semester") Integer semester);
}
