package com.example.demo.user.service;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.user.domain.UserCreate;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.UserUpdate;
import com.example.demo.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@SpringBootTest
@SqlGroup({
        @Sql(value = "/sql/user-service-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
class UserServiceTest {

    @Autowired
    private UserService userService;
    @MockBean
    private JavaMailSender javaMailSender;



    @DisplayName("getByEmail은 ACTIVE 상태인 유저를 찾아올 수 있다.")
    @Test
    void test2() {
        String email = "kwon@naver.com";
        User result = userService.getByEmail(email);
        assertThat(result.getNickname()).isEqualTo("kwon");
    }

    @DisplayName("getByEmail은 PENDING 상태인 유저를 찾아올 수 없다.")
    @Test
    void test1() {
        String email = "kwon1@naver.com";
        assertThatThrownBy(() -> userService.getByEmail(email))
                .isInstanceOf(ResourceNotFoundException.class);
    }


    @DisplayName("getById은 ACTIVE 상태인 유저를 찾아올 수 있다.")
    @Test
    void test3() {
        User result = userService.getById(1L);
        assertThat(result.getNickname()).isEqualTo("kwon");
    }

    @DisplayName("getById은 PENDING 상태인 유저를 찾아올 수 없다.")
    @Test
    void test4() {
        assertThatThrownBy(() -> userService.getById(2L))
                .isInstanceOf(ResourceNotFoundException.class);
    }


    @DisplayName("UserCreateDto로 User를 생성할 수 있다")
    @Test
    void test5() {
        UserCreate userCreateDto = UserCreate.builder()
                .email("rldh9037@naver.com")
                .nickname("kwon3")
                .address("Seoul")
                .build();
        BDDMockito.doNothing().when(javaMailSender).send(ArgumentMatchers.any(SimpleMailMessage.class));
        User result = userService.create(userCreateDto);

        assertThat(result.getId()).isNotNull();
        assertThat(result.getStatus()).isEqualTo(UserStatus.PENDING);
//        assertThat(result.getCertificationCode()).isEqualTo(UserStatus.PENDING);
    }


    @DisplayName("userUpdateDto를 이용하여 유저를 수정할 수 있다.")
    @Test
    void test6() {
        UserUpdate userUpdateDto = UserUpdate.builder()
                .nickname("gyeonggi")
                .address("kok2")
                .build();
        userService.update(1L, userUpdateDto);

        User result = userService.getById(1L);

        assertThat(result.getNickname()).isEqualTo("gyeonggi");
        assertThat(result.getAddress()).isEqualTo("kok2");
    }

    @DisplayName("userUpdateDto를 이용하여 유저를 수정할 수 있다.")
    @Test
    void test7() {
        userService.login(1L);
        User result = userService.getById(1L);
        assertThat(result.getLastLoginAt()).isGreaterThan(0);
    }

    @DisplayName("Pending 상태의 사용자는 인증 코드로 active 시킬 수 있다.")
    @Test
    public void test8() throws IOException {
        //given


        //when
        userService.verifyEmail(2L, "aaaa-bbbb-cccc");
        //then
        User result = userService.getById(2L);
        assertThat(result.getStatus()).isEqualTo(UserStatus.ACTIVE);

    }


    @DisplayName("PENDING 상태의 사용자는 잘못된 인증코드를 받으면 에러를 던진다.")
    @Test
    public void test9() throws IOException {
        //given
        //when
        assertThatThrownBy(() -> userService.verifyEmail(2L, "aaaa-bbbb-cccddc"))
                .isInstanceOf(CertificationCodeNotMatchedException.class);

        //then

    }
}