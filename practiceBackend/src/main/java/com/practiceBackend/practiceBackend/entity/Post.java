package com.practiceBackend.practiceBackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.practiceBackend.practiceBackend.modules.post.service.Postservice;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "POST")
public class Post {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "POST_KEY")
    private Long postkey;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "USER_KEY")
    @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "post")
    @JsonManagedReference// 이 필드는 JSON에 포함
    List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    List<Attachment> attachments = new ArrayList<>();

    @Builder.Default
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "POST_TAG", // 조인 테이블 이름
            joinColumns = @JoinColumn(name = "POST_KEY"), // Post 엔티티의 외래 키
            inverseJoinColumns = @JoinColumn(name = "TAG_KEY") // Tag 엔티티의 외래 키

    )
    List<Tag> tags = new ArrayList<>();

    private String title;
    private String text;
    private boolean isprivate;
    private boolean isblockComment;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    private int likes = 0;             // 추천 수
    private int views = 0;




    public void removeTag(Tag tag) {
        tags.remove(tag);
    }



}
