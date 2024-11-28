package com.ezen.boot_JPA.entity;

import jakarta.persistence.*;
import lombok.*;

/* DTO : 객체를 생성하는 클래스
  - 자주 쓰는 코드들(regDate, modDate, isDel 등)은 base class로 별도 관리

  * 기본키 생성 전략 : GeneratedValue*/

@Entity // DB의 테이블 클래스
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Board extends TimeBase {

    @Id // 기본키
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increments 생성
    private long bno;

    @Column(length = 100, nullable = false) // nullable = false -> not null
    private String title;

    @Column(length = 200, nullable = false)
    private String writer;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(length = 255, nullable = false)
    private String password;

    @Column(name = "cmt_count", nullable = false)
    private int cmtCount = 0;

    /*
     생성 시 초기화 값 지정 : 객체가 생길 때 객체의 기본값 생성
     @Builder.Default
     private int point = 0;
    */
}
