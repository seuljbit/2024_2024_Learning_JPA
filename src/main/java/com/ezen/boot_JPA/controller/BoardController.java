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
import org.springframework.security.core.Authentication;
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
    public void register(Authentication authentication, Model model) {
        if (authentication != null && authentication.isAuthenticated()) {
            // 로그인 사용자의 이메일 추가
            model.addAttribute("writer", authentication.getName());
            model.addAttribute("isAuthenticated", true);
        } else {
            model.addAttribute("isAuthenticated", false);
        }
    }

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

//    @PostMapping("/register")
//    public String register(BoardDTO boardDTO, @RequestParam(name = "files", required = false)MultipartFile[] files) {
//        List<FileDTO> fileList = null;
//
//        if(files != null && files[0].getSize() > 0) {
//            // 파일 핸들러 작업
//            fileList = fileHandler.uploadFiles(files);
//            log.info(">>> file list : {}", fileList);
//        }
//
//        BoardFileDTO boardFileDTO = new BoardFileDTO(boardDTO, fileList);
//        Long bno = boardService.insert(boardFileDTO);
//            log.info(">>> bno: {}", bno);
//
//        return "/index";
//    }
    @PostMapping("/register")
    public String register(BoardDTO boardDTO,
                           @RequestParam(name = "files", required = false) MultipartFile[] files,
                           Authentication authentication) {
        // 작성자 처리
        if (authentication != null && authentication.isAuthenticated()) {
            boardDTO.setWriter(authentication.getName()); // 로그인 사용자의 이메일 설정
        } else if (boardDTO.getWriter() == null || boardDTO.getWriter().isEmpty()) {
            throw new IllegalArgumentException("작성자명을 입력해야 합니다."); // 비회원은 작성자명 필수
        }

        // 파일 처리
        List<FileDTO> fileList = null;
        if (files != null && files[0].getSize() > 0) {
            fileList = fileHandler.uploadFiles(files);
            log.info(">>> file list : {}", fileList);
        }

        // BoardFileDTO 생성 및 저장
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

//    @GetMapping("/list")
//    public void list(Model model,
//                     @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo) {
//        // 화면에서 들어오는 pageNo = 1 / 0으로 처리되어야 함
//        // 화면에서 들어오는 pageNo = 2 / 1로 처리되어야 함
//        log.info(">>> pageNo : {}", pageNo);
//        pageNo = (pageNo == 0 ? 0 : pageNo - 1);
//
//        Page<BoardDTO> list = boardService.getList(pageNo);
//
//        log.info(">>> list : {}", list.toString());
//        log.info(">>> Total count : {}", list.getTotalElements()); // 전체 글 수
//        log.info(">>> Total page : {}", list.getTotalPages()); // 전체 페이지 수 (=realEndPage)
//        log.info(">>> Page Number : {}", list.getNumber()); // 전체 페이지 번호(pageNO)
//        log.info(">>> page Size : {}", list.getSize()); // 한 페이지에 표시되는 길이
//        log.info(">>> next : {}", list.hasNext()); // next 여부
//        log.info(">>> prev : {}", list.hasPrevious()); // prev 여부
//
//        PagingVO pagingVO = new PagingVO(list, pageNo);
//        log.info(">>> pagingVO : {}", pagingVO);
//
//        model.addAttribute("list", list);
//        model.addAttribute("pagingVO", pagingVO);
//    }
@GetMapping("/list")
public String list(Authentication authentication, Model model,
                   @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo) {
    pageNo = (pageNo == 0 ? 0 : pageNo - 1);

    // 데이터 로드
    Page<BoardDTO> list = boardService.getList(pageNo);

    // Null 방어 코드 추가
    //list = (Page<BoardDTO>) list.filter(Objects::nonNull);

    // 디버깅: 데이터 점검
    list.forEach(bvo -> log.info("게시글 ID: {}, 댓글 수: {}", bvo.getBno(), bvo.getCmtCount()));

    PagingVO pagingVO = new PagingVO(list, pageNo);

    boolean isAdmin = authentication != null && authentication.getAuthorities().stream()
            .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

    model.addAttribute("isAdmin", isAdmin);
    model.addAttribute("list", list);
    model.addAttribute("pagingVO", pagingVO);

    return "/board/list";
}


    @GetMapping("/password-check")
    public String passwordCheck(@RequestParam("bno") Long bno, Model model) {
        model.addAttribute("bno", bno); // 게시글 번호 전달
        return "/board/password-check"; // 비밀번호 확인 페이지
    }

    @PostMapping("/board/password-check")
    public String checkPassword(@RequestParam("bno") Long bno,
                                @RequestParam("password") String password,
                                Model model) {
        // 비밀번호 확인
        boolean isValidPassword = boardService.checkPassword(bno, password);

        if (isValidPassword) {
            return "redirect:/board/detail?bno=" + bno;
        }

        // 비밀번호가 틀린 경우
        model.addAttribute("error", "비밀번호가 틀렸습니다.");
        model.addAttribute("bno", bno); // bno 값 유지

        return "board/password-check"; // 비밀번호 확인 페이지로 다시 이동
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
