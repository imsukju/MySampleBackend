package com.practiceBackend.practiceBackend.modules.post.dto;

import java.util.ArrayList;
import java.util.List;

import com.practiceBackend.practiceBackend.entity.Attachment;
import com.practiceBackend.practiceBackend.entity.Tag;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreatePostDTO{
    private String title;          // 게시글 제목
    private String content;        // 게시글 내용
    private boolean privatePost;   // 비공개 게시 여부
    private boolean blockComment;  // 댓글 차단 여부
    private List<String> tags;     // 태그 리스트
    private List<Long> deletedFileIds;  // 삭제된 파일 ID 리스트
}
