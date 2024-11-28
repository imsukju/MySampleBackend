package com.practiceBackend.practiceBackend.modules.post.repository;

import com.practiceBackend.practiceBackend.entity.Post;
import com.practiceBackend.practiceBackend.entity.QPost;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.AllArgsConstructor;
import org.hibernate.mapping.Selectable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class PostDao {

// lt   : less than               (작다)             => WHERE field < value
// loe  : less than or equal to   (작거나 같다)      => WHERE field <= value
// gt   : greater than            (크다)             => WHERE field > value
// goe  : greater than or equal to(크거나 같다)      => WHERE field >= value
// eq   : equals                  (같다)             => WHERE field = value
// ne   : not equals              (같지 않다)        => WHERE field != value

    private final JPAQueryFactory jpaQueryFactory;

    //SELECT *
    //FROM posts p
    //WHERE p.id >= (
    //    SELECT id FROM posts
    //    ORDER BY created_at DESC
    //    LIMIT 1 OFFSET 80
    //)
    //ORDER BY p.created_at DESC
    //LIMIT 10;
//    public List<Post> getPosts(Long lastId, int size) {
//        QPost post = QPost.post;
//
//        return jpaQueryFactory
//                .selectFrom(post)
//                .orderBy(post.createdAt.desc())
//                .where(
//                )
//
//
//    }
//
//
//    public Post supportOffset(int page,int size) {
//        QPost post = QPost.post;
//
//
//        Post a = jpaQueryFactory
//    }

    public List<Post> getPostByOffset(int page, int limit) {
//        if (page < 1) {
//            throw new IllegalArgumentException("Page must be greater than or equal to 1");
//        }

        QPost qPost = QPost.post;
        return jpaQueryFactory.selectFrom(qPost)
                .orderBy(qPost.createdAt.desc())
                .offset((page) * limit)
                .limit(10)
                .fetch();

    }

//    public Long getLastId
}
