package com.example.demo.user.controller;

import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.UserUpdate;
import com.example.demo.user.infrastructure.UserEntity;
import com.example.demo.user.infrastructure.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@SqlGroup({
        @Sql(value = "/sql/user-controller-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;

    @DisplayName("사용자는 특정 유저의 정보를 개인정보는 소거된 채 전달받을 수 있다")
    @Test
    public void test1() throws Exception {
        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("kwon@naver.com"))
                .andExpect(jsonPath("$.nickname").value("kwon"))
                .andExpect(jsonPath("$.address").doesNotExist())
                .andExpect(jsonPath("$.status").value(UserStatus.ACTIVE.toString()));
    }

    @DisplayName("존재하지 않는 유저의 아이디로 api 호출할 경우 404 응답을 받는다.")
    @Test
    public void test3() throws Exception {
        mockMvc.perform(get("/api/users/112351235"))
                .andExpect(status().isNotFound());

    }

    @DisplayName("사용자는 인증 코드로 계정을 활성화 시킬 수 있다.")
    @Test
    public void test4() throws Exception {
        mockMvc.perform(get("/api/users/2/verify").queryParam("certificationCode", "aaaa-bbbb-cccc"))
                .andExpect(status().isFound());
        UserEntity result = userRepository.findById(2L).get();
        assertThat(result.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @DisplayName("사용자는 내 정보를 불러올 때 개인정보 주소도 갖고 올 수 있다. ")
    @Test
    public void test5() throws Exception {
        mockMvc.perform(get("/api/users/me").header("EMAIL", "kwon@naver.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("kwon@naver.com"))
                .andExpect(jsonPath("$.nickname").value("kwon"))
                .andExpect(jsonPath("$.address").value("Seoul"))
                .andExpect(jsonPath("$.status").value(UserStatus.ACTIVE.toString()));

    }


    @DisplayName("사용자는 email로 인가처리 후 닉네임와 주소만 바꿀 수 있다.")
    @Test
    public void test6() throws Exception {
        UserUpdate userUpdateDto = UserUpdate.builder()
                .address("hi")
                .nickname("ho")
                .build();
        mockMvc.perform(put("/api/users/me")
                        .header("EMAIL", "kwon@naver.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userUpdateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("kwon@naver.com"))
                .andExpect(jsonPath("$.nickname").value("ho"))
                .andExpect(jsonPath("$.address").value("hi"))
                .andExpect(jsonPath("$.status").value(UserStatus.ACTIVE.toString()));

    }
}