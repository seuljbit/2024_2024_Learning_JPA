package com.ezen.boot_JPA.security;

import com.ezen.boot_JPA.dto.MemberDTO;
import com.ezen.boot_JPA.service.AuthMember;
import com.ezen.boot_JPA.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Slf4j
public class CustomUserService implements UserDetailsService {

    @Autowired
    public MemberService memberService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MemberDTO memberDTO = memberService.selectEmail(username);
        log.info(">>> login User : {}", memberDTO);

        if (memberDTO == null) {
            throw new UsernameNotFoundException(username);
        }

        return new AuthMember(memberDTO);
    }
}
