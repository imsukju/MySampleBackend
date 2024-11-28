package com.practiceBackend.practiceBackend.entity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class CheckMail {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long mailkey;
    @Column(unique = true)
    private String mail;
    private String code;
    private long expirationTime;
}
