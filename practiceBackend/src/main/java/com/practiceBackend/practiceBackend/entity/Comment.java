package com.practiceBackend.practiceBackend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "COMMENT_KEY")
    private Long commentkey;

    private String content;

    @ManyToOne
    @JoinColumn(name = "POST_KEY")
    @JsonBackReference
    // 이 필드는 JSON에 포함되지 않음
    private Post post;

    @ManyToOne
    @JoinColumn(name = "USER_KEY")
    private User user;
}
