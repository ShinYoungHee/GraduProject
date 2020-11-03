package account.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import account.entity.Account;
import account.service.AccountService;

@Controller
public class AccountController {
	@Autowired
	AccountService as;
	//�α��� ������
	@RequestMapping("login")
	public String Login(Model model,Account account,HttpServletRequest req) {
		System.out.println("�α��� ��Ʈ�ѷ�");
		String result=as.login_service(account,req);
		System.out.println(result);
		HttpSession session=req.getSession();
		String id=(String)session.getAttribute("id");
		if(id==null) {
		if(result.equals("YES")) {
				return "redirect:BoardList";
			}else {
				return "account/login";
			}
		}else {
			return "redirect:BoardList";
		}
	}
	
	//���̵� ã�� ������
	@RequestMapping("/id_found")
	public String id_found(Model model,Account account) {
		System.out.println("���̵� ã�� ��Ʈ�ѷ�");
		return "account/id_found";
	}
	
	//�̸��Ϸ� ���̵� ã��
	@RequestMapping("/id_found_email")
	public String id_found_email(Model model,Account account) {
		System.out.println("�̸��Ϸ� ���̵� ã��");
		String id=as.id_found_email(account);
		model.addAttribute("user_id",id);
		return "account/id_result";
	}
	//�޴������� ���̵� ã��
	@RequestMapping("/id_found_phone")
	public String id_found_phone(Model model,Account account) {
		System.out.println("�޴�����ȣ�� ���̵� ã��");
		String id=as.id_found_phone(account);
		model.addAttribute("user_id",id);
		return "account/id_result";
	}
	//��й�ȣ ã�� ������
	@RequestMapping("/pw_found")
	public String pw_found(Model model) {
		System.out.println("��й�ȣ ã�� ��Ʈ�ѷ�");
		return "account/pw_found";
	}
	
	//��й�ȣ ã�� ����
	@RequestMapping("/pwd_service")
	public String pwd_find(Model model,Account account) {
		System.out.println("��й�ȣ ã�� ����");
		String result=as.pw_found_service(account);
		model.addAttribute("pw",result);
		return "account/pwd_result";
	}
	//ȸ������ ������
	@RequestMapping("/regist")
	public String regist(Model model) {
		System.out.println("ȸ������ ��Ʈ�ѷ�");
		return "account/regist";
	}
	
	//ȸ������ ����
	@RequestMapping("/regist_service")
	public String regist_service(Model model,Account act) {
		as.regist_service(act);
		return "account/login";
	}
	
	//���̵� �ߺ�üũ
	@RequestMapping("idCheck")
	public @ResponseBody String id_check(Model model,Account account) {
		System.out.println("���̵� �ߺ� üũ");
		String str=as.id_check(account);
		System.out.println(str);
		return str;
	}
	//�̸��� �ߺ�üũ
	@RequestMapping("emailCheck")
	public @ResponseBody String email_check(Model model,Account account) {
		System.out.println("�̸��� �ߺ� üũ");
		String str=as.email_check(account);
		System.out.println(str);
		return str;
	}
	
	//�޴�����ȣ �ߺ�üũ
	@RequestMapping("phoneCheck")
	public @ResponseBody String phone_check(Model model,Account account) {
		System.out.println("�޴��� �ߺ� üũ");
		String str=as.phone_check(account);
		System.out.println(str);
		return str;
	}
	
	//�α׾ƿ�
	@RequestMapping("/logOut")
	public String logOut(Model model,HttpServletRequest req) {
		HttpSession session=req.getSession();
		session.invalidate();
		return "redirect:login";
	}
	
	
}
