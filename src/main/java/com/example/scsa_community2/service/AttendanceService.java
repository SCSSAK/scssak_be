package com.example.scsa_community2.service;

import com.example.scsa_community2.dto.response.ArticleResponse;
import com.example.scsa_community2.dto.response.MainPageInfo;
import com.example.scsa_community2.entity.Attendance;
import com.example.scsa_community2.entity.Notice;
import com.example.scsa_community2.entity.Semester;
import com.example.scsa_community2.entity.User;
import com.example.scsa_community2.exception.BaseException;
import com.example.scsa_community2.exception.ErrorCode;
import com.example.scsa_community2.repository.AttendanceRepository;
import com.example.scsa_community2.repository.NoticeRepository;
import com.example.scsa_community2.repository.SemesterRepository;
import com.example.scsa_community2.repository.UserRepository;
import jakarta.transaction.Transactional;
import jdk.jfr.TransitionTo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;
    private final SemesterRepository semesterRepository;
    private final NoticeRepository noticeRepository;
    private final ArticleService articleService;

    public ResponseEntity<Void> markAttendance(String userId) {
        try {
            // 유저 확인
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // 재학생 여부 확인
            if (!Boolean.TRUE.equals(user.getUserIsStudent())) {
                log.warn("User {} is not an active student. Attendance not marked.", userId);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // 400: 재학생 아님
            }

            // 이미 출석한 경우 확인
            if (attendanceRepository.existsByUserId(userId)) {
                log.warn("User {} has already marked attendance.", userId);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // 400: 이미 출석
            }

            // 학기 정보 가져오기
            Semester currentSemester = semesterRepository.findBySemesterCpId(user.getUserSemester().getSemesterCpId())
                    .orElseThrow(() -> new RuntimeException("No active semester found"));

            // 지각 기준 시간 확인
            LocalTime tardyTime = currentSemester.getSemesterTardyTime();
            if (tardyTime == null) {
                throw new RuntimeException("Tardy time is not set for the semester.");
            }

            LocalDateTime now = LocalDateTime.now();

            // 출석 데이터 생성
            Attendance attendance = new Attendance();
            attendance.setUser(user);
            attendance.setAttendanceTime(now);

            // 지각 여부 확인 및 처리
            if (now.toLocalTime().isAfter(tardyTime)) {
                log.info("User {} is late.", userId);
                user.setUserTardyCount(user.getUserTardyCount() + 1); // 지각 횟수 증가
                userRepository.save(user); // 유저 데이터 저장
            } else {
                log.info("User {} is on time.", userId);
            }

            // 출석 데이터 저장
            attendanceRepository.save(attendance);

            log.info("Attendance marked successfully for user {}.", userId);
            return ResponseEntity.status(HttpStatus.OK).build(); // 200: 성공
        } catch (Exception e) {
            log.error("Error while marking attendance for user {}: {}", userId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500: 서버 오류
        }
    }



//    public MainPageInfo getMainPageInfo(String userId) {
//        try {
//            // 사용자 확인
//            User user = userRepository.findById(userId)
//                    .orElseThrow(() -> new RuntimeException("User not found"));
//
//            // 현재 학기 확인
//            Semester currentSemester = semesterRepository.findBySemesterCpId(user.getUserSemester().getSemesterCpId())
//                    .orElseThrow(() -> new RuntimeException("No active semester found"));
//
//            // 학기 지각 벌금
//            int penaltyPerTardy = currentSemester.getSemesterTardyPenalty();
//
//            // 학기의 지각 기준 시간
//            LocalTime tardyTime = currentSemester.getSemesterTardyTime();
//            if (tardyTime == null) {
//                throw new RuntimeException("Tardy time is not set for the semester.");
//            }
//
//            // 오늘 날짜 가져오기
//            LocalDate today = LocalDate.now();
//
//
//            List<String> onTimeUsers = attendanceRepository.findAll().stream()
//                    .filter(attendance -> attendance.getAttendanceTime().toLocalDate().isEqual(today) // 오늘 출석한 유저만
//                            && attendance.getAttendanceTime().toLocalTime().isBefore(tardyTime))    // 지각 시간 이전에 출석한 유저만
//                    .map(attendance -> attendance.getUser().getUserId())                           // 유저 ID 추출
//                    .toList();
//
//
//            // 전체 유저에서 시간 내 출석자를 제외한 리스트 생성
//            List<String> lateUsers = userRepository.findAll().stream()
//                    .map(User::getUserId)
//                    .filter(userIdInList -> !onTimeUsers.contains(userIdInList)) // 시간 내 출석자를 제외
//                    .map(userIdInList -> userRepository.findById(userIdInList).get().getUserName()) // 이름으로 변환
//                    .toList();
//
//            // 공지사항 리스트 가져오기
//            List<String> noticeList = noticeRepository.findByNoticeSemester_SemesterId(currentSemester.getSemesterId())
//                    .stream()
//                    .map(Notice::getNoticeContent)
//                    .collect(Collectors.toList());
//
//            return MainPageInfo.builder()
//                    .userTardyCount(user.getUserTardyCount()) // 사용자의 지각 횟수
//                    .tardyPenalty(penaltyPerTardy)           // 학기별 지각 벌금
//                    .absentList(lateUsers)                   // 출석하지 않거나 지각한 유저 리스트
//                    .noticeList(noticeList)                  // 공지사항 리스트
//                    .build();
//        } catch (Exception e) {
//            log.error("Error while fetching main page info: {}", e.getMessage());
//            throw new RuntimeException("Error fetching main page info");
//        }
//    }

    public MainPageInfo getMainPageInfo(String userId) {
        // 현재 유저 가져오기
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(ErrorCode.USER_NOT_FOUND));

        // 지각 정보 계산
        int userTardyCount = user.getUserTardyCount();
        int tardyPenalty = userTardyCount * 10000;

        // 지각자 명단
        List<String> absentList = getAbsentUsers();

        // 공지사항 가져오기
        List<String> noticeList = getRecentNotices(3);

        // 인기 게시글 가져오기 (좋아요 순)
        List<ArticleResponse> popularArticles = articleService.getPopularArticles();

        return MainPageInfo.builder()
                .userTardyCount(userTardyCount)
                .tardyPenalty(tardyPenalty)
                .absentList(absentList)
                .noticeList(noticeList)
                .popularArticleList(popularArticles)
                .build();
    }

    private List<String> getAbsentUsers() {
        return userRepository.findAbsentUsers();
    }

    private List<String> getRecentNotices(int count) {
        Pageable pageable = PageRequest.of(0, count);
        return noticeRepository.getRecentNotices(pageable);
    }



    @Scheduled(cron = "0 0 0 * * ?") // 매일 자정에 실행
    @Transactional
    public void resetAttendance() {
        log.info("Resetting attendance and updating tardy counts...");

        try {
            // 오늘 날짜 가져오기
            LocalDate today = LocalDate.now();

            // 모든 재학생 가져오기
            List<User> students = userRepository.findAllByUserIsStudentTrue();

            // 오늘 출석한 학생의 ID 가져오기
            List<String> attendedUserIds = attendanceRepository.findAll().stream()
                    .filter(attendance -> attendance.getAttendanceTime().toLocalDate().isEqual(today))
                    .map(attendance -> attendance.getUser().getUserId())
                    .collect(Collectors.toList());

            // 출석하지 않은 학생 처리
            for (User student : students) {
                if (!attendedUserIds.contains(student.getUserId())) {
                    // 지각 횟수 증가
                    student.setUserTardyCount(student.getUserTardyCount() + 1);
                    userRepository.save(student);
                    log.info("User {} did not attend. Tardy count increased.", student.getUserId());
                }
            }

            // 출석 기록 초기화
            attendanceRepository.disableSafeUpdates();
            attendanceRepository.deleteRows();
            attendanceRepository.enableSafeUpdates();

            log.info("Attendance table successfully cleared.");
        } catch (Exception e) {
            log.error("Error while resetting attendance: {}", e.getMessage());
        }
    }

}
