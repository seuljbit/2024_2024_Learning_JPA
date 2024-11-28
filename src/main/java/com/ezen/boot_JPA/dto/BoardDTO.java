package com.ezen.boot_JPA.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardDTO {
    private long bno;
    private String title;
    private String writer;
    private String content;
    private String password;
    private LocalDateTime regAt, modAt;
    private int cmtCount = 0;

    // 날짜만 반환하는 메서드
    public String getFormattedDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return regAt.format(formatter);
    }

    // 시분초까지 반환하는 메서드
    public String getFormattedDateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return regAt.format(formatter);
    }

    // modAt 시분초까지 반환하는 메서드 (detail 페이지에서만 사용)
    public String getFormattedModDateTime() {
        if (modAt != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            return modAt.format(formatter);
        }
        return ""; // modAt이 null일 경우 빈 문자열 반환
    }
}


