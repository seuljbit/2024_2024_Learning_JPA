package com.ezen.boot_JPA.controller;

import com.ezen.boot_JPA.dto.CommentDTO;
import com.ezen.boot_JPA.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comment/*")
@RequiredArgsConstructor
@Slf4j
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/post")
    @ResponseBody
    public String post(@RequestBody CommentDTO commentDTO) {
        long cno = commentService.post(commentDTO);
        log.info(">>> commentDTO : {}", commentDTO);

        return cno > 0 ? "1" : "0";
    }

    @GetMapping("/list/{bno}/{page}")
    @ResponseBody
    public Page<CommentDTO> list(@PathVariable("bno") long bno, @PathVariable("page") int page) {
        // page = 0부터
        // 1 page => 0 / 2page = 1
        page = (page == 0 ? 0 : page - 1);

        //List<CommentDTO> list = commentService.getList(bno);
        Page<CommentDTO> list = commentService.getList(bno, page);
        log.info(">>> comment list : {}", list);

        return list;
    }

    @PutMapping("/modify")
    @ResponseBody
    public String modify(@RequestBody CommentDTO commentDTO) {
        Long cno = commentService.modify(commentDTO);

        return cno > 0 ? "1" : "0";
    }

    @DeleteMapping("/{cno}/{bno}")
    @ResponseBody
    public String delete(@PathVariable("cno") long cno, @PathVariable("bno") long bno) {
        commentService.delete(cno, bno);

        return "1";
    }
}
