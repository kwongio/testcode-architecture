package com.example.demo.post.service;

import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.post.domain.PostCreate;
import com.example.demo.post.domain.PostUpdate;
import com.example.demo.post.infrastructure.PostEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@SpringBootTest
@SqlGroup({
        @Sql(value = "/sql/post-service-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
class PostServiceTest {

    @Autowired
    private PostService postService;

    @MockBean
    private JavaMailSender javaMailSender;

    @DisplayName("getById로 존재하는 게시물을 내려준다.")
    @Test
    public void test1() {
        //given
        //when
        PostEntity result = postService.getById(1L);
        //then
        assertThat(result.getContent()).isEqualTo("content");
        assertThat(result.getWriter().getEmail()).isEqualTo("kwon@naver.com");

    }

    @DisplayName("id가 없는 post를 조회하면 에러를 던진다.")
    @Test
    public void test2() throws IOException {
        //given

        assertThatThrownBy(() -> postService.getById(1000L)).isInstanceOf(ResourceNotFoundException.class);
    }


    @DisplayName("PostCreateDto로 post를 생성할 수 있다.")
    @Test
    public void  create() {
        //given
        PostCreate postCreateDto = PostCreate.builder()
                .writerId(1L)
                .content("hi")
                .build();
        //when
        PostEntity result = postService.create(postCreateDto);
        //then
        assertThat(result.getContent()).isEqualTo("hi");
        assertThat(result.getWriter().getEmail()).isEqualTo("kwon@naver.com");
    }

    @DisplayName("postUpdateDto를 이용하여 게시물을 수정할 수 있다")
    @Test
    public void update() {
        //given
        PostUpdate postUpdateDto = PostUpdate.builder()
                .content("hi")
                .build();
        //when
        PostEntity result = postService.update(1L, postUpdateDto);
        //then
        assertThat(result.getContent()).isEqualTo("hi");
        assertThat(result.getWriter().getEmail()).isEqualTo("kwon@naver.com");

    }


}