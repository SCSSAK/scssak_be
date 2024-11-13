package com.example.scsa_community2.jwt;

import com.example.scsa_community2.entity.User;
import com.example.scsa_community2.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// JWT 인증 필터로 요청에 JWT를 검증하는 클래스
@Slf4j
@Component
@RequiredArgsConstructor
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;
    private final JWTUtil jwtUtil;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {

            String token = tokenProvider.getAccessTokenFromRequest(request);
            if (StringUtils.hasText(token) && jwtUtil.validateToken(token) == JWTValType.VALID_JWT) {
                String userId = jwtUtil.getUserFromJwt(token);
                User user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

                PrincipalDetails principalDetails = new PrincipalDetails(user);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        principalDetails, null, principalDetails.getAuthorities()
                );
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception exception) {
            log.error("Exception during JWT processing: ", exception);
            log.error(exception.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    // 사용자 아이디 추출
    private String getUserId(String token) {
        return jwtUtil.getUserFromJwt(token);
    }

}