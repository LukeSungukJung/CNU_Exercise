import ast.*;
import compile.*;
import static java.lang.System.out;


public class PL_hw_05{
	//�ƽ� �ٸ���
	//�ִ밪�� �����ϵ��� �ۼ�  //value�� next �� �� ū ���� ����  
	public static int max(Node node) {
		//node�� � ������ Ŭ�������� Ȯ���ϰ� List Ŭ������ ��� if�� ���η� ���� ó����
		if(node.getClass().toString().equals("class ast.ListNode")) {
			
			// node ���� ���� null�� �ƴ� ��� 
			if(((ListNode)node).value!=null)
				// max�� ���ȣ�� �ϸ鼭 value���� ListNode�� ����ȯ�� �Ѵ�.
				return max(((ListNode)node).value);
			//�������� ���� ��尡 null�� �ƴ� ��쿡 ���ǹ� ����
			else if((ListNode)node.getNext()!=null)
				//max�� ��� ȣ�� �ϵ�, Node�� ����ȯ�� ��Ų���� ���� ��尪�� �־���  
				return max(((Node)node).getNext());
			//Next�� ���� ���� null�̰�.. �� �ƹ��͵� �������� -������ ����
			else return (int)Double.NEGATIVE_INFINITY;

		
	//classŸ���� ListNode�� �ƴ϶� IntNode�� ��� 
	}else {
			//node�� ������ null�� �ƴ� ���
			if(node.getNext()!=null)
				//���� ����� ���� ���� ����� ���� Math �޼ҵ带 ����Ͽ� ���Ͽ�, �����ϴ� ������ �մϴ�.
				//���� �����ϴ� max�ʹ� �ٸ� ���� ���߿� ū�� �����ϴ� ����.
				return Math.max(((IntNode)node).value, max(node.getNext()));
			}
			//���� ����� ���� �����մϴ�.(���� ����� ��츦 ó���ϱ� ����.)
			return ((IntNode)node).value;
	}
	
	//��� value�� ������ ��ȯ  //value�� next�� �� ���� �����ϸ��
	public static int sum(Node node) { 
		//��尡 null�� ��� 0�� ������.
		if(node==null) return 0;
		//���� ����� Ŭ���� Ÿ���� ListNode���� Ȯ��
		if(node.getClass().toString().equals("class ast.ListNode")) {
				//����Լ� sum�� �����ϵ� ������ ListNode�� ���� ����� ���ϰ��� ���� ���·� �θ�.
				//+ ������ �����ؾ� �ϹǷ�
				return sum(((ListNode)node).value)+sum((node.getNext()));
		//���� ����� Ŭ���� Ÿ���� IntNode��
		}else{
			//next�� ���� null�� �ƴ϶��
			if(node.getNext()!=null) {
				int  current = ((IntNode)node).value;
				//current�� ���簪�� ������ current + sum����Լ� ȣ���ϰ� �Ű������� next�� �־���
				return current+sum((node.getNext()));
			}
			//���� ����� ��� value�� ����
			return ((IntNode)node).value;
		}			 
	} 
	
public static void main(String[] args) {  
		Node node = TreeFactory.createtTree("( ( 3 ( ( 10 ) ) 6 ) 4 1 ( ) -2 ( ) )");  
		//Node testnode = TreeFactory.createtTree("3 (4) 4 3"); 
		//input�� ���
		out.println("input:"+ "( ( 3 ( ( 10 ) ) 6 ) 4 1 ( ) -2 ( ) )");
		//������ ���� node�� max�Լ��� ���� �� ���
		out.println("max value:"+max(node));
		//������ ���� node�� sum�Լ��� ���� �� ���
		out.println("sum value:"+sum(node));
		//���� ����� ����ϵ��� �ۼ�  
	}

}