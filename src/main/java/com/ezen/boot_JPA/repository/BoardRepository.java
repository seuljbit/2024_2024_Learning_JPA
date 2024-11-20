package com.ezen.boot_JPA.repository;

import com.ezen.boot_JPA.entity.Board;

import org.springframework.data.jpa.repository.JpaRepository;

/* JpaRepository<테이블, ID>*/
public interface BoardRepository extends JpaRepository<Board, Long> {
}
