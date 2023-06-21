package com.example.demo.user.service;

import com.example.demo.mock.FakeMailSender;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class CertificationServiceTest {

    @DisplayName("이멩ㄹ과 컨텐츠가 제대로 만들어져서 보내지는지 테스트 한다.")
    @Test
    public void test1() throws IOException {
        //given
        FakeMailSender mailSender = new FakeMailSender();
        CertificationService certificationService = new CertificationService(mailSender);
        //when
        certificationService.send("rldh9037@naver.com", 1, "aaaa-bbbb-cccc" );

        //then
        assertThat(mailSender.email).isEqualTo("rldh9037@naver.com");
        assertThat(mailSender.title).isEqualTo("Please certify your email address");
        assertThat(mailSender.content).isEqualTo("Please click the following link to certify your email address: " +"http://localhost:8080/api/users/1/verify?certificationCode=aaaa-bbbb-cccc");

    }

}