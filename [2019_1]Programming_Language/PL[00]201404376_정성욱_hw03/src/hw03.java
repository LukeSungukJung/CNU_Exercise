import java.util.regex.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.io.*;




public class hw03{
	//��ū ǥ���ϴ� �Լ�
	public enum TokenType{   
		ID(3), INT(2);      
		private final int finalState;      
		TokenType(int finalState) {    
			this.finalState = finalState;   
			}  
	}
	
	//��ū ���� Ŭ����
	public static class Token {   
		public final TokenType type;   
		public final String lexme; 
	 
	  	Token(TokenType type, String lexme) {    
	  		this.type = type;    
	  		this.lexme = lexme;   
	  		} 
	 
	  	@Override   
		public String toString() {    
			return String.format("[%s: %s]", type.toString(), lexme);   
		}  
	}
	//ScannerŬ���� �̹������� �ٽ�
	static class Scanner{
	private int transM[][];  
	private String source;    
	private StringTokenizer st;

	public Scanner(String source) {   
		this.transM = new int[4][128];   
		this.source = source == null ? "" : source;   
		initTM();
	}
	//���¸� �ʱ�ȭ �ϴ� �Լ�
	private void initTM() { 
		transM = new int[4][128];
		// ���ǵ� ���� �̿��� ���´� ���� �˼����� ������ -1�� ����ŷ����
		for(int i=0;i<transM.length;i++){
			for(int j=0;j<transM[i].length;j++)
				transM[i][j] = -1;
		}
		//0-9�� ���� A-z�� ���� - �� 1�� ���� �ʱ���� 0�� ���� ���ݴϴ�.
		//�ƽ�Ű�ڵ�ǥ�� ����
		for(int dec =48;dec<=57;dec++) {
			transM[3][(char)dec] = 3;
			transM[2][(char)dec] = 2;
			transM[1][(char)dec] = 2;
			transM[0][(char)dec] = 2;
		}
			
			transM[0]['-'] = 1;	
		for(int dec =65;dec<123;dec++) {
			transM[3][(char)dec] = 3;
			//transM[2][(char)dec] = 3;
			//transM[1][(char)dec] = 3;
			transM[0][(char)dec] = 3;
		}
		st = new StringTokenizer(this.source," ",true);
		
	}
	//���� ��ū�� �޾ƿ��� �Լ���
	private Token nextToken(){    
		int stateOld = 0, stateNew; 
		 
		  //��ū�� �� �ִ��� �˻�   
		  if(!st.hasMoreTokens()) return null; 
		 
		  //�� ���� ��ū�� ����   
		String temp = st.nextToken(); 
		//���� ���ڰ� ������ ��� �״�� ���� �ƹ� ���ڵ� �ƴϱ� ������..
		while(temp.equals(" "))
			temp = st.nextToken(); 
			
	
		Token result = null;
		for(int i = 0; i<temp.length();i++){  
			//���� ��ū�� ���ڸ� �ϳ����� �����ͼ�  ���µ��� �־ ����
			char check_temp  = temp.charAt(i);
			//startNew�� ����

			 //���ڿ��� ���ڸ� �ϳ��� ������ ������¿� TransM�� �̿��Ͽ� ���� ���¸� �Ǻ�      
			//�������¿� ���� ���� ���� �� ���� ���¸� ������.
			stateNew = transM[stateOld][check_temp];
			//���ǵ��� ���� ������ ��� ��������� ������.
			if(stateNew==-1) {
				 //���� �Էµ� ������ ���°� reject �̸� �����޼��� ��� �� return��      

				System.out.println("unknwon state! rejected");
				return null;
			}
			//������¸� ���� ���·� ����
			stateOld =stateNew;
		 //���� ���� ���¸� ���� ���·� ����   
		}
		//���� ������ ������ �� ���� ���� ��ū�� ����Ÿ�� ������ ������
		for (TokenType t : TokenType. values ()){
		//�����ͼ� ������ ���¿� ������ ����(��������)�� ����
		if(t.finalState == stateOld){     
		//������ ���°� ������ ���¿� �����ϴٸ�, Token�� ������.
		result = new Token(t, temp);     
		break;    }   
		}      
		return result;  
		
	}
	
	
		public List<Token> tokenize() { 
		//�Է����� ���� ��� token�� ���� 
		//nextToken()�̿��� �ĺ��� �� list�� �߰��� ��ȯ 
		//��ū�Ӽ��� ��̸���Ʈ�� ����
		List<Token> res_list = new ArrayList<Token>();
		// ���� ��ū�� res�� �ְ� res�� ���� �ƴϸ� ����Ʈ�� �߰��� �׸��� �ٽ�
		//res�� ���� null�� ���Ë�����.
		Token res =this.nextToken();
		while(res!=null) {
			res_list.add(res);
			res =this.nextToken();
		}
		
		//while�������� res list ������
		return res_list;  
		} 
	}		
	public static void main(String[]args) throws Exception {
		//������ �Է¹���
		FileReader fr = new FileReader("C:\\Users\\su_j6\\Desktop\\������\\PL[00]201404376������_hw_03\\as03.txt");   
		//���� 
		BufferedReader br = new BufferedReader(fr);   
		// ������ �о��
		String source = br.readLine();     
		//��ĳ�ʷ� ���� �Ŀ� ��ũ�������� �� ����Ʈ�� ����
		Scanner s = new Scanner(source);   
		List<Token> tokens = s.tokenize();   
		//����(�������� �������� ������ ������)�� �Է� ���� ���
		System.out.println(tokens);
	}
}