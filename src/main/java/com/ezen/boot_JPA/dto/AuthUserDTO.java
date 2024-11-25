package com.ezen.boot_JPA.dto;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthUserDTO {
    private long id;
    private String email;
    private String auth;
}
