package com.example.demo.user.controller;

import com.example.demo.mock.TestContainer;
import com.example.demo.stub.UserStub;
import com.example.demo.user.controller.response.UserResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static com.example.demo.stub.UserStub.EMAIL;
import static com.example.demo.stub.UserStub.NICKNAME;
import static org.assertj.core.api.Assertions.assertThat;


class UserControllerTest {


    @DisplayName("사용자는 특정 유저의 정보를 개인정보는 소거된 채 전달받을 수 있다")
    @Test
    public void test1() throws Exception {
        TestContainer testContainer = TestContainer.builder()
                .build();
        testContainer.userRepository.save(UserStub.active());

        ResponseEntity<UserResponse> result = UserController.builder()
                .userService(testContainer.userService)
                .build()
                .getUserById(1L);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getEmail()).isEqualTo(EMAIL);
        assertThat(result.getBody().getNickname()).isEqualTo(NICKNAME);

    }

    @DisplayName("존재하지 않는 유저의 아이디로 api 호출할 경우 404 응답을 받는다.")
    @Test
    public void test3() throws Exception {


    }

    @DisplayName("사용자는 인증 코드로 계정을 활성화 시킬 수 있다.")
    @Test
    public void test4() throws Exception {

    }

    @DisplayName("사용자는 내 정보를 불러올 때 개인정보 주소도 갖고 올 수 있다. ")
    @Test
    public void test5() throws Exception {


    }


    @DisplayName("사용자는 email로 인가처리 후 닉네임와 주소만 바꿀 수 있다.")
    @Test
    public void test6() throws Exception {

    }
}