package com.practiceBackend.practiceBackend.comment.dto;

import lombok.*;

@Getter
@Setter
@Builder
public class CommentDtoPost {
    private String content;
    private Long postId;

}
