package com.example.demo.service;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.UserEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@SpringBootTest
@SqlGroup({
        @Sql(value = "/sql/user-repository-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
class UserServiceTest {

    @Autowired
    private UserService userService;


    @DisplayName("getByEmail은 ACTIVE 상태인 유저를 찾아올 수 있다.")
    @Test
    void test2() {
        String email = "kwon@naver.com";
        UserEntity result = userService.getByEmail(email);
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
        String email = "kwon@naver.com";
        UserEntity result = userService.getById(1L);
        assertThat(result.getNickname()).isEqualTo("kwon");
    }

    @DisplayName("getById은 PENDING 상태인 유저를 찾아올 수 없다.")
    @Test
    void test4() {
        String email = "kwon1@naver.com";
        assertThatThrownBy(() -> userService.getById(2L))
                .isInstanceOf(ResourceNotFoundException.class);
    }


}