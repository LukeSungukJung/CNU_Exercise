package interpreter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class NodePrinter {
	private final String OUTPUT_FILENAME = "output08.txt";
	private StringBuffer sb = new StringBuffer();
	private Node root;
	NodePrinter(Node root) {
		this.root = root;
		}
	// ListNode, QuoteNode, Node에 대한 printNode 함수를 각각 overload 형식으로 작성
	//ListNode의 내부 List를 순회하며 각 노드 형식에 따라 맞는 함수를 호출하여 매개변수로 넘겨주는 함수
	private void printList(ListNode listNode) {
		if (listNode == ListNode.EMPTYLIST) {
			return;
		}
		// 이후 부분을 주어진 출력 형식에 맞게 코드를 작성하시오.
		
		// 들어온 ListNode의 앞부분의 클래스 타입을 받아내는 과정임 노드타입 car을 정의하여 car을 받음
		Node car = listNode.car();
		//quote노드일 경우에 조건문 진입함
		if(car instanceof QuoteNode) {
			//Quote는 '와 quote로 표현될수 있는데 둘 다 apostrophe가 붙으므로, '를 스트링 버퍼에 넣어준다.
			
			//ListNode의 head부분을 quote노드로 캐스팅 한후 quote를 print하는 printNode 호출하면서 매개변수로 넣어준다.
			printNode((QuoteNode)car);
		}
		else if(car instanceof ListNode){
			printNode((ListNode)listNode.car());
		}else{
			//나머지는 그냥 일반 단일 노드 이므로 다시 printNode로 넣어줌(Node일 경우 처리)
			printNode((ValueNode)listNode.car());
		}
		//나머지 tail은 ListNode이므로 다시 자기자신을 재귀 호출함
		printList((ListNode)listNode.cdr());
	
		}
	
	private void printNode(QuoteNode quoteNode) {
		if (quoteNode.nodeInside() == null)
			return;
		// 이후 부분을 주어진 출력 형식에 맞게 코드를 작성하시오.
		//Value노드의 후손 이라면  printNode로 추가함
		sb.append('\'');
		if (quoteNode.nodeInside() instanceof ValueNode) {	
		printNode((ValueNode)quoteNode.nodeInside());
	}
		else {
			//ListNode라면 '를 붙여 ListNode로 다시 감
			printNode((ListNode)quoteNode.nodeInside());
		}
			
		

		}
	private void printNode(Node node) {
		if (node == null)
			return;
		// 이후 부분을 주어진 출력 형식에 맞게 코드를 작성하시오.
		
		//들어온 노드의 클래스가 ListNode일경우와 일반 노드일경우 구분함.
		//ListNode일경우
		if(node instanceof ListNode){
			

			//하나의 리스트 이므로, 시작을 알리는 (를 sb에 먼저 추가함
			if(!node.equals(root)) {
			sb.append('(');
			sb.append(' ');
			}
			//타입은 노드 지만, 실질적으로 리스트노드가 업캐스팅 한것 이므로, 다운캐스팅을 한후에 ListNode를 탐색하기위해 printList를 호출한다.
			printList((ListNode)node);
			//리스트 노드의 탐색이 끝났으므로 )를 추가하여 마무리를 지음
			if(!node.equals(root)) {
			sb.append(')');
			sb.append(' ');
			}
		//일반적인 노드일 경우
		}else if(node instanceof ValueNode){
			//하나의 INT, ID등등 단일 노드 이므로, 형식에 맞추어 []로 감싸주고 중앙에 ndoe의 값을 string화하여 스트링버퍼에 넣어준다.
			sb.append(node.toString());
			sb.append(" ");
		}
		}
	//void -> bool값 리턴해서 무한루프 조정하도록 바꿈
	public boolean prettyPrint() throws Exception {
		printNode(root);
		//try (FileWriter fw = new FileWriter(OUTPUT_FILENAME);
			//	PrintWriter pw = new PrintWriter(fw)) {
			
		//} catch (IOException e) {
		//	e.printStackTrace();
		//}
			if("".equals(sb.toString())) {
				return true;
			}else {
				System.out.print("...> ");System.out.println(sb.toString());
				return false;
			}
	 }
	}