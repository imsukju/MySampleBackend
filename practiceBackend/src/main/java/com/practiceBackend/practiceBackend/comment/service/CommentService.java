package com.practiceBackend.practiceBackend.comment.service;

import com.practiceBackend.practiceBackend.comment.dto.CommentDtoPost;
import com.practiceBackend.practiceBackend.comment.repository.CommentRepository;
import com.practiceBackend.practiceBackend.entity.Comment;
import com.practiceBackend.practiceBackend.entity.Post;
import com.practiceBackend.practiceBackend.modules.post.repository.PostDao;
import com.practiceBackend.practiceBackend.modules.post.repository.PostRepository;
import com.practiceBackend.practiceBackend.modules.post.service.Postservice;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final Postservice postservice;

    public ResponseEntity<Map<?,?>> viewAllcomment(Long postid){
        Post post;

        if(postRepository.findById(postid).isPresent()  ){
            post = postRepository.findById(postid).get();
            List<Comment> comments = post.getComments();//나중에 멀티코어로 처리해보기
            Map map = new HashMap();

//            comments.stream().forEach(comment -> map.put("data",comment));
            map.put("data", comments);
            return ResponseEntity.ok(map);

        }else{
            return ResponseEntity.notFound().build();
        }
    }

    public void addComment(CommentDtoPost commentDtoPost){
        if(postRepository.findByPostkey(commentDtoPost.getPostId()).isPresent()){
            Post post = postRepository.findByPostkey(commentDtoPost.getPostId()).get();
            Comment comment = Comment.builder().content(commentDtoPost.getContent()).post(post).build();
            commentRepository.save(comment);
        }

    }

}
