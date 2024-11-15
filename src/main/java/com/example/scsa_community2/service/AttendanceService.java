package com.example.scsa_community2.service;

import com.example.scsa_community2.entity.User;
import com.example.scsa_community2.exception.BaseException;
import com.example.scsa_community2.exception.ErrorCode;
import com.example.scsa_community2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private static final Logger logger = LoggerFactory.getLogger(AttendanceService.class);
    private static final LocalTime TARDY_TIME = LocalTime.of(8, 55);

    private final UserRepository userRepository;

    @Transactional
    public ResponseEntity<Void> checkAttendance(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(ErrorCode.USER_NOT_FOUND));

        if (!user.getUserIsStudent()) {
            logger.info("Attendance check failed: User is not a student - userId: {}", userId);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        LocalTime currentTime = LocalTime.now();

        if (user.getUserTardyCount() != null && currentTime.isAfter(TARDY_TIME)) {
            logger.info("Attendance check failed: User is tardy - userId: {}", userId);
            user.setUserTardyCount(user.getUserTardyCount() + 1);
            userRepository.save(user);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        logger.info("Attendance check succeeded for userId: {}", userId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
