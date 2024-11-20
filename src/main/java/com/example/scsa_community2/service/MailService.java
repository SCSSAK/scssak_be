package com.example.scsa_community2.service;

import com.example.scsa_community2.dto.request.MailRequest;
import com.example.scsa_community2.dto.response.MailListResponse;
import com.example.scsa_community2.dto.response.MailResponse;
import com.example.scsa_community2.dto.response.UserMailInfo;
import com.example.scsa_community2.entity.Mail;
import com.example.scsa_community2.entity.User;
import com.example.scsa_community2.exception.custom.EntityNotFoundException;
import com.example.scsa_community2.exception.custom.UnauthorizedAccessException;
import com.example.scsa_community2.exception.error.BaseException;
import com.example.scsa_community2.exception.error.GlobalErrorCode;
import com.example.scsa_community2.repository.MailRepository;
import com.example.scsa_community2.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
//        List<User> usersInSemester = userRepository.findUsersBySemesterId(semester);
        List<User> usersInSemester = userRepository.findUsersBySemesterIdSortedByName(semester);


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

    public void sendMail(String senderId, MailRequest mailRequest) {
        // 메일 내용 확인
        if (mailRequest.getMailContent() == null || mailRequest.getMailContent().trim().isEmpty()) {
            throw new IllegalArgumentException("Mail content cannot be empty");
        }

        // 송신자 확인
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new EntityNotFoundException("Sender not found"));

        // 수신자 확인
        User receiver = userRepository.findById(mailRequest.getReceiverId())
                .orElseThrow(() -> new EntityNotFoundException("Receiver not found"));

        // 같은 학기인지 확인
        if (!sender.getUserSemester().getSemesterId().equals(receiver.getUserSemester().getSemesterId())) {
            throw new UnauthorizedAccessException("Receiver is in a different semester");
        }

        // 메일 저장
        Mail mail = new Mail();
        mail.setSender(sender);
        mail.setReceiver(receiver);
        mail.setMailContent(mailRequest.getMailContent());
        mail.setMailCreatedAt(LocalDateTime.now());

        mailRepository.save(mail);
    }


    public MailListResponse getMailList(String requestorId, String receiverId, int currentPage) {
        // 요청자와 수신자 확인
        User requestor = userRepository.findById(requestorId)
                .orElseThrow(() -> new BaseException(GlobalErrorCode.NOT_FOUND_DATA, "Requestor not found"));

        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new BaseException(GlobalErrorCode.NOT_FOUND_DATA, "Receiver not found"));

        // 같은 학기인지 확인
        if (!requestor.getUserSemester().getSemesterId().equals(receiver.getUserSemester().getSemesterId())) {
            throw new BaseException(GlobalErrorCode.UNAUTHORIZED, "Receiver is in a different semester");
        }

        // 페이지네이션 설정
        Pageable pageable = PageRequest.of(currentPage - 1, 10, Sort.by(Sort.Direction.DESC, "mailCreatedAt"));

        // 페이지네이션된 메일 목록 조회
        Page<Mail> mailPage = mailRepository.findByReceiver_UserIdOrderByMailCreatedAtDesc(receiverId, pageable);

        // DTO 변환
        List<MailListResponse.MailDetail> mailDetails = mailPage.getContent().stream()
                .map(mail -> new MailListResponse.MailDetail(
                        mail.getMailId(),
                        mail.getMailContent(),
                        mail.getMailCreatedAt().toLocalDate().toString(), // 날짜 형식 변환
                        mail.getSender().getUserId()
                ))
                .collect(Collectors.toList());
        int totalPage = Math.max(1, mailPage.getTotalPages());
        // 응답 생성
        return new MailListResponse(
                receiver.getUserName(),
                mailDetails,
                totalPage,
                currentPage
        );
    }

    public void deleteMail(Long mailId, String senderId) {
        // 메일 확인
        Mail mail = mailRepository.findById(mailId)
                .orElseThrow(() -> new EntityNotFoundException("Mail not found"));

        // 송신자 확인
        if (!mail.getSender().getUserId().equals(senderId)) {
            throw new UnauthorizedAccessException("You are not the sender of this mail");
        }

        // 메일 삭제
        mailRepository.delete(mail);
    }
}

