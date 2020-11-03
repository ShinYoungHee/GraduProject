package afk.service;

import java.util.HashMap;
import java.util.List;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import afk.entity.Afk;
import afk.entity.AfkMap;
import afk.entity.AfkValue;
import afk.repository.AfkRepository;

@Service
public class AfkService {
	@Autowired
	AfkRepository afkrepository;
	
	public AfkValue calculator(AfkMap afk) {				//����Ͽ� �������� ���� ������ ��ȯ �մϴ�.
		List<Afk> afkList=afkrepository.findAll();
		AfkValue afkvalue = new AfkValue();
		
		double totalExp=0;
		double totalGold=0;
		double totalEssence=0;
		double maxTotal=0;
		int option; //�ִ� �ð��� ���ϱ� ���� �ɼǰ��Դϴ�. �ɼ��� (1�̶�� ����ġ 2��� ��� 3�̶�� ������ ����)�� �ִ�ð�
		
		int weeks=0;  //���������� �����ϴ� �� �Դϴ�.
		int days=0;   //���������� �����ϴ� �ϼ� �Դϴ�.
		int e_hour=0;	// ���������� �����ϴ� ����ġ �ɸ��� �ð�
		int e_min=0;	//���������� �����ϴ� ����ġ �ɸ��� ��
		int e_sec=0;	//���������� �����ϴ� ����ġ �ɸ��� ��
		int g_hour=0;	// ���������� �����ϴ� ��� �ɸ��� �ð�
		int g_min=0;	//���������� �����ϴ� ��� �ɸ��� ��
		int g_sec=0;	//���������� �����ϴ� ��� �ɸ��� ��
		int ec_hour=0;	// ���������� �����ϴ� ������ ���� �ɸ��� �ð�
		int ec_min=0;	//���������� �����ϴ� ������ ���� �ɸ��� ��
		int ec_sec=0;	//���������� �����ϴ� ������ ���� �ɸ��� ��
		
		int s_lv=Integer.parseInt(afk.getStart())-1;
		int e_lv=Integer.parseInt(afk.getEnd())-1;
		int n_exp=Integer.parseInt(afk.getN_exp());			//���� ����ġ
		int n_gold=Integer.parseInt(afk.getN_gold());		//���� ���
		int n_essence=Integer.parseInt(afk.getN_essence());	//���� ������ ����
		int m_exp=Integer.parseInt(afk.getM_exp());			//�д� ����ġ ȹ�淮
		int m_gold=Integer.parseInt(afk.getM_gold());		//�д� ��� ȹ�淮
		int h_essence=Integer.parseInt(afk.getH_essence());	//�ð��� ������ ���� ȹ�淮
		
		String t_exp;		//������ ���� �����ϴ� ����ġ �ð�
		String t_gold;		//���������� �����ϴ� ��� �ð�
		String t_essence;	//���������� �����ϴ� ����ġ ������ ����
		String max_time = null;  //���������� �����ϴ� �ִ� �ð�
		
		while(s_lv<e_lv) {
			totalExp+=afkList.get(s_lv).getExp();
			totalGold+=afkList.get(s_lv).getGold();
			totalEssence+=afkList.get(s_lv).getEssence();
			s_lv++;
		}
		
		if(s_lv>=240) {
			totalExp=totalExp*10;
			totalGold=totalGold*10;
		}
		
		System.out.println(n_exp+" "+n_gold+" "+n_essence);
		totalExp=this.subtract_resource(totalExp, n_exp);
		totalGold=this.subtract_resource(totalGold, n_gold);
		totalEssence=this.subtract_resource(totalEssence, n_essence);
		
		if((totalExp/m_exp)>(totalGold/m_gold)) {
			if((totalExp/m_exp)>((totalEssence/h_essence)*60)) {
				maxTotal=totalExp;
				option=1;
			}else {
				maxTotal=totalEssence;
				option=3;
			}
		}else {
			if((totalGold/m_gold)>((totalEssence/h_essence)*60)) {
				maxTotal=totalGold;
				option=2;
			}else {
				maxTotal=totalEssence;
				option=3;
			}
		}
		
		System.out.println("totalExp : "+totalExp);
		System.out.println("totalGold:" +totalGold);
		System.out.println("totalEssence: "+totalEssence);
		
		//�ؿ� �������� �д� �ڿ� ȹ�淮�� 0�� ��� total���� ��� 0���� ����Ƿ� 0�̵Ǳ����� �̸� ���� �����صӴϴ�.
		afkvalue.setTotalExp((int)totalExp);
		afkvalue.setTotalGold((int)totalGold);
		afkvalue.setTotalEssence((int)totalEssence);
		
		try {
			if(totalExp!=0) {
				e_min=(int)(totalExp)/m_exp;
				e_sec=(int)(((totalExp/m_exp)-(int)(totalExp/m_exp))*60);
			}
		
			if(totalGold!=0) {
				g_min=(int)(totalGold)/m_gold;
				g_sec=(int)(((totalGold/m_gold)-(int)(totalGold/m_gold))*60);
			}
			
			if(totalEssence!=0) {
				ec_hour=(int)(totalEssence)/h_essence;
				ec_min=(int)(((totalEssence%h_essence)/h_essence)*60);
			}

		}catch(ArithmeticException are) {
			if(m_exp==0) {
				totalExp=0;
			}
			if(m_gold==0) {
				totalGold=0;
			}
			if(h_essence==0) {
				totalEssence=0;
			}
		}
		
		if(e_min>=60) {
			e_hour=e_min/60;
			e_min=e_min%60;
		}else {
			e_hour=0;
		}
		
		if(g_min>=60) {
			g_hour=g_min/60;
			g_min=g_min%60;
		}else {
			g_hour=0;
		}
			
		t_exp=e_hour+":"+e_min+":"+e_sec;
		t_gold=g_hour+":"+g_min+":"+g_sec;
		t_essence=ec_hour+":"+ec_min+":"+ec_sec;
		
		System.out.println(option);
		
		if(option==1) {
			max_time=this.get_max_time(afkvalue, e_hour, e_min, e_sec,this.size_total(totalExp, m_exp));
		}else if(option==2) {
			max_time=this.get_max_time(afkvalue, g_hour, g_min, g_sec,this.size_total(totalGold, m_gold));
		}else if(option==3) {
			max_time=this.get_max_time(afkvalue, ec_hour, ec_min, ec_sec,this.essence_size(totalEssence, h_essence));
		}
			
		System.out.println("t_exp:"+t_exp);
		System.out.println("t_gold:"+t_gold);
		System.out.println("t_essence:"+t_essence);
		System.out.println("max_time:"+max_time);
		
		
		//��Ż ������ ������ �̸� �����Ͽ����ϴ�.
		afkvalue.setT_exp(t_exp);			//����ġ �ð�
		afkvalue.setT_gold(t_gold);			//��� �ð�
		afkvalue.setT_essence(t_essence);	//������ �ð�
		afkvalue.setMax_time(max_time);		//�ִ� �ð�
		
		return afkvalue;
	}
	
	public AfkValue Get_MaxTime(AfkMap afkmap) {
		AfkValue afkvalue=new AfkValue();
		int hour=afkmap.getMax_hour();
		int min=afkmap.getMax_min();
		int sec=afkmap.getMax_sec();
		int total=sec+(min*60)+(hour*60*60);
		
		afkvalue.setMax_time(get_max_time(afkvalue,hour,min,sec,this.size_total(total, 60)));
		return afkvalue;
	}
	
	//�ִ� �ð��� ����ϴ� �Լ� �Դϴ�.
	public String get_max_time(AfkValue afkvalue,int hour,int min,int sec,int option) {
		int days=0;
		int weeks=0;
				
		afkvalue.setMax_hour(hour);	//�ִ� �ð�
		afkvalue.setMax_min(min);	//�ִ� ��
		afkvalue.setMax_sec(sec);	//�ִ� �� ����  ���߿� �信�� js�� �̿��ؼ� ��ĥ ���Դϴ�.
		
		System.out.println(hour+" : "+" : "+min+" : "+sec);
		
		if(hour>=24) {
			days=hour/24;
			hour=hour%24;
			
			if(days>=7) {
				weeks=days/7;
				days=days%7;
			}
		}
		
		if(option==1) {
			return sec+"��";
		}else if(option==2) {
			return min+"�� "+sec+"��";
		}else if(option==3) {
			return hour+"�ð� "+min+"�� "+sec+"��";
		}else if(option==4) {
			return days+"�� "+hour+"�ð� "+min+"�� "+sec+"��";
		}else {
			return weeks+"�� "+days+"�� "+hour+"�ð� "+min+"�� "+sec+"��";
		}
		
	}
	
	//�ִ�ð��� ������ ����
	public int size_total(double total,int resource) {
		if(total<resource) {			//�ִ�ð��� �� ���� �� ��� 		
			return 1;
		}else if(total<resource*60) {	//�ִ�ð��� �� ���� �� ���		
			return 2;
		}else if(total<resource*60*24){	//�ִ�ð��� �ð� ���� �� ���		
			return 3;
		}else if(total<resource*60*24*7){	//�ִ�ð��� �� ���� �� ���	
			return 4;
		}else {								//�ִ�ð��� �� ���� �� ���
			return 5;
		}
	}
	
	//����� �ּҽð��� ���̱� ������ �ִ�ð��� ������ �� ���� �����Ͽ����ϴ�.
	public int essence_size(double total,int resource) {
		if(total<resource) {			  //����� �д��� �� ���
			return 2;
		}else if(total<resource*24) {		//����� �ð� ���� �� ���
			return 3;
		}else if(total<resource*24*7){		//����� �� ������ ���
			return 4;
		}else {								//����� �� ������ ���
			return 5;
		}
	}
	
	//�ʿ��� �ڿ����� ���� �ִ� �ڿ��� ���ϴ�.
	public double subtract_resource(double total,int resource) {
		if(total<resource) {
			total=0;
		}else {
			total-=resource;
		}
		
		return total;
	}
	
	public String trans_service(String src_text){
		String apikey="d1e60bc7cbc98a6fbabab92592aed476";
		 String translate=null;
		try {
			String text=URLEncoder.encode(src_text,"UTF-8");
			String postParams="src_lang=en&target_lang=kr&query="+text;
			String apiURL="https://dapi.kakao.com/v2/translation/translate?"+postParams;
			URL url=new URL(apiURL);
			HttpURLConnection con=(HttpURLConnection)url.openConnection();
			String userCredentials=apikey;
			String basicAuth="KakaoAK "+userCredentials;
			con.setRequestProperty("Authorization", basicAuth);
			con.setRequestMethod("GET");
			//con.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
			con.setRequestProperty("Content-Type","application/json");
			con.setRequestProperty("charset", "UTF-8");
			con.setUseCaches(false);
			con.setDoInput(true);
			con.setDoOutput(true);
			int responseCode=con.getResponseCode();
			System.out.println("responseCode>>"+responseCode);
			BufferedReader br;
			if(responseCode==200) {
				br=new BufferedReader(new InputStreamReader(con.getInputStream()));
			}else {
				br=new BufferedReader(new InputStreamReader(con.getErrorStream()));
			}
			String inputLine;
			StringBuffer res=new StringBuffer();
			while((inputLine=br.readLine())!=null){
				res.append(inputLine);
			}
			
			JSONParser parser=new JSONParser();
			Object obj=parser.parse(res.toString());
			JSONObject jsonObj = (JSONObject)obj;
			JSONArray jsonArr = (JSONArray) jsonObj.get("translated_text");
			translate=jsonArr.get(0).toString();

			br.close();
		}catch(Exception e) {
			System.out.println("���� �߻�");
			System.out.println(e);
		}
		return translate;
	}
}
	