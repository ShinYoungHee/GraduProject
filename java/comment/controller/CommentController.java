package comment.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import comment.entity.Comment;
import comment.service.CommentService;

@Controller
public class CommentController {
	@Autowired
	CommentService cs;
	
	@RequestMapping("insert_comment")
	public String insert_comment(Model model,@RequestParam("num") int num,Comment comment,HttpServletRequest req) {
		cs.insert_commentservice(num, comment,req);
		model.addAttribute("num",num);
		return "redirect:getBoard";
	}
	
}
