package com.example.scsa_community2.control;

import com.example.scsa_community2.dto.request.UserLogInRequest;
import com.example.scsa_community2.dto.request.UserSignUpRequest;
import com.example.scsa_community2.dto.request.RefreshRequest;
import com.example.scsa_community2.dto.request.UserUpdateRequest;
import com.example.scsa_community2.dto.response.UserDetailResponse;
import com.example.scsa_community2.dto.response.UserLogInResponse;
import com.example.scsa_community2.entity.User;
import com.example.scsa_community2.exception.BaseException;
import com.example.scsa_community2.exception.ErrorCode;
import com.example.scsa_community2.jwt.JWTUtil;
import com.example.scsa_community2.jwt.JWTValType;
import com.example.scsa_community2.jwt.Token;
import com.example.scsa_community2.jwt.PrincipalDetails;
import com.example.scsa_community2.service.AttendanceService;
import com.example.scsa_community2.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


@Slf4j
@RestController
@RequiredArgsConstructor
//@RequestMapping("/user")
@RequestMapping("/api/v1/user")
public class UserControl {

    private final UserService userService;
    private final AttendanceService attendanceService;
    private final JWTUtil jwtUtil;

    // 직접 db에 유저 정보 넣기 위한 controller
    @PostMapping("/signUp")
    @Operation(description = "유저를 등록 한다")
    public ResponseEntity<?> SignUp(@RequestBody UserSignUpRequest userSingUpRequest) {

        userService.saveUser(userSingUpRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body("유저 등록이 완료되었습니다.");
    }

    @PostMapping("/login")
    @Operation(description = "로그인")
    public ResponseEntity<?> LogIn(@RequestBody UserLogInRequest userRequest) {

        UserLogInResponse userLogInResponseDto = userService.logIn(userRequest);

        return ResponseEntity.status(HttpStatus.OK)
                .body(userLogInResponseDto);
    }

    @PostMapping("/refresh")
    @Operation(description = "리프레시 토큰으로 엑세스 토큰을 재발급 받는다.")
    public ResponseEntity<?> refresh(@RequestBody RefreshRequest refreshToken) {

        // 유효한 토큰이라면
        if (jwtUtil.validateToken(refreshToken.getRefreshToken()) == JWTValType.VALID_JWT) {
            Token token = userService.refresh(refreshToken.getRefreshToken());

            return ResponseEntity.status(HttpStatus.OK)
                    .body(token.getRefreshToken()); // body에는 새로 발급한 access Token 반환
        }
        throw new BaseException(ErrorCode.INVALID_TOKEN);
    }

    @GetMapping("/profile/{user-id}")
    @Operation(description = "유저 정보를 반환한다.")
    public ResponseEntity<UserDetailResponse> getUserInfo(@PathVariable("user-id") String userId,
                                                          @AuthenticationPrincipal PrincipalDetails userDetails) {

        if (userDetails == null || userDetails.getUser() == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "인증되지 않은 사용자입니다.");
        }

        // Assuming `userService` provides a method to retrieve user details by `userId`
        User user = userService.getUser(userId);

        // Convert the User entity to UserDetailResponse DTO
        UserDetailResponse userDetailResponse = UserDetailResponse.builder()
                .userName(user.getUserName())
                .userSemester(user.getUserSemester().getSemesterId())
                .userCompany(user.getUserCompany())
                .userDepartment(user.getUserDepartment())
                .userPosition(user.getUserPosition())
                .userEmail(user.getUserEmail())
                .userSns(user.getUserSns())
                .userMessage(user.getUserMessage())
                .userImg(user.getUserImg())
                .build();

        return ResponseEntity.status(HttpStatus.OK)
                .body(userDetailResponse);
    }


    @PostMapping("/logout")
    @Operation(description = "로그 아웃")
    public ResponseEntity<?> signOut(@AuthenticationPrincipal PrincipalDetails userDetails) {

        // 액세스 토큰이 있다면
        if (userDetails != null) {
            String userId = userDetails.getUser().getUserId();
            userService.signOut(userId);
        }

        // 쿠키 만료 시키기
        String cookie = userService.setHttpOnlyCookieInvalidate("refreshToken");

        return ResponseEntity.ok()
                .header("Set-Cookie", cookie)
                .build();
    }

    @PutMapping("/profile")
    @Operation(description = "유저 페이지 수정")
    public ResponseEntity<Void> updateUserProfile(@ModelAttribute UserUpdateRequest userUpdateRequest,
                                                  @AuthenticationPrincipal PrincipalDetails userDetails) {
        if (userDetails == null || userDetails.getUser() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        log.info("Received user update request: {}", userUpdateRequest);
        String userId = userDetails.getUser().getUserId();

        return userService.updateUserProfile(userId, userUpdateRequest);
    }

    @PostMapping("/attend") // 명세서에 따른 출석 API 경로
    @Operation(description = "Marks the user's attendance for the day.")
    public ResponseEntity<Void> markAttendance(@AuthenticationPrincipal PrincipalDetails userDetails) {
        if (userDetails == null || userDetails.getUser() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 401: 인증되지 않은 사용자
        }

        String userId = userDetails.getUser().getUserId();
        return attendanceService.markAttendance(userId); // AttendanceService 호출
    }



}
