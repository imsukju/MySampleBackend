package com.practiceBackend.practiceBackend.modules.post.repository;

import com.practiceBackend.practiceBackend.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Attechmentrepository extends JpaRepository<Attachment,Long> {
}
