package com.ezen.boot_JPA.service;

import com.ezen.boot_JPA.dto.BoardDTO;
import com.ezen.boot_JPA.dto.BoardFileDTO;
import com.ezen.boot_JPA.dto.FileDTO;
import com.ezen.boot_JPA.entity.Board;
import com.ezen.boot_JPA.entity.File;
import com.ezen.boot_JPA.repository.BoardRepository;

import com.ezen.boot_JPA.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {
    private final BoardRepository boardRepository;
    private final FileRepository fileRepository;

    @Override
    public Long insert(BoardDTO boardDTO) {
        /* 내가 실제로 저장해야 할 객체 : Board
         * save() : insert 후 저장 객체의 id를 리턴
          - save() 안에 Entity 객체를 파라미터로 전송 */

        return boardRepository.save(convertDtoToEntity(boardDTO)).getBno();
    }

    @Transactional
    @Override
    public Long insert(BoardFileDTO boardFileDTO) {
        long bno = insert(boardFileDTO.getBoardDTO());

        if(bno > 0 && boardFileDTO.getFileDTOList() != null) {
            for (FileDTO fileDTO : boardFileDTO.getFileDTOList()) {
                fileDTO.setBno(bno);
                bno = fileRepository.save(convertDtoToEntity(fileDTO)).getBno();
            }
        }

        return bno;
    }

//    @Override
//    public List<BoardDTO> getList(int pageNo) {
//        /* 컨트롤러에서 보내야 하는 리턴은 List<BoardDTO>
//         * DB에서 가져오는 리턴은 List<Board> -> BoardDTO 객체로 변환
//         * findAll() = select *
//         * 정렬 : Sort.by(Sort.Direction.DESC, "정렬 기준 칼럼명") */
//
//        List<Board> boardList = boardRepository.findAll(Sort.by(Sort.Direction.DESC, "bno"));
//
//        List<BoardDTO> boardDTOList = new ArrayList<>();
//        for (Board board : boardList) {
//            boardDTOList.add(convertEntityToDto(board));
//        }
//
//        return boardList.stream().map(this::convertEntityToDto).toList();
//    }
    @Override
    public Page<BoardDTO> getList(int pageNo) {
        // pageNo = 0부터 시작
        // 0 => limit 0, 10 / 1 => limit 10, 10
        Pageable pageable = PageRequest.of(pageNo, 10, Sort.by("bno").descending());
        Page<Board> list = boardRepository.findAll(pageable);

        Page<BoardDTO> boardDTOList = list.map(b -> convertEntityToDto(b));

        return boardDTOList;
    }

//    @Override
//    public BoardDTO getDetail(Long bno) {
//        /* findById : 아이디(pk)를 주고 해당 객체를 리턴
//          - findById의 리턴 타입 Optional<Board> 타입으로 리턴
//
//         * Optional<T> : nullPointException이 발생하지 않도록 도와줌
//          - Optional.isEmpty() : null일 경우 확인 가능 true / false
//          - Optional.isPresent() : 값이 있는지 확인 true / false
//          - Optional.get() : 객체 가져오기 */
//
//        Optional<Board> optional = boardRepository.findById(bno);
//
//        if(optional.isPresent()) {
//            BoardDTO boardDTO = convertEntityToDto(optional.get());
//
//            return boardDTO;
//        }
//
//        return null;
//    }

    @Override
    public BoardFileDTO getDetail(Long bno) {
        // file bno에 일치하는 모든 파일 리스트를 가져오기
        Optional<Board> optional = boardRepository.findById(bno);

        if(optional.isPresent()) {
            BoardDTO boardDTO = convertEntityToDto(optional.get());
            List<File> fileList = fileRepository.findByBno(bno);
            List<FileDTO> fileDTOList = fileList.stream().map(f -> convertEntityToDto(f)).toList();

            BoardFileDTO boardFileDTO = new BoardFileDTO(boardDTO, fileDTOList);
            log.info(">>> boardFileDTO : {}", boardFileDTO);

            return boardFileDTO;
        }

        return null;
    }

//    @Override
//    public Long modify(BoardDTO boardDTO) {
//        // jpa는 update X, 기존 객체를 가져와서 set 수정 후 다시 저장
//
//        Optional<Board> optional = boardRepository.findById(boardDTO.getBno());
//
//        if(optional.isPresent()) {
//            Board entity = optional.get();
//            entity.setTitle(boardDTO.getTitle());
//            entity.setContent(boardDTO.getContent());
//
//            // 다시 저장(기존 객체 덮어쓰기)
//            return boardRepository.save(entity).getBno();
//        }
//
//        return null;
//    }
    @Override
    public Long modify(BoardFileDTO boardFileDTO) {
//        Optional<Board> optional = boardRepository.findById(boardFileDTO.getBoardDTO().getBno());
//
//        if(optional.isPresent()) {
//            Board board = optional.get();
//            board.setTitle(boardFileDTO.getBoardDTO().getTitle());
//            board.setContent(boardFileDTO.getBoardDTO().getContent());
//
//            long bno = boardRepository.save(board).getBno();
//
//            if(boardFileDTO.getFileDTOList() != null) {
//                for (FileDTO fileDTO : boardFileDTO.getFileDTOList()) {
//                    fileDTO.setBno(bno);
//                    bno = fileRepository.save(convertDtoToEntity(fileDTO)).getBno();
//                }
//                return bno;
//            }
//        }
//        return 0L;

        long bno = insert(boardFileDTO);

        return bno;
    }

    @Override
    public void delete(Long bno) {
        boardRepository.deleteById(bno);
    }

    @Override
    public long fileDelete(String uuid) {
        Optional<File> optional = fileRepository.findById(uuid);

        if(optional.isPresent()) {
            fileRepository.deleteById(uuid);

            return optional.get().getBno();
        }

        return 0;
    }

    @Override
    public FileDTO getFile(String uuid) {
        Optional<File> optional = fileRepository.findById(uuid);

        if(optional.isPresent()) {
            FileDTO fileDTO = convertEntityToDto(optional.get());

            return fileDTO;
        }

        return null;
    }

    @Override
    public boolean checkPassword(Long bno, String inputPassword) {
        Optional<Board> optionalBoard = boardRepository.findById(bno); // 게시글 가져오기
        if (optionalBoard.isPresent()) {
            // 게시글 비밀번호와 입력받은 비밀번호 비교
            return optionalBoard.get().getPassword().equals(inputPassword);
        }
        return false; // 게시글이 없거나 비밀번호가 틀린 경우
    }

}
