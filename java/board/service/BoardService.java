package board.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import board.entity.Board;
import board.repository.BoardRepository;
import comment.repository.CommentRepository;

@Service
public class BoardService {
	private static final int BLOCK_PAGE=15;		//한페이지에 나오는 최대 게시물 수
	
	@Autowired
	private BoardRepository br;
	@Autowired
	private CommentRepository cr;
	
	//게시글리스트 불러오기
	public Page<Board> getBoardList(int page) {
		Pageable pageable=PageRequest.of(page-1,BLOCK_PAGE,Sort.by("num").descending());
		return br.findAll(pageable);
	}
	
	//게시글 찾기 서비스
	public Page<Board> search_Board(String Condition,String Keyword,int page){
		Pageable pageable=PageRequest.of(page-1, BLOCK_PAGE);
		if(Condition.equals("all")) {
			return br.findBytitleContainingOrContentContainingOrderByNumDesc(Keyword, Keyword,pageable);
		}else if(Condition.equals("title")) {
			return br.findBytitleContainingOrderByNumDesc(Keyword,pageable);
		}else if(Condition.equals("content")) {
			return br.findBycontentContainingOrderByNumDesc(Keyword,pageable);
		}else {
			return null;
		}
	}
	
	//게시글 페이지수
	public long count_page() {
		double count=(double)br.count();
		int page=(int)Math.ceil(count/BLOCK_PAGE);
		
		return page;
	}
	
	//게시글 확인
	public Board getBoard(int num) {
		Board board=br.findBynum(num);
		board.setRead_cnt(board.getRead_cnt()+1);
		br.save(board);
		return br.findBynum(num);
	}
	
	//게시글 작성 서비스
	public void insert_board(Board board,HttpServletRequest req) {
		HttpSession session=req.getSession();
		String writer=(String) session.getAttribute("id");
		board.setName(writer);
		Date date=new Date();
		SimpleDateFormat format=new SimpleDateFormat("yyyy:MM:dd HH:mm:ss ");
		String time=format.format(date);
		try {
			board.setRegDate(format.parse(time));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		br.save(board);
	}
	
	//게시글 수정
	public void update_board(Board board) {
		Board brd=br.findBynum(board.getNum());
		Date date=new Date();
		SimpleDateFormat format=new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
		String time=format.format(date);
		
		try {
			brd.setRegDate(format.parse(time));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		brd.setTitle(board.getTitle());
		brd.setContent(board.getContent());
		brd.setHeading(board.getHeading());
		br.save(brd);
	}
	
	public void delete_board(int num) {
		
		br.deleteAllBynum(num);
		cr.deleteAllBypostNum(num);
	}
}
