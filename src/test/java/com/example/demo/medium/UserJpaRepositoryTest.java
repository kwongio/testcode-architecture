package com.example.demo.medium;

import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.infrastructure.UserEntity;
import com.example.demo.user.infrastructure.UserJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@SqlGroup({
        @Sql(value = "/sql/user-repository-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
class UserJpaRepositoryTest {


    @Autowired
    private UserJpaRepository userRepository;


    @DisplayName("findByIdAndStatus로 유저를 조회할 수 있다.")
    @Test
    public void test5() throws IOException {
        //given
        //when
        Optional<UserEntity> result = userRepository.findByIdAndStatus(1, UserStatus.ACTIVE);

        //then
        assertThat(result.isPresent()).isTrue();

    }

    @DisplayName("findByIdAndStatus는 데이터가 없으면 Optional Emtpy를 반환한다.")
    @Test
    public void test6() throws IOException {
        //given
        //when
        Optional<UserEntity> result = userRepository.findByIdAndStatus(1, UserStatus.PENDING);

        //then
        assertThat(result.isEmpty()).isTrue();

    }

    @DisplayName("findByIdAndStatus로 유저를 조회할 수 있다.")
    @Test
    public void test2() throws IOException {
        //given
        //when
        Optional<UserEntity> result = userRepository.findByEmailAndStatus("kwon@naver.com", UserStatus.ACTIVE);

        //then
        assertThat(result.isPresent()).isTrue();

    }

    @DisplayName("findByIdAndStatus는 데이터가 없으면 Optional Emtpy를 반환한다.")
    @Test
    public void test3() throws IOException {
        //given

        //when
        Optional<UserEntity> result = userRepository.findByEmailAndStatus("kwon@naver.com", UserStatus.PENDING);

        //then
        assertThat(result.isEmpty()).isTrue();

    }

}