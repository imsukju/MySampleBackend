package com.practiceBackend.practiceBackend.comment.dto;
import com.practiceBackend.practiceBackend.entity.Comment;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentViewDTO {
    public Long commentId;
    public String writerImage;
    public boolean writerIsDelete;
    public String writer;
    public LocalDateTime postDate;
    public String content;

    public static CommentViewDTO commentToCommentDto(Comment comment) {
      return  CommentViewDTO.builder()
                .commentId(comment.getCommentkey())
                .writerImage(comment.getUser().getProfileImage())
                .writerIsDelete(false) // 나중에 탈퇴회원 추가할 예정
                .writer(comment.getUser().getUserid())
                .postDate(LocalDateTime.now())
                .content(comment.getContent())
                .build();

    }
}
