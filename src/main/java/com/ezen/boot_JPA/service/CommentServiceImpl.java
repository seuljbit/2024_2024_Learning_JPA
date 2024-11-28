package com.ezen.boot_JPA.service;

import com.ezen.boot_JPA.dto.CommentDTO;
import com.ezen.boot_JPA.entity.Comment;
import com.ezen.boot_JPA.repository.BoardRepository;
import com.ezen.boot_JPA.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService{
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    @Override
    public long post(CommentDTO commentDTO) {
        boardRepository.incrementCommentCount(commentDTO.getBno());

        return commentRepository.save(convertDtoToEntity(commentDTO)).getCno();
    }

//    @Override
//    public List<CommentDTO> getList(long bno) {
//        List<Comment> list = commentRepository.findByBno(bno);
//        List<CommentDTO> dtoList =
//                list.stream().map(b -> convertEntityToDto(b)).toList();
//
//        return dtoList;
//    }
    @Override
    public Page<CommentDTO> getList(long bno, int page) {
        Pageable pageable = PageRequest.of(page, 5, Sort.by("cno").descending());
        Page<Comment> list = commentRepository.findByBno(bno, pageable);
        Page<CommentDTO> dtoPageList = list.map(c -> convertEntityToDto(c));

        return dtoPageList;
    }

    @Override
    public Long modify(CommentDTO commentDTO) {
        Optional<Comment> optional = commentRepository.findById(commentDTO.getCno());

        if(optional.isPresent()) {
            Comment entity = optional.get();
            entity.setContent(commentDTO.getContent());

            return commentRepository.save(entity).getCno();
        }

        return null;
    }

    @Override
    @Transactional
    public void delete(long cno, long bno) {
        boardRepository.decrementCommentCount(bno);
        commentRepository.deleteById(cno);
    }

}
