package com.practiceBackend.practiceBackend.modules.post.repository;

import com.practiceBackend.practiceBackend.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByTagname(String tagname);
    boolean  existsByTagname(String tagname);
}
