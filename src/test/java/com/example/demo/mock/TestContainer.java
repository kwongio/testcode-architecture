package com.example.demo.mock;

import com.example.demo.common.service.port.ClockHolder;
import com.example.demo.common.service.port.UuidHolder;
import com.example.demo.post.service.PostService;
import com.example.demo.post.service.port.PostRepository;
import com.example.demo.user.service.CertificationService;
import com.example.demo.user.service.UserService;
import com.example.demo.user.service.port.MailSender;
import com.example.demo.user.service.port.UserRepository;
import lombok.Builder;

public class TestContainer {

    public final MailSender mailSender;
    public final UserRepository userRepository;
    public final UserService userService;
    public final PostRepository postRepository;
    public final PostService postService;


    @Builder
    public TestContainer(ClockHolder clockHolder, UuidHolder uuidHolder) {
        this.mailSender = new FakeMailSender();
        this.userRepository = new FakeUserRepository();
        this.userService = UserService.builder()
                .uuidHolder(uuidHolder)
                .clockHolder(clockHolder)
                .certificationService(new CertificationService(mailSender))
                .userRepository(userRepository)
                .build();

        this.postRepository = new FakePostRepository();
        this.postService = PostService.builder()
                .postRepository(postRepository)
                .userRepository(userRepository)
                .clockHolder(clockHolder)
                .build();

    }



}
