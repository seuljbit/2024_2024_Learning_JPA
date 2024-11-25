package com.ezen.boot_JPA.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberDTO {
    private String email;
    private String pwd;
    private String nickName;
    private LocalDateTime lastLogin;
    private LocalDateTime regAt, modAt;

    private List<AuthUserDTO> authUserList;
}
