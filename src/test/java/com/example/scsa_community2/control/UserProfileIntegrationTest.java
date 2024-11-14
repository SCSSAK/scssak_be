package com.example.scsa_community2;

import com.example.scsa_community2.dto.request.UserLogInRequest;
import com.example.scsa_community2.dto.response.UserLogInResponse;
import com.example.scsa_community2.entity.User;
import com.example.scsa_community2.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserProfileIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        // 테스트용 유저 등록
        userRepository.save(User.builder()
                .userId("testUser")
                .userPwd(passwordEncoder.encode("password123"))
                .userName("홍길동")
                .userIsStudent(true)
                .userCompany("삼성전자")
                .userDepartment("컴퓨터공학")
                .userPosition("교육생")
                .userEmail("testuser@example.com")
                .userSemester(3)
                .userMessage("자기소개")
                .userSns("snsProfile")
                .userImg("https://www.example.com/profile.jpg")
                .build());
    }

    @Test
    public void testLoginAndFetchUserProfile() throws Exception {
        // 로그인 요청 데이터 생성
        UserLogInRequest loginRequest = new UserLogInRequest("testUser", "password123");

        // 로그인 API 호출
        ResultActions loginResult = mockMvc.perform(post("/api/v1/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk());

        // 로그인 응답에서 accessToken 추출
        String accessToken = loginResult.andReturn().getResponse().getContentAsString();
        UserLogInResponse responseDTO = objectMapper.readValue(accessToken, UserLogInResponse.class);

        // 프로필 조회 API 호출
        mockMvc.perform(get("/api/v1/user/profile/testUser")
                        .header("Authorization", "Bearer " + responseDTO.getAccessToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user_name", is("홍길동")))
                .andExpect(jsonPath("$.user_email", is("testuser@example.com")))
                .andExpect(jsonPath("$.user_sns", is("snsProfile")))
                .andExpect(jsonPath("$.user_img", is("https://www.example.com/profile.jpg")));
    }
}
