package board.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import board.entity.Board;
import board.service.BoardService;
import comment.entity.Comment;
import comment.service.CommentService;

@Controller
public class BoardController {
	@Autowired
	BoardService bsr;
	@Autowired
	CommentService csr;
	
	//�Խñ� ���
	@RequestMapping("/BoardList")
	public String GetBoardList (Model model,@RequestParam(value="page",defaultValue="1") int page,HttpServletRequest req) {
		System.out.println("�Խ��Ǹ���Ʈ ��Ʈ�ѷ� ����");
		Page<Board> BoardList=bsr.getBoardList(page);
		model.addAttribute("boardList",BoardList);
		model.addAttribute("pageCnt",bsr.count_page());
		HttpSession session=req.getSession();
		String id=(String)session.getAttribute("id");
		if(id!=null) {
			return "board/BoardList";
		}else {
			return "redirect:login";
		}
		
	}
	
	//�Խñ� �˻�
	@RequestMapping("/search_board")
	public String searchBoard(Model model,@RequestParam(value="Conditon",required=false) String Condition,@RequestParam(value="Keyword",required=false) String Keyword,@RequestParam(value="page",defaultValue="1") int page,HttpServletRequest req) {
		HttpSession session=req.getSession();
		
		System.out.println("�Խñ� ã�� ����");		
		if(session.getAttribute("Condition") == null) {
			session.setAttribute("Condition", Condition);
		}else {
			if(Condition==null) {
				Condition=(String) session.getAttribute("Condition");
			}
		}
		
		if(session.getAttribute("Keyword")==null) {
			session.setAttribute("Keyword", Keyword);
		}else {
			if(Keyword==null) {
				Keyword=(String)session.getAttribute("Keyword");
			}
		}
		System.out.println(Condition+" "+Keyword);
		
		Page<Board> boardList=bsr.search_Board(Condition, Keyword, page);
		long size=boardList.getTotalElements()-(page*15);
		model.addAttribute("boardList", boardList);
		
		System.out.println(size);
		
		if(size<=0) {
			model.addAttribute("pageCnt",page);
		}else {
			model.addAttribute("pageCnt",page+1);
		}
		
		return "board/BoardSearchList";
	}
	
	//�Խñ�
	@RequestMapping("/getBoard")
	public String GetBoard(Model model,@RequestParam("num") int num) {
		System.out.println("�� Ȯ�� ��Ʈ�ѷ�");
		model.addAttribute("board",bsr.getBoard(num));
		model.addAttribute("comment",csr.get_comment(num));
		return "board/getBoard";
	}
	
	//�Խñ� �ۼ�â
	@RequestMapping("/insertBoard")
	public String InsertBoard(Model model) {
		System.out.println("�Խñ� �ۼ� ��Ʈ�ѷ�");
		return "board/insertBoard";
	}
	//�Խñ� ���
	@RequestMapping("/insert_service")
	public String insert_board(Model model,Board board,HttpServletRequest req) {
		System.out.println("�� ��� ����");
		bsr.insert_board(board,req);
		return "redirect:BoardList";
	}
	
	//�Խñ� ����
	@RequestMapping("/updateBoard")
	public String UpdateBoard(Model model,@RequestParam("num") int num) {
		System.out.println("�Խñ� ������Ʈ");
		model.addAttribute("board",bsr.getBoard(num));
		return "board/updateBoard";
	}
	
	//�Խñ� ���� ����
	@RequestMapping("/update_service")
	public String update_board(Model model,Board board,@RequestParam("num") int num) {
		System.out.println("�Խñ� ���� ����");
		bsr.update_board(board);
		return "redirect:BoardList";
	}
		
	//�Խñ� ���� ����
	@RequestMapping("/deleteBoard")
	public String delete_board(Model model,@RequestParam("num") int num) {
		System.out.println("�Խñ� ���� ����");
		bsr.delete_board(num);
		return "redirect:BoardList";
	}
}
