package com.ezen.boot_JPA.service;

import com.ezen.boot_JPA.dto.BoardDTO;
import com.ezen.boot_JPA.dto.BoardFileDTO;
import com.ezen.boot_JPA.dto.FileDTO;
import com.ezen.boot_JPA.entity.Board;
import com.ezen.boot_JPA.entity.File;
import org.springframework.data.domain.Page;

/* interface : 추상 메서드만 가능 but 일부 메서드 구현 가능
 * 메서드를 default(접근 제한자)로 선언하면 구현 가능
  - 자식 클래스(구현체)에서 디폴트 메소드를 오버라이딩하여 재정의 가능
 */
public interface BoardService {

    /* BoardDTO(java class) : bno, title, writer, content, regAt, modAt
     * Board(table) : bno, title, writer, content */

    // 화면에서 가져온 BoardDTO 객체를 저장하기 위해 Board 객체로 변환(BoardDTO -> Board 변환)
    default Board convertDtoToEntity(BoardDTO boardDTO) {
        return Board.builder()
                .bno(boardDTO.getBno())
                .title(boardDTO.getTitle())
                .writer(boardDTO.getWriter())
                .content(boardDTO.getContent())
                .password(boardDTO.getPassword())
                .cmtCount(boardDTO.getCmtCount())
                .build();
    }

    // DB에서 가져온 Board 객체를 화면에 뿌리기 위해 BoardDTO 객체로 변환(Board -> BoardDTO 변환)
    default BoardDTO convertEntityToDto(Board board) {
        return BoardDTO.builder()
                .bno(board.getBno())
                .title(board.getTitle())
                .writer(board.getWriter())
                .content(board.getContent())
                .regAt(board.getRegAt())
                .modAt(board.getModAt())
                .password(board.getPassword())
                .cmtCount(board.getCmtCount())
                .build();
    }

    // FileDTO -> File Entity
    default File convertDtoToEntity(FileDTO fileDTO) {
        return File.builder()
                .uuid(fileDTO.getUuid())
                .saveDir(fileDTO.getSaveDir())
                .fileName(fileDTO.getFileName())
                .fileType(fileDTO.getFileType())
                .bno(fileDTO.getBno())
                .fileSize(fileDTO.getFileSize())
                .build();
    }

    // File Entity -> FileDTO
    default FileDTO convertEntityToDto(File file) {
        return FileDTO.builder()
                .uuid(file.getUuid())
                .saveDir(file.getSaveDir())
                .fileName(file.getFileName())
                .fileType(file.getFileType())
                .bno(file.getBno())
                .fileSize(file.getFileSize())
                .regAt(file.getRegAt())
                .modAt(file.getModAt())
                .build();
    }

    Long insert(BoardDTO boardDTO);

    Long insert(BoardFileDTO boardFileDTO);

    // List<BoardDTO> getList();
    Page<BoardDTO> getList(int pageNo);

    //BoardDTO getDetail(Long bno);
    BoardFileDTO getDetail(Long bno);

    // Long modify(BoardDTO boardDTO);
    Long modify(BoardFileDTO boardFileDTO);

    void delete(Long bno);

    long fileDelete(String uuid);

    FileDTO getFile(String uuid);

    boolean checkPassword(Long bno, String password);
}
