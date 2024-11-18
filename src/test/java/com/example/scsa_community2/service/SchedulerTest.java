package com.example.scsa_community2.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SchedulerTest {

    @Autowired
    private AttendanceService attendanceService;

    @Test
    public void testResetAttendance() {
        attendanceService.resetAttendance();
        // 로그 출력이나 attendance_tbl의 데이터가 삭제되었는지 확인
    }
}
