
import java.util.HashMap;
import java.util.Map;

import lexer.TokenType;


public class FunctionNode extends Node{
	//���⼭ ó���ؾ��� Ÿ�Ե鿡 ���� enum���� ���Ǹ� �� ���Դϴ�
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
		//��ūŸ�Կ� ���� ��� Ÿ���� �ؽ������� ���� ¦������ ���Ŀ� ���� ����
		private static Map<TokenType, FunctionType> fromTokenType = new HashMap<TokenType, FunctionType>();              
		
		//���������� MAP�� ¦��� �ϳ��� �߰��մϴ�. enum�� ���ư��鼭 enum�� ��ūŸ���� �� enum��ü�� ¦������
		static {          
			for (FunctionType fType : FunctionType.values()){             
			fromTokenType.put(fType.tokenType(), fType);          
			}       
		} 
		// Map�� �� �ִ� ¦������ ��(Ʃ��?)�� �����մϴ�.(��ū Ÿ���� ��ġ�ϴ� ���� ������)
		static FunctionType getFuncType(TokenType tType){         
			return fromTokenType.get(tType);       
			}              
		abstract TokenType tokenType();           
		}    
	 public FunctionType value;     
    @Override    
    //FunctionType value�� ���� name ��Ʈ������ ������.
    public String toString(){       
    	return value.name();
    	}   
    //��ūŸ���� �Է¹޾� FunctionType ���·� �ٲ��ִ� �Լ��� ȣ���ؼ� value�� ������ ftype�� �־��ݴϴ�.
    //�� �״�� value���� �Լ���
	public void setValue(TokenType tType) {       
		FunctionType ftype = FunctionType.getFuncType(tType);
		value = ftype;
		}
}
