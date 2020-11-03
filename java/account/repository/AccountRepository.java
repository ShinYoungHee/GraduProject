package account.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import account.entity.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account,String> {
	public Account findByid(String id);				//�α��� ,ȸ������ �ߺ�üũ ���
	public Account findByphone(String phone);		//���̵� ã��,ȸ������ �ߺ�üũ ���
	public Account findByemail(String email);		//���̵� ã��,ȸ������ �ߺ�üũ ���
}
