package com.example.scsa_community2.service;

import com.example.scsa_community2.dto.response.MailResponse;
import com.example.scsa_community2.dto.response.UserMailInfo;
import com.example.scsa_community2.entity.User;
import com.example.scsa_community2.repository.MailRepository;
import com.example.scsa_community2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MailService {

    private final UserRepository userRepository;
    private final MailRepository mailRepository;

    public MailResponse getMailInfo(String userId) {
        // 현재 사용자 확인
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 같은 학기 사용자 조회
        Integer semester = user.getUserSemester().getSemesterId();
        List<User> usersInSemester = userRepository.findUsersBySemesterId(semester);

        // 기준 시간 (현재 - 24시간)
        LocalDateTime sinceTime = LocalDateTime.now().minusDays(1);

        // 사용자별 새 메일 여부 확인
        List<UserMailInfo> userMailInfos = usersInSemester.stream()
                .map(u -> {
                    boolean hasNewMail = mailRepository.hasNewMail(u.getUserId(), sinceTime);
                    return new UserMailInfo(u.getUserId(), u.getUserName(), hasNewMail);
                })
                .collect(Collectors.toList());

        // 응답 생성
        return new MailResponse(semester, userMailInfos);
    }
}

