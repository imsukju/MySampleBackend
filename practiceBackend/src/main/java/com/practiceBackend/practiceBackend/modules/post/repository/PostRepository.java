package com.practiceBackend.practiceBackend.modules.post.repository;

import com.practiceBackend.practiceBackend.entity.Post;
import com.practiceBackend.practiceBackend.entity.QPost;
import com.practiceBackend.practiceBackend.entity.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByTitle(String title,Pageable pageable);
    List<Post> findByTitleStartsWith(String title,Pageable pageable);
    List<Optional<Post>> findByUser(User user);
    Optional<Post> findByPostkey(Long postkey);

    //Tag를 기준으로 페이징 하여서 전달, findBy 뒤에는 엔티티이름, 칼럼
    List<Post> findByTagsTagname(String tagname, Pageable pageable);
    long countByTagsTagname(String tagname);
    long countByTitle(String title);

}
