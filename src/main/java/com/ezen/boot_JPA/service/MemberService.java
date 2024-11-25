package com.ezen.boot_JPA.service;

import com.ezen.boot_JPA.dto.AuthUserDTO;
import com.ezen.boot_JPA.dto.MemberDTO;
import com.ezen.boot_JPA.entity.AuthUser;
import com.ezen.boot_JPA.entity.Member;

import java.util.List;

public interface MemberService {
    // convert 작업

    default Member convertDtoToEntity(MemberDTO memberDTO) {
        return Member.builder()
                .email(memberDTO.getEmail())
                .pwd(memberDTO.getPwd())
                .nickName(memberDTO.getNickName())
                .lastLogin(memberDTO.getLastLogin())
                .build();
    }

    default AuthUser convertDtoToAuthEntity(MemberDTO memberDTO) {
        return AuthUser.builder()
                .email(memberDTO.getEmail())
                .auth("ROLE_USER")
                .build();
    }

    default AuthUserDTO convertEntityToAuthDto(AuthUser authUser) {
        return AuthUserDTO.builder()
                .email(authUser.getEmail())
                .auth(authUser.getAuth())
                .build();
    }

    default MemberDTO convertEntityToDto(Member member, List<AuthUserDTO> authUserList) {
        return MemberDTO.builder()
                .email(member.getEmail())
                .pwd(member.getPwd())
                .nickName(member.getNickName())
                .lastLogin(member.getLastLogin())
                .regAt(member.getRegAt())
                .modAt(member.getModAt())
                .authUserList(authUserList)
                .build();
    }

    String joinMembership(MemberDTO memberDTO);

    MemberDTO selectEmail(String username);

    boolean updateLastLogin(String authEmail);

    List<MemberDTO> getList();

    boolean updateMember(MemberDTO memberDTO);

    boolean remove(String name);
}
