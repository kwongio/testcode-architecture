package com.example.demo.post.domain;

import com.example.demo.mock.FakeClockHolder;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class PostTest {

    @DisplayName("Post는 PostCreate와 User를 받아 생성할 수 있다")
    @Test
    public void create() throws IOException {
        //given
        PostCreate postCreate = PostCreate.builder()
                .content("hi")
                .writerId(1L)
                .build();
        User writer = User.builder()
                .id(1L)
                .email("rldh9037@naver.com")
                .nickname("gio")
                .address("Seoul")
                .status(UserStatus.ACTIVE)
                .certificationCode(UUID.randomUUID().toString())
                .build();
        //when
        Post post = Post.from(postCreate, writer, new FakeClockHolder(100L));


        //then
        assertThat(post.getContent()).isEqualTo("hi");
        assertThat(post.getWriter().getId()).isEqualTo(1L);
        assertThat(post.getWriter().getEmail()).isEqualTo("rldh9037@naver.com");
        assertThat(post.getWriter().getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(post.getWriter().getAddress()).isEqualTo("Seoul");
        assertThat(post.getWriter().getNickname()).isEqualTo("gio");
        assertThat(post.getCreatedAt()).isEqualTo(100L);

    }


}