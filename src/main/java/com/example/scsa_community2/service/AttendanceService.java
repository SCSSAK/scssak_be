package com.example.scsa_community2.service;

import com.example.scsa_community2.dto.response.ArticleResponse;
import com.example.scsa_community2.dto.response.MainPageInfo;
import com.example.scsa_community2.entity.Attendance;
import com.example.scsa_community2.entity.Semester;
import com.example.scsa_community2.entity.User;
import com.example.scsa_community2.exception.error.BaseException;
import com.example.scsa_community2.exception.error.GlobalErrorCode;
import com.example.scsa_community2.repository.AttendanceRepository;
import com.example.scsa_community2.repository.NoticeRepository;
import com.example.scsa_community2.repository.SemesterRepository;
import com.example.scsa_community2.repository.UserRepository;
import com.example.scsa_community2.util.IpUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
import java.util.Optional;
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

    @Value("${attendance.allowed.ip-ranges}")
    private String allowedIpRanges;

    public void markAttendance(String userId, HttpServletRequest request) {
        // 클라이언트 IP 가져오기 및 검증
        String clientIp = IpUtils.getClientIp(request);
        List<String> allowedRanges = List.of(allowedIpRanges.split(","));
        if (!IpUtils.isIpAllowed(clientIp, allowedRanges)) {
            throw new BaseException(GlobalErrorCode.NOT_PRIVILEGED); // 403: 접근 권한 없음
        }

        // 유저 확인
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(GlobalErrorCode.USER_NOT_FOUND)); // 404: 유저 없음

        // 재학생 여부 확인
        if (!Boolean.TRUE.equals(user.getUserIsStudent())) {
            throw new BaseException(GlobalErrorCode.INVALID_INPUT); // 400: 잘못된 입력
        }

        // 이미 출석 여부 확인
        if (attendanceRepository.existsByUserId(userId)) {
            throw new BaseException(GlobalErrorCode.INVALID_INPUT); // 400: 잘못된 입력
        }

        // 학기 정보 및 지각 기준 시간 확인
        Semester currentSemester = semesterRepository.findBySemesterCpId(user.getUserSemester().getSemesterCpId())
                .orElseThrow(() -> new BaseException(GlobalErrorCode.SEMESTER_NOT_FOUND)); // 404: 학기 없음
        LocalTime tardyTime = Optional.ofNullable(currentSemester.getSemesterTardyTime())
                .orElseThrow(() -> new BaseException(GlobalErrorCode.INTERNAL_SERVER_ERROR)); // 500: 서버 오류

        // 출석 처리
        LocalDateTime now = LocalDateTime.now();
        Attendance attendance = new Attendance();
        attendance.setUser(user);
        attendance.setAttendanceTime(now);

        // 지각 여부 확인 및 처리
        if (now.toLocalTime().isAfter(tardyTime)) {
            user.setUserTardyCount(user.getUserTardyCount() + 1);
            userRepository.save(user);
        }

        attendanceRepository.save(attendance);
    }


    public MainPageInfo getMainPageInfo(String userId) {
        // 현재 유저 가져오기
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(GlobalErrorCode.USER_NOT_FOUND));

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
