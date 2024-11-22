package com.ezen.boot_JPA.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class File extends TimeBase {

    @Id
    private String uuid;

    @Column(name = "save_dir", length = 256, nullable = false)
    private String saveDir;

    @Column(name = "file_name", length = 256, nullable = false)
    private String fileName;

    @Column(name = "file_type", columnDefinition = "tinyint default 0")
    private int fileType;

    @Column
    private long bno;

    @Column(name = "file_size", nullable = false)
    private long fileSize;
}
