package com.example.scsa_community2.repository;

import com.example.scsa_community2.entity.Attendance;
import jakarta.transaction.Transactional;
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


//    @Modifying
//    @Transactional
//    @Query(value = "DELETE FROM attendance_tbl WHERE user_id IS NOT NULL", nativeQuery = true)
//    void deleteAllAttendances();

    //    @Modifying
//    @Transactional
//    @Query(value = """
//            SET SQL_SAFE_UPDATES = 0;
//            DELETE FROM table_name WHERE num > 1;
//            SET SQL_SAFE_UPDATES = 1;
//            """, nativeQuery = true)
//    void deleteWithSafeUpdateDisabled();
    @Modifying
    @Transactional
    @Query(value = "SET SQL_SAFE_UPDATES = 0", nativeQuery = true)
    void disableSafeUpdates();

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM attendance_tbl WHERE user_id IS NOT NULL", nativeQuery = true)
    void deleteRows();

    @Modifying
    @Transactional
    @Query(value = "SET SQL_SAFE_UPDATES = 1", nativeQuery = true)
    void enableSafeUpdates();

}

