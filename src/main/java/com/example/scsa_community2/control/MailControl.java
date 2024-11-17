package com.example.scsa_community2.control;

import com.example.scsa_community2.dto.response.MailResponse;
import com.example.scsa_community2.jwt.PrincipalDetails;
import com.example.scsa_community2.service.MailService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}

