package com.example.demo.stub;

import com.example.demo.post.domain.Post;
import com.example.demo.user.domain.User;

public class PostStub {
    public static Post create(User user) {
        return Post.builder()
                .id(1L)
                .content("content")
                .createdAt(0L)
                .modifiedAt(0L)
                .writer(user)
                .build();
    }


}
