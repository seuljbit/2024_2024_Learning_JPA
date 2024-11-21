package com.ezen.boot_JPA.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Comment extends TimeBase{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long cno;

    @Column(nullable = false)
    private long bno;

    @Column(length = 15, nullable = false)
    private String writer;

    @Column(length = 500, nullable = false)
    private String content;
}
