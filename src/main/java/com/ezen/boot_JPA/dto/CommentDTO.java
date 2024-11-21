package com.ezen.boot_JPA.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentDTO {
    private long cno;
    private long bno;
    private String writer;
    private String content;
    private LocalDateTime regAt, modAt;
}
