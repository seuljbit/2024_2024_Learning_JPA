package com.ezen.boot_JPA.repository;

import com.ezen.boot_JPA.entity.Board;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/* JpaRepository<테이블, ID>*/
public interface BoardRepository extends JpaRepository<Board, Long> {
    @Modifying
    @Query("UPDATE Board b SET b.cmtCount = b.cmtCount + 1 WHERE b.bno = :bno")
    void incrementCommentCount(@Param("bno") long bno);

    @Modifying
    @Query("UPDATE Board b SET b.cmtCount = CASE WHEN b.cmtCount > 0 THEN b.cmtCount - 1 ELSE 0 END WHERE b.bno = :bno")
    void decrementCommentCount(@Param("bno") long bno);
}
