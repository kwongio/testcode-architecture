package com.example.demo.post.controller.response;

import com.example.demo.post.domain.Post;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class PostResponseTest {

    @DisplayName("PostResponse는 Post 객체로 생성할 수 있다.")
    @Test
    public void createResponse() throws IOException {
        //given
        Post post = Post.builder()
                .content("helloworld")
                .writer(User.builder()
                        .id(1L)
                        .email("rldh9037@naver.com")
                        .nickname("gio")
                        .address("Seoul")
                        .status(UserStatus.ACTIVE)
                        .certificationCode(UUID.randomUUID().toString())
                        .build())
                .build();

        //when
        PostResponse postResponse = PostResponse.from(post);

        //then
        assertThat(postResponse.getContent()).isEqualTo("helloworld");
        assertThat(postResponse.getWriter().getEmail()).isEqualTo("rldh9037@naver.com");
        assertThat(postResponse.getWriter().getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(postResponse.getWriter().getNickname()).isEqualTo("gio");

    }

}