package com.example.demo.user.controller;

import com.example.demo.user.domain.UserCreate;
import com.example.demo.user.infrastructure.UserJpaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
@SqlGroup({
        @Sql(value = "/sql/user-controller-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
class UserCreateControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserJpaRepository userRepository;
    @MockBean
    private JavaMailSender javaMailSender;


    @DisplayName("UserCreateDto로 유저를 생성할 수 있고 회원가입된 사용자는 PENDING 상태이다.")
    @Test
    public void test1() throws Exception {
        UserCreate userCreateDto = UserCreate.builder()
                .address("hihi")
                .email("gio1234@naver.com")
                .nickname("g")
                .build();
        BDDMockito.doNothing().when(javaMailSender).send(ArgumentMatchers.any(SimpleMailMessage.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/users")
                        .content(objectMapper.writeValueAsString(userCreateDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("gio1234@naver.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.nickname").value("g"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("PENDING"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.address").doesNotExist());

    }

}