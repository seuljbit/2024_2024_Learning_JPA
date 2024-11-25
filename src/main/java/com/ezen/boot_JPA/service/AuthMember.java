package com.ezen.boot_JPA.service;

import com.ezen.boot_JPA.dto.MemberDTO;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.stream.Collectors;

@Getter
@Slf4j
public class AuthMember extends User {

    private MemberDTO memberDTO;

    public AuthMember(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    public AuthMember(MemberDTO memberDTO) {
        super(memberDTO.getEmail(), memberDTO.getPwd(),
                memberDTO.getAuthUserList().stream()
                        .map(auth -> new SimpleGrantedAuthority(auth.getAuth()))
                        .collect(Collectors.toList())
        );

        this.memberDTO = memberDTO;
    }
}
