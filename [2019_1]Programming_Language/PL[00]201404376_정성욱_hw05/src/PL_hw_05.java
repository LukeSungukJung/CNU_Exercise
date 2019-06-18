import ast.*;
import compile.*;
import static java.lang.System.out;


public class PL_hw_05{
	//맥스 다만듬
	//최대값을 리턴하도록 작성  //value와 next 값 중 큰 값을 리턴  
	public static int max(Node node) {
		//node가 어떤 종류의 클래스인지 확인하고 List 클래스일 경우 if문 내부로 들어가서 처리함
		if(node.getClass().toString().equals("class ast.ListNode")) {
			
			// node 가진 값이 null이 아닐 경우 
			if(((ListNode)node).value!=null)
				// max를 재귀호출 하면서 value값을 ListNode로 형변환을 한다.
				return max(((ListNode)node).value);
			//현재노드의 다음 노드가 null이 아닐 경우에 조건문 진입
			else if((ListNode)node.getNext()!=null)
				//max를 재귀 호출 하되, Node로 형변환을 시킨다음 다음 노드값을 넣어줌  
				return max(((Node)node).getNext());
			//Next도 없고 값도 null이고.. 즉 아무것도 없을때는 -무한을 리턴
			else return (int)Double.NEGATIVE_INFINITY;

		
	//class타입이 ListNode가 아니라 IntNode일 경우 
	}else {
			//node의 다음이 null이 아닐 경우
			if(node.getNext()!=null)
				//현재 노드의 값과 다음 노드의 값을 Math 메소드를 사용하여 비교하여, 리턴하는 식으로 합니다.
				//현재 구현하는 max와는 다른 거임 둘중에 큰거 리턴하는 거임.
				return Math.max(((IntNode)node).value, max(node.getNext()));
			}
			//현재 노드의 값을 리턴합니다.(단일 노드일 경우를 처리하기 위함.)
			return ((IntNode)node).value;
	}
	
	//노드 value의 총합을 반환  //value와 next의 총 합을 리턴하면됨
	public static int sum(Node node) { 
		//노드가 null일 경우 0을 리턴함.
		if(node==null) return 0;
		//현재 노드의 클래스 타입이 ListNode인지 확인
		if(node.getClass().toString().equals("class ast.ListNode")) {
				//재귀함수 sum을 싱행하되 현재의 ListNode와 다음 노드의 리턴값을 더한 형태로 부름.
				//+ 연산을 수행해야 하므로
				return sum(((ListNode)node).value)+sum((node.getNext()));
		//현재 노드의 클래스 타입이 IntNode임
		}else{
			//next의 값이 null이 아니라면
			if(node.getNext()!=null) {
				int  current = ((IntNode)node).value;
				//current에 현재값을 저장후 current + sum재귀함수 호출하고 매개변수로 next값 넣어줌
				return current+sum((node.getNext()));
			}
			//단일 노드일 경우 value값 리턴
			return ((IntNode)node).value;
		}			 
	} 
	
public static void main(String[] args) {  
		Node node = TreeFactory.createtTree("( ( 3 ( ( 10 ) ) 6 ) 4 1 ( ) -2 ( ) )");  
		//Node testnode = TreeFactory.createtTree("3 (4) 4 3"); 
		//input값 출력
		out.println("input:"+ "( ( 3 ( ( 10 ) ) 6 ) 4 1 ( ) -2 ( ) )");
		//위에서 만든 node의 max함수값 받은 후 출력
		out.println("max value:"+max(node));
		//위에서 만든 node의 sum함수값 받은 후 출력
		out.println("sum value:"+sum(node));
		//이하 결과를 출력하도록 작성  
	}

}