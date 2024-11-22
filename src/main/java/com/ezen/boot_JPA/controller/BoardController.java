package com.ezen.boot_JPA.controller;

import com.ezen.boot_JPA.dto.BoardFileDTO;
import com.ezen.boot_JPA.handler.FileHandler;
import com.ezen.boot_JPA.dto.BoardDTO;
import com.ezen.boot_JPA.dto.FileDTO;
import com.ezen.boot_JPA.dto.PagingVO;
import com.ezen.boot_JPA.handler.FileRemoveHandler;
import com.ezen.boot_JPA.service.BoardService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/board/*")
public class BoardController {
    private final BoardService boardService;
    private final FileHandler fileHandler;
    private final FileRemoveHandler fileRemoveHandler;

    @GetMapping("/register")
    public void register() {}

//    @PostMapping("/register")
//    public String register(BoardDTO boardDTO) {
//        log.info(">>> boardDTO : {}", boardDTO);
//
//        // 기존 : insert, update, delete -> return 1 row
//        // jpa : insert, update, delete -> return id
//        Long bno = boardService.insert(boardDTO);
//
//        log.info(">>>>> insert : {}", bno > 0 ? "Ok!" : "Fail");
//
//        return "/index";
//    }

    @PostMapping("/register")
    public String register(BoardDTO boardDTO, @RequestParam(name = "files", required = false)MultipartFile[] files) {
        List<FileDTO> fileList = null;

        if(files != null && files[0].getSize() > 0) {
            // 파일 핸들러 작업
            fileList = fileHandler.uploadFiles(files);
            log.info(">>> file list : {}", fileList);
        }

        BoardFileDTO boardFileDTO = new BoardFileDTO(boardDTO, fileList);
        Long bno = boardService.insert(boardFileDTO);
            log.info(">>> bno: {}", bno);

        return "/index";
    }

//    @GetMapping("/list")
//    public void list(Model model) { // paging 없는 케이스
//        List<BoardDTO> list = boardService.getList();
//        model.addAttribute("list", list);
//    }

    @GetMapping("/list")
    public void list(Model model,
                     @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo) {
        // 화면에서 들어오는 pageNo = 1 / 0으로 처리되어야 함
        // 화면에서 들어오는 pageNo = 2 / 1로 처리되어야 함
        log.info(">>> pageNo : {}", pageNo);
        pageNo = (pageNo == 0 ? 0 : pageNo - 1);

        Page<BoardDTO> list = boardService.getList(pageNo);

        log.info(">>> list : {}", list.toString());
        log.info(">>> Total count : {}", list.getTotalElements()); // 전체 글 수
        log.info(">>> Total page : {}", list.getTotalPages()); // 전체 페이지 수 (=realEndPage)
        log.info(">>> Page Number : {}", list.getNumber()); // 전체 페이지 번호(pageNO)
        log.info(">>> page Size : {}", list.getSize()); // 한 페이지에 표시되는 길이
        log.info(">>> next : {}", list.hasNext()); // next 여부
        log.info(">>> prev : {}", list.hasPrevious()); // prev 여부

        PagingVO pagingVO = new PagingVO(list, pageNo);
        log.info(">>> pagingVO : {}", pagingVO);

        model.addAttribute("list", list);
        model.addAttribute("pagingVO", pagingVO);
    }

    @GetMapping("/detail")
    public void modify(Model model, @RequestParam("bno") Long bno) {
        // BoardDTO boardDTO = boardService.getDetail(bno);
        BoardFileDTO boardFileDTO = boardService.getDetail(bno);
        model.addAttribute("boardFileDTO", boardFileDTO);
    }

//    @PostMapping("/modify")
//    public String modify(BoardDTO boardDTO, RedirectAttributes redirectAttributes) {
//        Long bno = boardService.modify(boardDTO);
//        redirectAttributes.addAttribute("bno", boardDTO.getBno());
//
//        return "redirect:/board/detail";
//    }
    @PostMapping("/modify")
    public String modify(BoardDTO boardDTO,
                         @RequestParam(name = "files", required = false) MultipartFile[] files,
                         RedirectAttributes redirectAttributes) {

        List<FileDTO> fileList = null;
        if(files != null && files[0].getSize() > 0) {
            fileList = fileHandler.uploadFiles(files);
        }

        Long bno = boardService.modify(new BoardFileDTO(boardDTO, fileList));
        redirectAttributes.addAttribute("bno", boardDTO.getBno());

        return "redirect:/board/detail";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam("bno") Long bno) {
        boardService.delete(bno);

        return "redirect:/board/list";
    }

    @ResponseBody
    @DeleteMapping("/file/{uuid}")
    public String fileDelete(@PathVariable("uuid") String uuid) {
        log.info(">>> Deleting file with UUID: {}", uuid);

        FileDTO fileDTO = boardService.getFile(uuid);
        log.info(">>>>>>>>>> fileDTO : {}", fileDTO);
        boolean isDel = fileRemoveHandler.deleteFile(fileDTO);

        long bno =  boardService.fileDelete(uuid);

        return (bno > 0 && isDel) ? "1" : "0";
    }
}
