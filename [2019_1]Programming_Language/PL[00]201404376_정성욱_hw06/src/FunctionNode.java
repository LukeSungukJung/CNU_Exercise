
import java.util.HashMap;
import java.util.Map;

import lexer.TokenType;


public class FunctionNode extends Node{
	//여기서 처리해야할 타입들에 대해 enum으로 정의를 한 것입니다
	public enum FunctionType{
		ATOM_Q  { TokenType tokenType() { return TokenType.ATOM_Q;} },        
		CAR   { TokenType tokenType() { return TokenType.CAR;} },        
		CDR  { TokenType tokenType() { return TokenType.CDR;} },        
		COND    { TokenType tokenType() { return TokenType.COND;} },        
		CONS        { TokenType tokenType() { return TokenType.CONS;} },        
		DEFINE        { TokenType tokenType() { return TokenType.DEFINE;} },        
		EQ_Q        { TokenType tokenType() {return TokenType.EQ_Q;} },
		LAMBDA        { TokenType tokenType() { return TokenType.LAMBDA;} },        
		NOT        { TokenType tokenType() { return TokenType.NOT;} };
		//토큰타입에 대해 펑션 타입을 해쉬맵으로 만들어서 짝지어줄 형식에 대해 정의
		private static Map<TokenType, FunctionType> fromTokenType = new HashMap<TokenType, FunctionType>();              
		
		//실질적으로 MAP에 짝지어서 하나식 추가합니다. enum를 돌아가면서 enum의 토큰타입을 과 enum자체를 짝지어줌
		static {          
			for (FunctionType fType : FunctionType.values()){             
			fromTokenType.put(fType.tokenType(), fType);          
			}       
		} 
		// Map에 들어가 있는 짝지어진 쌍(튜플?)을 리턴합니다.(토큰 타입이 일치하는 쌍을 리턴함)
		static FunctionType getFuncType(TokenType tType){         
			return fromTokenType.get(tType);       
			}              
		abstract TokenType tokenType();           
		}    
	 public FunctionType value;     
    @Override    
    //FunctionType value가 가진 name 스트링값을 리턴함.
    public String toString(){       
    	return value.name();
    	}   
    //토큰타입을 입력받아 FunctionType 형태로 바꿔주는 함수를 호출해서 value에 변형된 ftype을 넣어줍니다.
    //말 그대로 value설정 함수임
	public void setValue(TokenType tType) {       
		FunctionType ftype = FunctionType.getFuncType(tType);
		value = ftype;
		}
}
