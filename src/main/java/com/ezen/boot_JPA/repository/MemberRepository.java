package com.ezen.boot_JPA.repository;

import com.ezen.boot_JPA.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, String> {

}
