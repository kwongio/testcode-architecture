package com.example.demo.mock;

import com.example.demo.post.domain.Post;
import com.example.demo.post.service.port.PostRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FakePostRepository implements PostRepository {

    private long id = 1L;
    private List<Post> data = new ArrayList<>();

    @Override
    public Post save(Post post) {
        if (post.getId() == null || post.getId() == 0L) {
            Post newPost = Post.builder()
                    .id(id++)
                    .content(post.getContent())
                    .createdAt(post.getCreatedAt())
                    .modifiedAt(post.getModifiedAt())
                    .writer(post.getWriter())
                    .build();
            data.add(newPost);
            return newPost;
        }
        data.removeIf(p -> p.getId().equals(post.getId()));
        data.add(post);
        return post;
    }

    @Override
    public Optional<Post> findById(long id) {
        return data.stream().filter(post -> post.getId().equals(id)).findAny();
    }
}
