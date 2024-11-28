package com.practiceBackend.practiceBackend.comment.repository;

import com.practiceBackend.practiceBackend.entity.Comment;
import com.practiceBackend.practiceBackend.entity.Post;
import com.practiceBackend.practiceBackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    public List<Optional<Comment>> findByPost(Post post);
    public List<Optional<Comment>> findByUser(User user);
}
