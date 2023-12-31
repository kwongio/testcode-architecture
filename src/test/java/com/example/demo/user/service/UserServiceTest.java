package com.example.demo.user.service;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.mock.FakeClockHolder;
import com.example.demo.mock.FakeMailSender;
import com.example.demo.mock.FakeUserRepository;
import com.example.demo.mock.FakeUuidHolder;
import com.example.demo.stub.UserStub;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserCreate;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.UserUpdate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static com.example.demo.stub.UserStub.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


class UserServiceTest {


    private UserService userService;

    @BeforeEach
    void init() {
        FakeUserRepository userRepository = new FakeUserRepository();

        this.userService = UserService.builder()
                .userRepository(userRepository)
                .certificationService(new CertificationService(new FakeMailSender()))
                .uuidHolder(new FakeUuidHolder(CERTIFICATION_CODE))
                .clockHolder(new FakeClockHolder(100L))
                .build();
        userRepository.save(UserStub.pending());
        userRepository.save(UserStub.active());
    }

    @DisplayName("getByEmail은 ACTIVE 상태인 유저를 찾아올 수 있다.")
    @Test
    void test2() {
        String email = EMAIL;
        User result = userService.getByEmail(email);
        assertThat(result.getNickname()).isEqualTo(NICKNAME);
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
        assertThat(result.getNickname()).isEqualTo(NICKNAME);
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
        User result = userService.create(userCreateDto);

        assertThat(result.getId()).isNotNull();
        assertThat(result.getStatus()).isEqualTo(UserStatus.PENDING);
        assertThat(result.getCertificationCode()).isEqualTo(CERTIFICATION_CODE);
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