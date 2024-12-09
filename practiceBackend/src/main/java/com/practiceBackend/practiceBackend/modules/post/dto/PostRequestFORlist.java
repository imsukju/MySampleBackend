package com.practiceBackend.practiceBackend.modules.post.dto;

import com.practiceBackend.practiceBackend.entity.Post;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class PostRequestFORlist {
    private Long numbers;           // 게시물 번호
    private String title;          // 게시물 제목
    private Integer count;         // 댓글 수 (nullable)
    private String writer;         // 작성자 이름 (nullable)
    private String writerImage;    // 작성자 이미지
    private boolean writerIsDelete;// 작성자 탈퇴 여부
    private String postDate;       // 게시물 작성 날짜
    private int likes;             // 추천 수
    private int views;             // 조회수

    // Getters and Setters
    // Constructor(s)

    public static List<PostRequestFORlist> convertListToDTO(List<Post> posts) {
        List<PostRequestFORlist> postRequestList = new ArrayList<>();
        try {
            postRequestList =  posts.stream()
                    .map(e -> PostRequestFORlist.builder()
                            .numbers(e.getPostkey())
                            .title(e.getTitle())
                            .writer(e.getUser().getUserid())
                            .writerImage(e.getUser().getProfileImage())
                            .postDate(e.getCreatedAt().toString())
                            .likes(e.getLikes())
                            .views(e.getViews())
                            .count(e.getComments().size())
                            .build()
                    )
                    .collect(Collectors.toList());
        }catch (Exception e){
            e.printStackTrace();
        }



        return postRequestList;
    }

}
