package com.example.demo.user.domain;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.mock.FakeClockHolder;
import com.example.demo.mock.FakeUuidHolder;
import com.example.demo.stub.UserStub;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static com.example.demo.stub.UserStub.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserTest {

    @DisplayName("User는 UserCreate 객체로 생성할 수 있다.")
    @Test
    public void create() throws IOException {

        //given
        UserCreate userCreateDto = UserCreate.builder()
                .email("rldh9037@naver.com")
                .nickname("kwon3")
                .address("Seoul")
                .build();


        //when
        User result = User.from(userCreateDto, new FakeUuidHolder("aaaa-bbbb-cccc"));

        //then
        Assertions.assertThat(result.getEmail()).isEqualTo("rldh9037@naver.com");
        Assertions.assertThat(result.getNickname()).isEqualTo("kwon3");
        Assertions.assertThat(result.getAddress()).isEqualTo("Seoul");
        Assertions.assertThat(result.getStatus()).isEqualTo(UserStatus.PENDING);
        Assertions.assertThat(result.getCertificationCode()).isEqualTo("aaaa-bbbb-cccc");

    }

    @DisplayName("User는 UserUpdate 객체로 수정할 수 있다.")
    @Test
    public void update() throws IOException {
        User user = UserStub.active();

        //given
        UserUpdate userUpdateDto = UserUpdate.builder()
                .address("hi")
                .nickname("ho")
                .build();
        //when
        user = user.update(userUpdateDto);


        //then
        Assertions.assertThat(user.getNickname()).isEqualTo("ho");
        Assertions.assertThat(user.getAddress()).isEqualTo("hi");
        Assertions.assertThat(user.getEmail()).isEqualTo(EMAIL);
        Assertions.assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVE);
        Assertions.assertThat(user.getCertificationCode()).isEqualTo("aaaa-bbbb-cccc");


    }


    @DisplayName("User는 로그인을 할 수 있고 로그인시 마지막 로그인 시간이 변경된다.")
    @Test
    public void login() throws IOException {

        //given
        User user = UserStub.active();

        //when
        user = user.login(new FakeClockHolder(100));

        //then
        Assertions.assertThat(user.getNickname()).isEqualTo(NICKNAME);
        Assertions.assertThat(user.getAddress()).isEqualTo(ADDRESS);
        Assertions.assertThat(user.getEmail()).isEqualTo(EMAIL);
        Assertions.assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVE);
        Assertions.assertThat(user.getCertificationCode()).isEqualTo("aaaa-bbbb-cccc");
        Assertions.assertThat(user.getLastLoginAt()).isEqualTo(100);
    }


    @DisplayName("User는 잘못된 인증 코드로 계정을 활성화하려면 에러를 던진다.")
    @Test
    public void verifyFail()  {
        //given
        User user = UserStub.pending();

        //then
        assertThatThrownBy(() -> user.certificate("not-valid")).isInstanceOf(CertificationCodeNotMatchedException.class);
    }


    @DisplayName("User는 유효한 인증 코드로 계정을 활성화 할 수 있다.")
    @Test
    public void verfiy() throws IOException {
        //given
        User user = UserStub.pending();

        //when
        user = user.certificate(CERTIFICATION_CODE);

        //then
        Assertions.assertThat(user.getNickname()).isEqualTo(NICKNAME);
        Assertions.assertThat(user.getAddress()).isEqualTo(ADDRESS);
        Assertions.assertThat(user.getEmail()).isEqualTo("rldh90372@naver.com");
        Assertions.assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVE);
        Assertions.assertThat(user.getCertificationCode()).isEqualTo("aaaa-bbbb-cccc");
        Assertions.assertThat(user.getLastLoginAt()).isEqualTo(0);

    }

}