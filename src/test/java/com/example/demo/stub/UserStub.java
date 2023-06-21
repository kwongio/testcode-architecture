package com.example.demo.stub;

import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;

public class UserStub {

    public final static String EMAIL = "rldh9037@naver.com";
    public final static String NICKNAME = "gio";
    public final static String ADDRESS = "Seoul";
    public final static String CERTIFICATION_CODE = "aaaa-bbbb-cccc";

    public static User active() {
        return User.builder()
                .id(1L)
                .email("rldh9037@naver.com")
                .nickname("gio")
                .address("Seoul")
                .status(UserStatus.ACTIVE)
                .certificationCode("aaaa-bbbb-cccc")
                .lastLoginAt(0L)
                .build();
    }

    public static User pending() {
        return User.builder()
                .id(2L)
                .email("rldh90372@naver.com")
                .nickname("gio")
                .address("Seoul")
                .status(UserStatus.PENDING)
                .certificationCode("aaaa-bbbb-cccc")
                .lastLoginAt(0L)
                .build();
    }

    public static User create(Long id) {
        return User.builder()
                .id(id)
                .email("rldh9037@naver.com")
                .nickname("gio")
                .address("Seoul")
                .status(UserStatus.PENDING)
                .certificationCode("aaaa-bbbb-cccc")
                .lastLoginAt(0L)
                .build();
    }
}
