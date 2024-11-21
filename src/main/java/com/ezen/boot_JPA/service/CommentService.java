package com.ezen.boot_JPA.service;

import com.ezen.boot_JPA.dto.CommentDTO;
import com.ezen.boot_JPA.entity.Comment;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CommentService {
    // DTO -> Entity
    default Comment convertDtoToEntity(CommentDTO commentDTO) {
        return Comment.builder()
                .cno(commentDTO.getCno())
                .bno(commentDTO.getBno())
                .writer(commentDTO.getWriter())
                .content(commentDTO.getContent())
                .build();
    }

    // Entity -> DTO
    default CommentDTO convertEntityToDto(Comment comment) {
        return CommentDTO.builder()
                .cno(comment.getCno())
                .bno(comment.getBno())
                .writer(comment.getWriter())
                .content(comment.getContent())
                .regAt(comment.getRegAt())
                .modAt(comment.getModAt())
                .build();
    }

    long post(CommentDTO commentDTO);

    // List<CommentDTO> getList(long bno);
    Page<CommentDTO> getList(long bno, int page);

    Long modify(CommentDTO commentDTO);

    void delete(long cno, long bno);
}
