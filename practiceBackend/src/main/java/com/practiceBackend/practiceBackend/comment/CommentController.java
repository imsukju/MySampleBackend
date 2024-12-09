package com.practiceBackend.practiceBackend.comment;

import com.practiceBackend.practiceBackend.comment.dto.CommentDtoPost;
import com.practiceBackend.practiceBackend.comment.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comments")
@AllArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getCommentById(@PathVariable Long id) {

        return this.commentService.viewAllcomment(id);
    }

    @PostMapping
    public void addComment(@RequestBody CommentDtoPost commentDtoPost, HttpServletRequest request) {
        commentService.addComment(commentDtoPost, request);

    }


}
