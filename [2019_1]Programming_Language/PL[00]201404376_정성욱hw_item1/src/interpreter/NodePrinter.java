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
	// ListNode, QuoteNode, Node�� ���� printNode �Լ��� ���� overload �������� �ۼ�
	//ListNode�� ���� List�� ��ȸ�ϸ� �� ��� ���Ŀ� ���� �´� �Լ��� ȣ���Ͽ� �Ű������� �Ѱ��ִ� �Լ�
	private void printList(ListNode listNode) {
		if (listNode == ListNode.EMPTYLIST) {
			return;
		}
		// ���� �κ��� �־��� ��� ���Ŀ� �°� �ڵ带 �ۼ��Ͻÿ�.
		
		// ���� ListNode�� �պκ��� Ŭ���� Ÿ���� �޾Ƴ��� ������ ���Ÿ�� car�� �����Ͽ� car�� ����
		Node car = listNode.car();
		//quote����� ��쿡 ���ǹ� ������
		if(car instanceof QuoteNode) {
			//Quote�� '�� quote�� ǥ���ɼ� �ִµ� �� �� apostrophe�� �����Ƿ�, '�� ��Ʈ�� ���ۿ� �־��ش�.
			
			//ListNode�� head�κ��� quote���� ĳ���� ���� quote�� print�ϴ� printNode ȣ���ϸ鼭 �Ű������� �־��ش�.
			printNode((QuoteNode)car);
		}
		else if(car instanceof ListNode){
			printNode((ListNode)listNode.car());
		}else{
			//�������� �׳� �Ϲ� ���� ��� �̹Ƿ� �ٽ� printNode�� �־���(Node�� ��� ó��)
			printNode((ValueNode)listNode.car());
		}
		//������ tail�� ListNode�̹Ƿ� �ٽ� �ڱ��ڽ��� ��� ȣ����
		printList((ListNode)listNode.cdr());
	
		}
	
	private void printNode(QuoteNode quoteNode) {
		if (quoteNode.nodeInside() == null)
			return;
		// ���� �κ��� �־��� ��� ���Ŀ� �°� �ڵ带 �ۼ��Ͻÿ�.
		//Value����� �ļ� �̶��  printNode�� �߰���
		sb.append('\'');
		if (quoteNode.nodeInside() instanceof ValueNode) {	
		printNode((ValueNode)quoteNode.nodeInside());
	}
		else {
			//ListNode��� '�� �ٿ� ListNode�� �ٽ� ��
			printNode((ListNode)quoteNode.nodeInside());
		}
			
		

		}
	private void printNode(Node node) {
		if (node == null)
			return;
		// ���� �κ��� �־��� ��� ���Ŀ� �°� �ڵ带 �ۼ��Ͻÿ�.
		
		//���� ����� Ŭ������ ListNode�ϰ��� �Ϲ� ����ϰ�� ������.
		//ListNode�ϰ��
		if(node instanceof ListNode){
			

			//�ϳ��� ����Ʈ �̹Ƿ�, ������ �˸��� (�� sb�� ���� �߰���
			if(!node.equals(root)) {
			sb.append('(');
			sb.append(' ');
			}
			//Ÿ���� ��� ����, ���������� ����Ʈ��尡 ��ĳ���� �Ѱ� �̹Ƿ�, �ٿ�ĳ������ ���Ŀ� ListNode�� Ž���ϱ����� printList�� ȣ���Ѵ�.
			printList((ListNode)node);
			//����Ʈ ����� Ž���� �������Ƿ� )�� �߰��Ͽ� �������� ����
			if(!node.equals(root)) {
			sb.append(')');
			sb.append(' ');
			}
		//�Ϲ����� ����� ���
		}else if(node instanceof ValueNode){
			//�ϳ��� INT, ID��� ���� ��� �̹Ƿ�, ���Ŀ� ���߾� []�� �����ְ� �߾ӿ� ndoe�� ���� stringȭ�Ͽ� ��Ʈ�����ۿ� �־��ش�.
			sb.append(node.toString());
			sb.append(" ");
		}
		}
	//void -> bool�� �����ؼ� ���ѷ��� �����ϵ��� �ٲ�
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