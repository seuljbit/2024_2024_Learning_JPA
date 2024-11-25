package com.ezen.boot_JPA.controller;

import com.ezen.boot_JPA.dto.MemberDTO;
import com.ezen.boot_JPA.service.MemberService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/user/*")
public class MemberController {
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/join")
    public void join() {}

    @PostMapping("/join")
    public String join(MemberDTO memberDTO) {
        log.info(">>> memberDTO : {}", memberDTO);
        memberDTO.setPwd(passwordEncoder.encode(memberDTO.getPwd())); // password 암호화
        String email = memberService.joinMembership(memberDTO);

        return (email == null ? "/user/join" : "/index");
    }

    @GetMapping("/login")
    public void login(@RequestParam(value = "error", required = false) String error,
                      @RequestParam(value = "exception", required = false) String exception, Model model) {
        // 에러와 예외값을 담아 화면으로 전달
        model.addAttribute("error", error);
        model.addAttribute("exception", exception);
    }

    @GetMapping("/list")
    public void list(Model model) {
        model.addAttribute("userList", memberService.getList());
    }

    @GetMapping("/modify")
    public void modify() {}

    @PostMapping("/modify")
    public String modify(MemberDTO memberDTO) {
        log.info(">>> MemberDTO : {}", memberDTO);

        if (memberDTO.getPwd() == null || memberDTO.getPwd().isEmpty()) {
            MemberDTO existingMember = memberService.selectEmail(memberDTO.getEmail());
            memberDTO.setPwd(existingMember.getPwd()); // 기존 비밀번호 설정
        } else {
            memberDTO.setPwd(passwordEncoder.encode(memberDTO.getPwd()));
        }

        memberService.updateMember(memberDTO);

        return "redirect:/user/logout";
    }

    @GetMapping("/remove")
    public String withdrawMember(Principal principal) {
        log.info(">>> Principal: {}", principal.getName());

        boolean isRemoved = memberService.remove(principal.getName());

        if (!isRemoved) {
            log.error(">>> Failed to remove member: {}", principal.getName());
            return "redirect:/"; // 실패 시 메인 페이지로 이동
        }

        return "redirect:/user/logout"; // 성공 시 로그아웃
    }

}
