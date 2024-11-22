package com.ezen.boot_JPA.handler;

import com.ezen.boot_JPA.dto.FileDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
@Slf4j
@RequiredArgsConstructor
public class FileRemoveHandler {
    private final String BASE_PATH = "D:\\_myProject\\_java\\_fileUpload\\";

    public boolean deleteFile(FileDTO fileDTO) {
        File filePath = new File(BASE_PATH, fileDTO.getSaveDir()); // 파일 경로
        boolean isDel = false;
        String fileName = fileDTO.getUuid() + "_" + fileDTO.getFileName(); // 저장된 파일명

        try {
            File deleteFile = new File(filePath, fileName);
            log.info(">>> delete File : {}", deleteFile);

            if(deleteFile.exists()) {
                isDel = deleteFile.delete();
            }

            if(fileDTO.getFileType() > 0) {
                String thumbPath = fileDTO.getUuid() + "_th_" + fileDTO.getFileName();
                File deleteThumbFile = new File(filePath, thumbPath);
                log.info(">>> delete Thumnail File : {}", deleteThumbFile);

                if(deleteThumbFile.exists()) {
                    deleteThumbFile.delete();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isDel;
    }
}
