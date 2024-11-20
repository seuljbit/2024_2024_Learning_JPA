package com.ezen.boot_JPA.controller;

import com.ezen.boot_JPA.dto.BoardDTO;
import com.ezen.boot_JPA.service.BoardService;

import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/board/*")
public class BoardController {
    private final BoardService boardService;

    @GetMapping("/register")
    public void register() {}

    @PostMapping("/register")
    public String register(BoardDTO boardDTO) {
        log.info(">>> boardDTO : {}", boardDTO);

        // 기존 : insert, update, delete -> return 1 row
        // jpa : insert, update, delete -> return id
        Long bno = boardService.insert(boardDTO);

        log.info(">>>>> insert : {}", bno > 0 ? "Ok!" : "Fail");

        return "/index";
    }

    @GetMapping("/list")
    public void list(Model model) {
        List<BoardDTO> list = boardService.getList();
        model.addAttribute("list", list);
    }

    @GetMapping("/detail")
    public void modify(Model model, @RequestParam("bno") Long bno) {
        BoardDTO boardDTO = boardService.getDetail(bno);
        model.addAttribute("boardDTO", boardDTO);
    }

    @PostMapping("/modify")
    public String modify(BoardDTO boardDTO, RedirectAttributes redirectAttributes) {
        Long bno = boardService.modify(boardDTO);
        redirectAttributes.addAttribute("bno", boardDTO.getBno());

        return "redirect:/board/detail";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam("bno") Long bno) {
        boardService.delete(bno);

        return "redirect:/board/list";
    }
}
