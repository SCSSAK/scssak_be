package com.example.scsa_community2.service;

import com.example.scsa_community2.dto.request.UserLogInRequest;
import com.example.scsa_community2.dto.request.UserSignUpRequest;
import com.example.scsa_community2.dto.response.UserLogInResponse;
import com.example.scsa_community2.dto.response.UserResponse;
import com.example.scsa_community2.jwt.Token;
import com.example.scsa_community2.jwt.JWTUtil;
import com.example.scsa_community2.entity.User;
import com.example.scsa_community2.exception.BaseException;
import com.example.scsa_community2.exception.ErrorCode;
import com.example.scsa_community2.jwt.UserAuthentication;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.scsa_community2.repository.UserRepository;

import static com.example.scsa_community2.jwt.JWTValType.VALID_JWT;

@RequiredArgsConstructor
@Service
public class UserService {

    private static final int ACCESS_TOKEN_EXPIRATION = 3 * 24 * 60 * 60 * 1000; // 3일
    private static final int REFRESH_TOKEN_EXPIRATION = 1209600000; // 2주

    private final UserRepository userRepository;
    private final JWTUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    // 유저 정보 db에 저장하는 메서드
    public User saveUser(UserSignUpRequest userData) {

//        String encryptedPwd = userData.getUserPwd(); // to-do : userData로 받아온 password를 암호화 하는 작업
        // 비밀번호를 BCrypt로 암호화
        String encryptedPwd = passwordEncoder.encode(userData.getUserPwd());

        User user = User.builder()
                .userId(userData.getUserId())
                .userPwd(encryptedPwd)
                .userName(userData.getUserName())
                .userIsStudent(userData.getUserIsStudent())
                .userCompany(userData.getUserCompany())
                .userDepartment(userData.getUserDepartment())
                .userPosition(userData.getUserPosition())
//                .userJob(userData.getUserJob())
                .userEmail(userData.getUserEmail())
                .userSemester(userData.getUserSemester())
                .userMessage(userData.getUserMessage())
                .userImg(userData.getUserImg())
                .userSns(userData.getUserSns())
                .userIsCp(userData.getUserIsCp())
                .userTardyCount(userData.getUserTardyCount())
                .build();
        return userRepository.save(user);
    }

    @Transactional
    public UserLogInResponse logIn(UserLogInRequest userLogInReq) {
        // 유저 찾기
        User user = getUser(userLogInReq.getUserId());

        // jwt 토큰 생성
        Token token = getToken(user);

        return UserLogInResponse.of(token, UserResponse.from(user));
    }

    // 유저 정보 찾는 메서드
    private User getUser(String userId) {
        return userRepository.findById(userId).orElseThrow(() -> new BaseException(ErrorCode.USER_NOT_FOUND));
    }

    // 사용자 정보를 통해  refreshToken을 User에 저장하고 jwt Token을 반환
    private Token getToken(User user) {
        Token token = generateToken(new UserAuthentication(user.getUserId(), null, null)); // jwt 토큰 생성
        user.updateRefreshToken(token.getRefreshToken()); // 유저 정보에 refresh 토큰 저장
        return token; // access, refresh 토큰 반환
    }


    // 토큰 객체 생성
    private Token generateToken(Authentication authentication) {
        return Token.builder()
                .accessToken(jwtUtil.generateToken(authentication, ACCESS_TOKEN_EXPIRATION)) // 액세스 토큰 생성
                .refreshToken(jwtUtil.generateToken(authentication, REFRESH_TOKEN_EXPIRATION)) // 리프레시 토큰 생성
                .build();
    }


    // access token으로 access token, refresh token 재발급하는 메소드
    @Transactional
    public Token refresh(String refreshToken) {

        String userId = jwtUtil.getUserFromJwt(refreshToken);; //  유저 id 추출
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(ErrorCode.UNAUTHORIZED)); //유저 정보 추출
        String realRefreshToken = user.getRefreshToken(); // 저장된 refreshToken 가지고 오기

        // 저장된 리프레시 토큰이 유효하지 않다면 401 에러
        if (realRefreshToken == null ||!jwtUtil.validateToken(realRefreshToken).equals(VALID_JWT)) {
            throw new BaseException(ErrorCode.INVALID_TOKEN);
        }

        // 저장된 리프레시 토큰의 유효성 검증
        if (!jwtUtil.validateToken(realRefreshToken).equals(VALID_JWT)) {
            throw new BaseException(ErrorCode.INVALID_TOKEN);
        }
        // access Token 이 유효하면 엑세스 토큰, 리프레시 토큰 새로 생성 해서 반환
        return getToken(user); // Token 재생성 및 user 리프레시 토큰 컬럼에 저장한다.
    }

    //리프레시 토큰 담긴 쿠키 만료 시키기
    public String setHttpOnlyCookieInvalidate(String cookieName) {

        ResponseCookie cookie = ResponseCookie.from(cookieName, null)
                .path("/")
                .sameSite("None")
                .httpOnly(true)
                .secure(true)
                .maxAge(0) // 쿠키 바로 만료
                .build();

        return cookie.toString();
    }

    // 로그아웃
    @Transactional
    public void signOut(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(ErrorCode.USER_NOT_FOUND));
        user.resetRefreshToken();
    }

}
