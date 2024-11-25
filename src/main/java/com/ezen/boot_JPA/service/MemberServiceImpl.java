package com.ezen.boot_JPA.service;

import com.ezen.boot_JPA.dto.MemberDTO;
import com.ezen.boot_JPA.entity.AuthUser;
import com.ezen.boot_JPA.entity.Member;
import com.ezen.boot_JPA.repository.AuthUserRepository;
import com.ezen.boot_JPA.repository.MemberRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberServiceImpl implements MemberService{
    private final MemberRepository memberRepository;
    private final AuthUserRepository authUserRepository;


    @Override
    public String joinMembership(MemberDTO memberDTO) {
        String email = memberRepository.save(convertDtoToEntity(memberDTO)).getEmail();

        if(email != null) {
            authUserRepository.save(convertDtoToAuthEntity(memberDTO));
        }

        return email;
    }

    @Override
    public MemberDTO selectEmail(String username) {
        Optional<Member> optionalMember = memberRepository.findById(username);
        List<AuthUser> optionalAuthUser = authUserRepository.findByEmail(username);

        if(optionalMember.isPresent()) {
            MemberDTO memberDTO = convertEntityToDto(optionalMember.get(),
                    optionalAuthUser.stream()
                                    .map(u -> convertEntityToAuthDto(u))
                                    .toList()
            );
            log.info(">>> userDTO : {}", memberDTO);

            return memberDTO;
        }

        return null;
    }

    @Override
    public boolean updateLastLogin(String authEmail) {
        Optional<Member> optional = memberRepository.findById(authEmail);

        if(optional.isPresent()) {
            Member member = optional.get();
            member.setLastLogin(LocalDateTime.now()); // LocalDateTime.now() = 현재 날짜/시간
            String email = memberRepository.save(member).getEmail();

            return email != null; //return email == null ? false : true;
        }

        return false;
    }

    @Override
    public List<MemberDTO> getList() {
        List<Member> memberList = memberRepository.findAll(Sort.by(Sort.Direction.DESC, "regAt"));

        return memberList.stream()
                .map(member -> convertEntityToDto(
                        member,
                        authUserRepository.findByEmail(member.getEmail()).stream()
                                .map(this::convertEntityToAuthDto)
                                .toList()
                ))
                .toList();
    }

//    @Override
//    public boolean updateMember(MemberDTO memberDTO) {
//        // 기존 회원 정보 조회
//        Member existingMember = memberRepository.findById(memberDTO.getEmail())
//                .orElseThrow(() -> new IllegalArgumentException("User not found: " + memberDTO.getEmail()));
//
//        existingMember.setNickName(memberDTO.getNickName());
//
//        // 비밀번호 변경 여부 확인
//        if (memberDTO.getPwd() != null && !memberDTO.getPwd().isEmpty()) {
//            existingMember.setPwd(memberDTO.getPwd());
//        }
//
//        // 데이터베이스에 저장
//        memberRepository.save(existingMember);
//        log.info(">>> Member updated: {}", existingMember);
//
//        return true;
//    }

    @Override
    public boolean updateMember(MemberDTO memberDTO) {
        Optional<Member> optional = memberRepository.findById(memberDTO.getEmail());
        if (optional.isPresent()) {
            Member member = optional.get();

            if(!memberDTO.getPwd().isEmpty()) {
                member.setPwd(memberDTO.getPwd());
            }

            member.setNickName(memberDTO.getNickName());
            memberRepository.save(member).getEmail();

            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public boolean remove(String email) {
        Optional<Member> optionalMember = memberRepository.findById(email);

        if (optionalMember.isPresent()) {
            authUserRepository.deleteAllByEmail(email);
            memberRepository.deleteById(email);

            log.info(">>> Member removed: {}", email);
            return true;
        }

        log.warn(">>> Member not found: {}", email);
        return false;
    }
}
