package com.example.demo.post.infrastructure;

import com.example.demo.post.domain.Post;
import com.example.demo.user.infrastructure.UserEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Entity
@Table(name = "posts")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content")
    private String content;

    @Column(name = "created_at")
    private Long createdAt;

    @Column(name = "modified_at")
    private Long modifiedAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity writer;

    public Post toDomain() {
        return Post.builder().id(id).content(content).createdAt(createdAt).modifiedAt(modifiedAt).writer(writer.toDomain()).build();
    }

    public static PostEntity from(Post post) {
        return PostEntity.builder()
                .id(post.getId())
                .content(post.getContent()).
                createdAt(post.getCreatedAt()).
                modifiedAt(post.getModifiedAt()).
                writer(UserEntity.from(post.getWriter()))
                .build();
    }

    @Builder
    public PostEntity(Long id, String content, Long createdAt, Long modifiedAt, UserEntity writer) {
        this.id = id;
        this.content = content;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.writer = writer;
    }
}