package com.practiceBackend.practiceBackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "TAG_KEY")
    private long tagkey;

    private String tagname;

    @ManyToMany(mappedBy = "tags")
    @JsonIgnore
    private List<Post> postList = new ArrayList<>();
}
