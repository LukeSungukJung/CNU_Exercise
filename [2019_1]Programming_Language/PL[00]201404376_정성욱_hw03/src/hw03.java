import java.util.regex.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.io.*;




public class hw03{
	//토큰 표현하는 함수
	public enum TokenType{   
		ID(3), INT(2);      
		private final int finalState;      
		TokenType(int finalState) {    
			this.finalState = finalState;   
			}  
	}
	
	//토큰 정적 클래스
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
	//Scanner클래스 이번과제의 핵심
	static class Scanner{
	private int transM[][];  
	private String source;    
	private StringTokenizer st;

	public Scanner(String source) {   
		this.transM = new int[4][128];   
		this.source = source == null ? "" : source;   
		initTM();
	}
	//상태를 초기화 하는 함수
	private void initTM() { 
		transM = new int[4][128];
		// 정의된 상태 이외의 상태는 전부 알수없기 때문에 -1로 마스킹해줌
		for(int i=0;i<transM.length;i++){
			for(int j=0;j<transM[i].length;j++)
				transM[i][j] = -1;
		}
		//0-9는 숫자 A-z는 문자 - 는 1의 상태 초기상태 0을 정의 해줍니다.
		//아스키코드표값 참고
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
	//다음 토큰을 받아오는 함수임
	private Token nextToken(){    
		int stateOld = 0, stateNew; 
		 
		  //토큰이 더 있는지 검사   
		  if(!st.hasMoreTokens()) return null; 
		 
		  //그 다음 토큰을 받음   
		String temp = st.nextToken(); 
		//다음 문자가 공백일 경우 그대로 진행 아무 문자도 아니기 때문에..
		while(temp.equals(" "))
			temp = st.nextToken(); 
			
	
		Token result = null;
		for(int i = 0; i<temp.length();i++){  
			//읽은 토큰의 문자를 하나씩을 가져와서  상태도에 넣어서 돌림
			char check_temp  = temp.charAt(i);
			//startNew가 받음

			 //문자열의 문자를 하나씩 가져와 현재상태와 TransM를 이용하여 다음 상태를 판별      
			//이전상태와 현재 값을 넣은 후 다음 상태를 예상함.
			stateNew = transM[stateOld][check_temp];
			//정의되지 않은 상태일 경우 에러출력후 리턴함.
			if(stateNew==-1) {
				 //만약 입력된 문자의 상태가 reject 이면 에러메세지 출력 후 return함      

				System.out.println("unknwon state! rejected");
				return null;
			}
			//현재상태를 이전 상태로 넣음
			stateOld =stateNew;
		 //새로 얻은 상태를 현재 상태로 저장   
		}
		//위의 루프가 끝났을 때 현재 읽은 토큰이 가진타입 값들을 가져옴
		for (TokenType t : TokenType. values ()){
		//가져와서 마지막 상태와 현재의 상태(최종상태)를 비교함
		if(t.finalState == stateOld){     
		//마지막 상태가 현재의 상태와 동일하다면, Token을 리턴함.
		result = new Token(t, temp);     
		break;    }   
		}      
		return result;  
		
	}
	
	
		public List<Token> tokenize() { 
		//입력으로 들어온 모든 token에 대해 
		//nextToken()이용해 식별한 후 list에 추가해 반환 
		//토큰속성의 어레이리스트를 받음
		List<Token> res_list = new ArrayList<Token>();
		// 다음 토큰을 res에 넣고 res가 널이 아니면 리스트에 추가함 그리고 다시
		//res를 받음 null이 나올떄까지.
		Token res =this.nextToken();
		while(res!=null) {
			res_list.add(res);
			res =this.nextToken();
		}
		
		//while문끝나고 res list 리턴함
		return res_list;  
		} 
	}		
	public static void main(String[]args) throws Exception {
		//파일을 입력받음
		FileReader fr = new FileReader("C:\\Users\\su_j6\\Desktop\\프개언\\PL[00]201404376정성욱_hw_03\\as03.txt");   
		//버퍼 
		BufferedReader br = new BufferedReader(fr);   
		// 한줄을 읽어옴
		String source = br.readLine();     
		//스캐너로 읽은 후에 토크나이즈한 후 리스트에 저장
		Scanner s = new Scanner(source);   
		List<Token> tokens = s.tokenize();   
		//상태(문자인지 숫자인지 에러가 났는지)와 입력 값을 출력
		System.out.println(tokens);
	}
}