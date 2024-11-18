package com.example.scsa_community2.control;

import com.example.scsa_community2.dto.request.MailRequest;
import com.example.scsa_community2.dto.response.MailListResponse;
import com.example.scsa_community2.dto.response.MailResponse;
import com.example.scsa_community2.exception.EntityNotFoundException;
import com.example.scsa_community2.exception.UnauthorizedAccessException;
import com.example.scsa_community2.jwt.PrincipalDetails;
import com.example.scsa_community2.service.MailService;
import io.swagger.v3.oas.annotations.Operation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/mail")
@RequiredArgsConstructor
public class MailControl {

    private final MailService mailService;

    @GetMapping
    @Operation(description = "Retrieves the list of users in the same semester and checks if they have new mail.")
    public ResponseEntity<?> getMailInfo(@AuthenticationPrincipal PrincipalDetails userDetails) {
        if (userDetails == null || userDetails.getUser() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 401
        }

        String userId = userDetails.getUser().getUserId();
        MailResponse mailResponse = mailService.getMailInfo(userId);

        return ResponseEntity.ok(mailResponse);
    }

    @PostMapping
    @Operation(description = "Creates a new mail.")
    public ResponseEntity<Void> sendMail(
            @AuthenticationPrincipal PrincipalDetails userDetails,
            @RequestBody MailRequest mailRequest) {
        if (userDetails == null || userDetails.getUser() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 401
        }

        String senderId = userDetails.getUser().getUserId();
        try {
            mailService.sendMail(senderId, mailRequest);
            return ResponseEntity.ok().build(); // 200
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // 400
        } catch (UnauthorizedAccessException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 401
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500
        }
    }

    @GetMapping("/{user_id}")
    @Operation(description = "Retrieves the list of mails received by a specific user.")
//    public ResponseEntity<MailListResponse> getMailList(
//            @PathVariable("user_id") String userId,
//            @AuthenticationPrincipal PrincipalDetails userDetails)
    public ResponseEntity<?> getMailList(@PathVariable("user_id") String userId, @AuthenticationPrincipal PrincipalDetails userDetails) {
        if (userDetails == null || userDetails.getUser() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 401
        }
        String requestorId = userDetails.getUser().getUserId();
        try {
            MailListResponse response = mailService.getMailList(requestorId, userId);
            List<MailListResponse.MailDetail> mailList = response.getMailList();
//            return ResponseEntity.ok(response); // 200
            return ResponseEntity.ok(mailList);
        } catch (UnauthorizedAccessException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 401
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500
        }
    }

    @DeleteMapping("/{mail_id}")
    @Operation(description = "Deletes a mail if the requester is the sender.")
    public ResponseEntity<Void> deleteMail(
            @PathVariable("mail_id") Long mailId,
            @AuthenticationPrincipal PrincipalDetails userDetails) {

        if (userDetails == null || userDetails.getUser() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 401
        }

        String senderId = userDetails.getUser().getUserId();
        try {
            mailService.deleteMail(mailId, senderId);
            return ResponseEntity.ok().build(); // 200
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404
        } catch (UnauthorizedAccessException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 401
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500
        }
    }

}

