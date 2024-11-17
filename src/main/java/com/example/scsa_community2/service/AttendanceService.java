package com.example.scsa_community2.service;

import com.example.scsa_community2.entity.Attendance;
import com.example.scsa_community2.entity.Semester;
import com.example.scsa_community2.entity.User;
import com.example.scsa_community2.repository.AttendanceRepository;
import com.example.scsa_community2.repository.SemesterRepository;
import com.example.scsa_community2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;
    private final SemesterRepository semesterRepository;

    public ResponseEntity<Void> markAttendance(String userId) {
        try {
            // 유저 확인
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // 재학생 여부 확인
            if (!Boolean.TRUE.equals(user.getUserIsStudent())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // 400: 재학생 아님
            }

            // 이미 출석한 경우 확인
            if (attendanceRepository.existsByUserId(userId)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // 400: 이미 출석
            }

            // 반장이 속한 학기 가져오기
            Semester currentSemester = semesterRepository.findBySemesterCpId(user.getUserSemester().getSemesterCpId())
                    .orElseThrow(() -> new RuntimeException("No active semester found"));

            // 학기별 지각 시간 가져오기
            LocalTime tardyTime = currentSemester.getSemesterTardyTime();
            if (tardyTime == null) {
                throw new RuntimeException("Tardy time is not set for the semester.");
            }

            LocalDateTime now = LocalDateTime.now();

            // 출석 데이터 생성
//            Attendance attendance = new Attendance();
//            attendance.setUserId(userId);
//            attendance.setAttendanceTime(now);

            // 출석 데이터 생성
            Attendance attendance = new Attendance();
            attendance.setUser(user);  // User 엔티티 설정
            attendance.setAttendanceTime(now);

            // 지각 여부 판단
            if (now.toLocalTime().isBefore(tardyTime)) {
                log.info("User {} is on time.", userId);
            } else {
                log.info("User {} is late.", userId);
            }

            // DB에 저장
            attendanceRepository.save(attendance);
            return ResponseEntity.status(HttpStatus.OK).build(); // 200: 성공

        } catch (Exception e) {
            log.error("Error while marking attendance: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500: 서버 오류
        }
    }

    @Scheduled(cron = "0 0 0 * * ?") // 매일 자정 실행
    public void resetAttendance() {
        log.info("Resetting attendance table...");
        attendanceRepository.deleteAll();
    }
}
