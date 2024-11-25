package com.ezen.boot_JPA.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "auth_user")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(length = 30, nullable = false)
    private String email;

    @Column(length = 50, nullable = false)
    private String auth;
}
