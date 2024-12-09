package com.practiceBackend.practiceBackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Attachment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ATTACHMENT_KEY")
    private Long attachmentkey;

    @ManyToOne(fetch = FetchType.LAZY,cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "POST_KEY")
    @JsonIgnore
    private Post post;

    String s3url;
    String Filename;


}
