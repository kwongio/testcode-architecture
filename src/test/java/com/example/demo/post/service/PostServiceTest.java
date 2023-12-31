package com.example.demo.post.service;

import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.mock.FakeClockHolder;
import com.example.demo.mock.FakePostRepository;
import com.example.demo.mock.FakeUserRepository;
import com.example.demo.post.domain.Post;
import com.example.demo.post.domain.PostCreate;
import com.example.demo.post.domain.PostUpdate;
import com.example.demo.stub.PostStub;
import com.example.demo.stub.UserStub;
import com.example.demo.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;

import java.io.IOException;

import static com.example.demo.stub.UserStub.EMAIL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


class PostServiceTest {


    private PostService postService;

    @BeforeEach
    void init() {

        FakePostRepository postRepository = new FakePostRepository();
        FakeUserRepository userRepository = new FakeUserRepository();
        this.postService = PostService.builder()
                .postRepository(postRepository)
                .userRepository(userRepository)
                .clockHolder(new FakeClockHolder(100L))
                .build();

        userRepository.save(UserStub.pending());
        User user = UserStub.active();
        userRepository.save(user);
        postRepository.save(PostStub.create(user));
    }

    @MockBean
    private JavaMailSender javaMailSender;

    @DisplayName("getById로 존재하는 게시물을 내려준다.")
    @Test
    public void test1() {
        //given
        //when
        Post result = postService.getById(1L);
        //then
        assertThat(result.getContent()).isEqualTo("content");
        assertThat(result.getWriter().getEmail()).isEqualTo(EMAIL);

    }

    @DisplayName("id가 없는 post를 조회하면 에러를 던진다.")
    @Test
    public void test2() throws IOException {
        //given

        assertThatThrownBy(() -> postService.getById(1000L)).isInstanceOf(ResourceNotFoundException.class);
    }


    @DisplayName("PostCreateDto로 post를 생성할 수 있다.")
    @Test
    public void create() {
        //given
        PostCreate postCreateDto = PostCreate.builder()
                .writerId(1L)
                .content("hi")
                .build();
        //when
        Post result = postService.create(postCreateDto);
        //then
        assertThat(result.getContent()).isEqualTo("hi");
        assertThat(result.getWriter().getEmail()).isEqualTo(EMAIL);
        assertThat(result.getCreatedAt()).isEqualTo(100L);

    }

    @DisplayName("postUpdateDto를 이용하여 게시물을 수정할 수 있다")
    @Test
    public void update() {
        //given
        PostUpdate postUpdateDto = PostUpdate.builder()
                .content("hi")
                .build();
        //when
        Post result = postService.update(1L, postUpdateDto);
        //then
        assertThat(result.getContent()).isEqualTo("hi");
        assertThat(result.getWriter().getEmail()).isEqualTo(EMAIL);
        assertThat(result.getModifiedAt()).isEqualTo(100L);

    }


}