package com.practiceBackend.practiceBackend.modules.post.repository;

import com.practiceBackend.practiceBackend.entity.Post;
import com.practiceBackend.practiceBackend.entity.QPost;
import com.practiceBackend.practiceBackend.entity.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    Optional<Post> findByTitle(String title);
    List<Optional<Post>> findByUser(User user);
    Optional<Post> findByPostkey(Long postkey);


}
