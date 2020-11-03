package account.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import account.entity.Account;
import account.repository.AccountRepository;

@Service
public class AccountService {
	@Autowired
	AccountRepository ar;
	
	//�α��� ����
	public String login_service(Account act,HttpServletRequest req) {
		Account account=ar.findByid(act.getId());
		if(account!=null) {
			if(account.getPwd().equals(act.getPwd())) {
				HttpSession session=req.getSession();
				session.setAttribute("id", account.getId());
				return "YES"; 
			}
		}
		return "NO";
		
	}
	
	//���̵� �̸��Ϸ� ã�� ����
	public String id_found_email(Account act) {
		Account account=ar.findByemail(act.getEmail());
		if(account!=null) {
			if(account.getName().equals(act.getName())) {
				return account.getId();
			}else {
				return "������ �ٽ� Ȯ���� �ּ���.";
			}
		}else {
			return "���̵� �������� �ʽ��ϴ�.";
		}
	}
	
	//���̵� �޴��� ��ȣ�� ã�� ����
	public String id_found_phone(Account act) {
		Account account=ar.findByphone(act.getPhone());
		if(account!=null) {
			if(account.getName().equals(act.getName())) {
				return account.getId();
			}else {
				return "������ �ٽ� Ȯ���� �ּ���.";
			}
		}else {
			return "���̵� �������� �ʽ��ϴ�.";
		}
	}
	
	//��й�ȣ ã�� ����
	public String pw_found_service(Account act) {
		Account account=ar.findByid(act.getId());
		
		if(account!=null) {
			if(account.getName().equals(act.getName())&&account.getEmail().equals(act.getEmail())) {
				return account.getPwd();
			}else {
				return "�Է� ������ Ʋ���ϴ�.";
			}
		}else {
			return "���̵� �������� �ʽ��ϴ�.";
		}
	}
	
	//ȸ������ ����
	public void regist_service(Account act) {
		ar.save(act);
	}
	
	//���̵� �ߺ� üũ
	public String id_check(Account act) {
		Account account=ar.findByid(act.getId());
		if(account!=null) {
			return "NO";
		}else {
			return "YES";
		}
	}
	//�޴��� ��ȣ �ߺ� üũ
	public String phone_check(Account act) {
		Account account=ar.findByphone(act.getPhone());
		if(account!=null) {
			System.out.println(account.getPhone());
			return "NO";
		}else {
			return "YES";
		}
	}
	//�̸��� �ߺ� üũ
	public String email_check(Account act) {
		Account account=ar.findByemail(act.getEmail());
		if(account!=null) {
			System.out.println(account.getEmail());
			return "NO";
		}else {
			return "YES";
		}
	}
}
