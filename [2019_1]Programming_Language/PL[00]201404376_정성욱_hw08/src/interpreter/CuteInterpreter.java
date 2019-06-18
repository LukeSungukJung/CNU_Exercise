package interpreter;

import java.io.File;

public class CuteInterpreter {
	 public static void main(String[] args) {
	 ClassLoader cloader = ParserMain.class.getClassLoader();
	 File file = new File(cloader.getResource("interpreter/as08.txt").getFile());
	 CuteParser cuteParser = new CuteParser(file);
	 CuteInterpreter interpreter = new CuteInterpreter();
	 Node parseTree = cuteParser.parseExpr();
	 Node resultNode = interpreter.runExpr(parseTree);
	 NodePrinter nodePrinter = new NodePrinter(resultNode);
	 nodePrinter.prettyPrint();
	 }

	 private void errorLog(String err) {
	 System.out.println(err);
	 }

	 
	 public Node runExpr(Node rootExpr) {
	 if (rootExpr == null)
	 return null;
	 if (rootExpr instanceof IdNode)
	 return rootExpr;
	 else if (rootExpr instanceof IntNode)
	 return rootExpr;
	 else if (rootExpr instanceof BooleanNode)
	 return rootExpr;
	 else if (rootExpr instanceof ListNode)
	 return runList((ListNode) rootExpr);
	 else
	 errorLog("run Expr error");
	 return null;
	 }
	 
	 private Node runList(ListNode list) {
	 if (list.equals(ListNode.EMPTYLIST))
	 return list;
	 
	 if (list.car() instanceof FunctionNode) {
	 return runFunction((FunctionNode) list.car(), (ListNode)stripList(list.cdr())); 
	 }
	 if (list.car() instanceof BinaryOpNode) {
	 return runBinary(list);
	 }
	 
	 return list;
	 }
	 
	 private Node runFunction(FunctionNode operator, ListNode operand) {
		 switch (operator.funcType) {
	 
	 // CAR, CDR, CONS� ���� ���� ����
	 // CAR�� ��� ����
	 case CAR:
		 // ����Ʈ�� ù��°�� ���� Ȯ����
		 Node head = operand.car();
		 //( �Լ� �� ( ...) ) �� ��츦 ����Ѱ�
		 if(head instanceof FunctionNode) {
			 //���ȣ���ؼ� �ȿ��� ó����
			 Node res = runFunction((FunctionNode)head,((ListNode)operand.cdr().car()));
			 //( ...) �������� �����ٵ�  ...�� �о�� ...�ϼ��� ���� ... �ϰ�� ...�� 
			 head = ((ListNode)res).car();
			 //operand��  ������ �޺κп� ���� ó���ε� �� �κи� ���� �ǹǷ�  ���� ���� ���� ����
			 operand = (ListNode)res;
		 }
		//'( ... ) �ϰ�� ����
		 if(head instanceof QuoteNode) {
			 // ��Ʈ ��带 ���Ƽ� car �� ���θ���Ʈ ( ... )�� ��ȯ��
			 Node res =  ((ListNode)runQuote(operand)).car();
			 // ...�� ����Ʈ ���� ���� 
			 
			 if(res instanceof ListNode) {
				 //'( ... )�� ���·� ��� �ϹǷ� ��Ʈ��� �ϳ� ����
				 Node p = new QuoteNode((ListNode)res);
				 //�� ����Ʈ�� ���ļ� ��ȯ�Ѵ�
				 return ListNode.cons(p, ListNode.EMPTYLIST);
			 }
			// quote �Ⱥٿ��൵ �ǹǷ� �� ( ...)�� ��ȯ��
			 return res;
		 }
		// �ƴϸ� 'a 'b�� �̹Ƿ�  �� ��ȯ�� 
		 return head;
	//CDR�� ��� ������
	 case CDR:
		 // CDR (...) �� �Ŵϱ�
		 //�Ǿպκ� ���� �� 
		 head =  operand.car();
		 //...�� � �Լ� ( ...2 ) �ϰ�� �� ������
		 if(head instanceof FunctionNode) {
			 // � �Լ� ...2�� ���ڷ� �༭ �Լ� ����ѹ� ������
			 Node res = runFunction((FunctionNode)head,((ListNode)operand.cdr().car()));
			 // car�ϼ��� cdr�ϼ��� ������ ��·�� �װ���� ( ( ...) )�� �����ϼ��� (  a ) �ϼ��� ������ �⺻������ ��ȣ �ϳ��� �ļ� ������ �����Ƿ�
			 //ù���� ��ȣ �����ִ� �۾�����car�� ��
			 head = ((ListNode)res).car();
			 //���� �޺κ����� operand�� �־��� cdr �̰� �־��ش�
			 operand = (ListNode)res;
		 }

		 //������ ó���� head�� '( ...) �ϼ��� �ְ� �׳� '( ...)�ϼ��� �϶� if�� ����!
		 if(head instanceof QuoteNode) {
			 //'�� �޺κ� '( ...)���� (...)�� ���ؼ� quoteNodeó���ϴ� �Լ� ����
			 Node res =  ((ListNode)runQuote(operand)).cdr();
			 // ��¥ �޺κ� ���� ���̹Ƿ� ... �� ���°��̹Ƿ� �� ���� ���� ���Ѽ� �� '��� �ϳ� ����
			 Node p = new QuoteNode((ListNode)res);
			 //cons�� ������
			 return ListNode.cons(p, ListNode.EMPTYLIST);
		 }
		 //����Ʈ�� �� �Ͱ��� ����� �� ������ �׳� �޺κ� ���ҵ� ������.
		 return operand.cdr();
	//cons�� ��� ����!
	 case CONS:
		 //�ϴ� ���κ� ��
		 head =  operand.car();
		 //�����κе� ���� ����
		 ListNode tail = ((ListNode)((ListNode)operand.cdr().car()));
		 // ��� �κ��� a b 1 2 �� ���� ���� ���� ��� �� ����!
		 if(head instanceof ValueNode) {
			 	// ������ ��� �׳� ��ġ�⸸ �ϸ� �Ǳ� ������ �����κ� �׻� '�� �����Ƿ� (pdf���� list�� ��ģ�ٰ� ���ͼ� ������������ �ٴ´ٰ�)
			 	//Quote ������ ����Ʈ ������.
				 Node tail_res =  runQuote(tail);
				 //���� ����� ����Ʈ�� ���Ҹ� ���ļ�
				 Node res = ListNode.cons(head,(ListNode)tail_res);
				 Node p = new QuoteNode((ListNode)res);
				 //��ģ �κп� ���� �ٽ� �� ��Ʈ ��� �ϳ����� �ű⿡ �� ���� �߰��ؼ� ������.
				 return ListNode.cons(p, ListNode.EMPTYLIST);
		 //head�� ����Ʈ ����� ��� ����!		 
		 }else if(head instanceof ListNode){
			 //����� ù�κ��� ��
			 Node t_head = ((ListNode)head).car();
			 //����� ������ ��
			 ListNode head_b = ((ListNode)head).cdr();
			 //����� ù�κ��� �Լ��� ����!
			 if(t_head instanceof FunctionNode) {
				 //����� ù�κ��� �Ű������� �޺κ��� �����ϴ� ��� ����!
				 Node res = runFunction((FunctionNode)t_head,((ListNode)head_b.car()));
				 //�����κ� '�ɷ������Ƿ� �����ش�.
				 Node t_tail = runQuote(tail);
				 //������ ù�κа�
				 Node tail_h = ((ListNode)t_tail).car();
				 //�޺κ��� ����
				 ListNode tail_b = ((ListNode)t_tail).cdr();
				 //�׳� �Ϲ����� ����Ʈ �ϼ��� �����Ƿ� �ϴ� �־���
				 Node tail_res = t_tail;
				 //���� �Լ��� ���� �����̸� ��!
				 if(tail_h instanceof FunctionNode) { 
					 //�պκ� �Լ� �޺κ� �Ű����� ����!
					 tail_res =  runFunction((FunctionNode)tail_h,(ListNode)tail_b.car());
					 //�޺κ��� �׻� ����Ʈ�̹Ƿ� '�� �ٴµ� ���⼭ �̰� ����
					 tail_res = runQuote((ListNode)tail_res);}
				 // �Լ� �ִ� �����̸� ����� ������� �ƴϸ� �׳����� ����Ʈ�� ���κ� ��� ������
				 res = ListNode.cons(res,(ListNode)tail_res);
				//����Ʈ�� '�ٿ����ؼ� �ϳ� ��Ʈ ��常�����
				 Node p = new QuoteNode((ListNode)res);
				 //( )  �����ַ��� �� ����Ʈ �ϳ����� ���ļ� ������
				 return ListNode.cons(p, ListNode.EMPTYLIST);
			 }else {
			 //��¥ �׳� ����Ʈ�� �ִ� ��� ���� '���� �ٿ��� �����ִ� ������.
			 Node head_res = runQuote((ListNode)head);	 
			 Node tail_res =  runQuote(tail);
			 Node res =ListNode.cons(head_res,(ListNode)tail_res);
			 Node p = new QuoteNode((ListNode)res);
			 return ListNode.cons(p, ListNode.EMPTYLIST);
			 }
		 }
	 case EQ_Q:
		 //���� ���κ� ��
		 head = operand.car();
		 // ' ����
		 head =runQuote((ListNode)head);
		 //�޺κ� �о�ͼ� �Ȱ��� ' ����
		 tail = (ListNode)(operand.cdr()).car();
		 Node tail_ = runQuote(tail);
		 //�� �� �Ѵ� ��Ʈ������ ��ȯ��
		 String a = head.toString();
		 String b =tail_.toString();
		 //��Ʈ������ ���� ������ �׸��� ���� ��ü�� �����ϴ��� and���� ������
		 boolean res = a.equals(b) && head.equals(tail_);
		 // ���̸� booleannode true�� �����ϰ� �ƴϸ� ������ ������
		 return  res ?  BooleanNode.TRUE_NODE:BooleanNode.FALSE_NODE;
	 case NULL_Q:
		 //���κ� ������
		 head = operand.car();
		 //���κ��� '����̸�, ���� �������� �ִٸ�?
		 if(head instanceof QuoteNode) {
			// '����
			Node res_head = runQuote((ListNode)operand);
			//'�� ���� �ڰ�  ����� ���
			if((operand.cdr()).equals(ListNode.EMPTYLIST)) {
				//'�� ����Ʈ�� �պκе� �������� 
				if(res_head.equals(ListNode.EMPTYLIST)){
					//�� ��ü�� ����� ��� Ʈ�� �Ҹ���� ����.
					return BooleanNode.TRUE_NODE;
				}
			}
		 }
		 // ������ �������� ������ �ȿ� ���� �ִ� ���̹Ƿ� �� ������
		 return BooleanNode.FALSE_NODE;
	 case ATOM_Q:
		 //�պκ� �� ��� 
		 head = operand.car();
		 //head�� ��Ʈ �����
		 if(head instanceof QuoteNode) {
			 //'�� �´µ� emptylist��� true��ȯ
			 if(runQuote(operand).equals(ListNode.EMPTYLIST))
				 return BooleanNode.TRUE_NODE;
			 //' �´µ� ���� ���� ���� ���� true��ȯ
			 else if(runQuote(operand) instanceof ValueNode)
				 return BooleanNode.TRUE_NODE;
			 //�������� ������ 
				 return BooleanNode.FALSE_NODE;
				 
		 }
	 case NOT:
		 //�պκ� ����
		 head = operand.car();
		 //�׳� �Ҹ� ����Ͻÿ� 
		 if(head instanceof BooleanNode) {
			 //���� ����ͼ� �ݴ밪�� ������
			 boolean h_val = !((BooleanNode) head).value; 
			 if(h_val == true)
				 //�װ����� ���� �� �Ҹ� ��� ������
				 return BooleanNode.TRUE_NODE;
			 else
				 return BooleanNode.FALSE_NODE;
			 
			 }	
		 Node n_res;
		 Boolean n_res_v;
		 //�պκ��� ���̳ʸ� �����
		 if(head instanceof BinaryOpNode) {
			 //Binary ���굹�� > < = �ϼ� ������
			 n_res = runBinary(operand);
			 //��� ���� �Ҹ� ��尪�� !�� �ݴ븦 ��������
			 n_res_v = !((BooleanNode)n_res).value;
			 //�װ��� ����� ����  Ʈ�� �޽��� ������
			 if(n_res_v == false)
				 return BooleanNode.FALSE_NODE;
			 else
				 return BooleanNode.TRUE_NODE;
						 
		//���̳ʸ���� �ƴҽÿ� ��ǳ������ ���� ��
		 }else if(head instanceof FunctionNode){
			 //��ǳ�� �պκ��̶� ���۷����� �� �κ��� ��
			 n_res = runFunction((FunctionNode)head,operand.cdr());
			 n_res_v = !((BooleanNode)n_res).value;
			 //���� ���������� �� ������ͼ� �ݴ� �۾� ���ش㿡 
			 //�ݴ��� �� �����ؼ� �׿� �°� ��������.
			 if(n_res_v == true)
				 return BooleanNode.TRUE_NODE;
			 else
				 return BooleanNode.FALSE_NODE;
		 }else {
			 head = operand.car();
			 
		 }
		 //COND�� �ܿ� ����
	 case COND:
		 //���� ��ü�� �պκ��� ���
		 head = ((ListNode)operand.car());
		 // �պκ� ��� �κ��� ����� ������ ������
		 //��� �Ҹ����� Ʈ�������� �޽� ������� ������
		 Node h_cond_res;
		 //Ʈ�� �޽� �϶� ������ ���� ������.
		 Node h_cond_ans_res ;
		 //����� �պκ��� ������ head -> ( ?.....  ) �϶� ?�� ����� 
		 Node head_cond =((ListNode)head).car();
		 //������ ����� ? �� ����Ʈ ���� ������ ? ->  (.... )
		 if (head_cond instanceof ListNode) {
			 //?�� �޺κ��� ����� (?2 ...) ���� ...
			Node head_res = ((ListNode)head_cond).cdr();
			//?�� ù��° ��带 ����� 
			Node list_head = ((ListNode) head_cond).car();
			//? �� �޺κ� ���⼱ ?2 �κ��� �����
			Node list_tail = ((ListNode) head_cond).cdr();
			//�̰� ���� �Դٴ� ���� ����Ʈ�� � ������ ���ؼ� true false�� �����ؾߵǹǷ� Ư�� �����ϴ� �κ��� �ִٰ� ������
			//?2�� ���� �˻��� ��� ����� ��� ����!
			if(list_head instanceof FunctionNode) {
				//�̺κ��� ���� ��� �� #f #T ���ϳ��� ���� �κ���
				h_cond_res = runFunction((FunctionNode)list_head,(ListNode)list_tail);
				//���� h_cond_res�� ���� �����Ǵ� ������ ������ ������� ans�� 1�̰� cond_res�� #T��� ������ ���� ��� ����: (#T 1)  
				h_cond_ans_res = ((ListNode)head).cdr().car();
			}else {
				//?2�� ���� �˻��� ���̳ʸ� ���( < > = )�� ��� ����!
				//�̺κ��� ���� ��� �� #f #T ���ϳ��� ���� �κ���
				h_cond_res = runBinary((ListNode)head_cond);
				//���� h_cond_res�� ���� �����Ǵ� ������ ������ ������� ans�� 1�̰� cond_res�� #T��� ������ ���� ��� ����: (#T 1) 
				h_cond_ans_res = ((ListNode)head).cdr().car();			
			}
		 }else {
			 //�� ���� �ƴ� ��� �׳� ���� �� ���� ��� �̹Ƿ� ���� �״�� �־���
			 h_cond_res = head_cond;
			 //��͵� �׳� �ٷ� �޺κ� ���� �־���
			 h_cond_ans_res = ((ListNode)head).cdr().car();
		 }
		 
		 
		 //�����κ� ����Ʈ ��ȯ
		 tail = (ListNode)operand.cdr().car();
		 // �պκ� ���� �κ��� ����� ������ ������
		 //��� �Ҹ����� Ʈ�������� �޽� ������� ������		 
		 Node t_cond_res;
		 Node t_cond_ans_res ;
		 //Ʈ�� �޽� �϶� ������ ���� ������.
		 Node tail_cond =((ListNode)tail).car();
		 //����� �պκ��� ������ head -> ( ?.....  ) �϶� ?�� ����� 		 
		 //������ ����� ? �� ����Ʈ ���� ������ ? ->  (?2 .... )
		 if (tail_cond instanceof ListNode) {
			//?�� ù��° ��带 �����. �� (?2 ���� ��) 			 
			Node list_head = ((ListNode) tail_cond).car();
			//?�� �޺κ��� ����� (?2 ...) ���� ...
			Node list_tail = ((ListNode) tail_cond).cdr();
			// ?2�� ��� ���� ������
			if(list_head instanceof FunctionNode) {
				//t_cond_res�� ?2�� �Լ��� ...�� �Ű������� �� ������ ����� #T #F �� �ϳ��� ������
				t_cond_res = runFunction((FunctionNode)list_head,(ListNode)list_tail);
				//t_cond_res�� ?2�� �Լ��� ...�� �Ű������� �� ���꿡 �����Ǵ� ���� ������
				t_cond_ans_res = ((ListNode)tail).cdr().car();
			}else {
				//�� ���� �ƴ� ��� �׳� ���� �� ���� ��� �̹Ƿ� ���� �״�� �־���
				t_cond_res = runBinary((ListNode)tail_cond);
				//��͵� �׳� �ٷ� �޺κ� ���� �־��� �������� ���� �����Ǵ� ����.
				t_cond_ans_res = ((ListNode)tail).cdr().car();	
			}	
			//�޺κ��� car�κ���  ����Ʈ�� �ƴ� ���, �׳� ���� ��� �̹Ƿ� �״�� ����´�
		 }else {
			 //�״�� �� �־���
			 t_cond_res = tail_cond;
			 //�׿� ���� �Ǵ� �޺κе� �״�� �����ͼ� �־���
			 t_cond_ans_res = ((ListNode)tail).cdr().car();
		 }
		 //(a b) (c d)���� ������ a�� �ش��ϴ� �κ��� ��
		 Boolean head_right = ((BooleanNode)h_cond_res).value;
		 //(a b) (c d)���� ������ c�� �ش��ϴ� �κ��� ��
		 Boolean tail_right = ((BooleanNode)t_cond_res).value;
		 //a�� ���� ��� b�� ��ȯ
		 if(head_right)
			 return h_cond_ans_res;
		 //c�� ���� ��� d�� ��ȯ
		 else
			 return t_cond_ans_res;
		 
		 
		 
	 default:
	 break;
	 }
	 return null;
	 }
	 
	 
	 private Node stripList(ListNode node) {
	 if (node.car() instanceof ListNode && node.cdr() == ListNode.EMPTYLIST) {
		 Node listNode = node.car();
	 	return listNode;
	 } else { 
		 return node; 
		 }
	 }
	 
	 
	 private Node runBinary(ListNode list) {
	 BinaryOpNode operator = (BinaryOpNode) list.car();
	// ������������ �ʿ��� ���� �� �Լ� �۾� ����
	 //���̳ʸ�  ���� ��ü�� Ŀ�������ϰ� �׳� ���� Ŭ���� �ϳ� ����
	 class OPPMTD{
		 // ��Ģ������ Ŀ���� ������ a ,b �Լ� ���갪�� ����
	 public String PMTD(Integer a, Integer b,BinaryOpNode op){
		 int res =99;
		 //99�� �ʱ�ȭ ���ذ��̰� ������ ���ϵɸ� ����
		 // ���� Ÿ�Կ����� �׳� �������� ��Ʈ ���� �޾Ƽ� �����ϰ� ����ġ�� ����
		 switch (op.binType) {
		 case PLUS:
			 res=a.intValue()+b.intValue(); break;
		 case MINUS:
			 res=a.intValue()-b.intValue(); break;
		 case TIMES:
			 res=a.intValue()*b.intValue(); break;
		 case DIV:
			 res=a.intValue()/b.intValue(); break;
		 }
		 // ������ �����Լ��� ��Ʈ�������� ��ȯ����
		 return Integer.toString(res);
	 }
	 // LG GT EQ�� Ŀ���ϴ� �Լ��� ���� �Ű������� ����������� �����ϴ°� ����
	 public BooleanNode LGE(Integer a, Integer b,BinaryOpNode op) {
		 switch(op.binType) {
		 //<��� 
		 case LT:
			 //���� ��¥ < �� �� ���� �ƴϸ� ���� �� �Ҹ���带 ������.
			 if(a.intValue()<b.intValue())
				 return BooleanNode.TRUE_NODE;
			 else
				 return BooleanNode.FALSE_NODE;
		 //�����ڰ� = ��
		 case EQ: 
			//���� ��¥ ������ �� ���� �ƴϸ� ���� �� �Ҹ���带 ������.
			 if(a.intValue()==b.intValue())
				 return BooleanNode.TRUE_NODE;
			 else
				 return BooleanNode.FALSE_NODE;
		 //�����ڰ� >��
		 case GT:
			//���� ��¥ > �� �� ���� �ƴϸ� ���� �� �Ҹ���带 ������.
			 if(a.intValue()>b.intValue())
				 return BooleanNode.TRUE_NODE;
			 else
				 return BooleanNode.FALSE_NODE;
		default:
			return null;
	 }
	 }
	 }
	 //����Ʈ���� �պκ� ����
	 Node head = list.cdr().car();
	 IntNode a;
	 //��尡 ����Ʈ����
	 if(head instanceof ListNode) {
		 //����� ù��°�� �������� ����̸� ���ǹ� ��
		 if(((ListNode)head).car() instanceof FunctionNode) {
			 // �������� ���Դ� ����� ù�κ� ��ǳ�� ��
			 Node func = ((ListNode)head).car();
			 //�޺κ��� ����Ʈ�� �и��ؼ�
			 ListNode head_rest = (ListNode)((ListNode)head).cdr().car();
			 //����̹Ƿ� runfuntion ������ ��
			 a = (IntNode)runFunction((FunctionNode)func,head_rest);
			 }else
			 //�׷������� ���� ( + 2 3 ) �� �̷� ��� �̹Ƿ� �����Լ� ���ȣ���ؼ� �� �����
			 a= (IntNode)runBinary((ListNode)head);
	 // ����Ʈ ��尡 �ƴϸ� + 2 3 �̹Ƿ� �ι�°�� ù��° operand�� ��.
	 }else {
		a= (IntNode)list.cdr().car();
	 }
	 // + 1 2���� 2�� ��Ÿ���� �� �޺κ��� �޺κ��� ����Ŵ
	 ListNode tail = list.cdr().cdr();
	 //�� ���� ����Ʈ�� ù�κ��� ���� ��
	 Node tail_head = tail.car();
	 IntNode b;
	 // �� ù�κ��� ����Ʈ �����
	 if(tail_head instanceof ListNode) {
		 //�� ù�κ��� ��� ����� ��� ��!
		 if(((ListNode)tail_head).car() instanceof FunctionNode) {
			 //�պκ� �Լ� ����
			 Node func = ((ListNode)tail_head).car();
			 //�޺κ�  ( ... ) ���¸� ���ܵ� ...�� ���� ����������
			 ListNode tail_rest = (ListNode)((ListNode)tail_head).cdr().car();
			 // ��� �պκ� �� �Լ��� �޺κ� ����Ʈ�� �Ű������� �Լ� �� �����.(IntNode ������) �̰Ÿ� b�� �־��� 
			 b = (IntNode)runFunction((FunctionNode)func,tail_rest);
			 }
		 else
		// ��� �ƴϸ� �׳� ( + 2 1 ) �̷� ���̽��Ƿ� ����Լ� ȣ���ؼ� �� �������
		 b = (IntNode)runBinary((ListNode)tail_head);
	 }else {
		 //����Ʈ �ƴϸ� ���� ���̹Ƿ� �״�� b�� �־���
		 b= (IntNode)tail_head;
	 }
	 //�����Ҷ� ���� ���� Ŭ���� ������ �༭ �������
	 OPPMTD op = new OPPMTD();
	 
	 switch (operator.binType) {
	 // +,-,/ � ���� ���̳ʸ� ���� ���� ����
	 case PLUS:
	 case MINUS:
	 case TIMES:
	 case DIV:
		 // IntNode�� �����ؾ��ϹǷ� �װ��� �����ϱ����� �Ű������� �����ϱ����� ��Ʈ������ �޴´�  ��Ģ���꿡 ���� ����� ��
		 String res_r = op.PMTD(a.getValue(),b.getValue(),operator);
		 //��Ʈ ��� ���� ������
		 IntNode res = new IntNode(res_r);
		 //System.out.println(res);
		 return res;
	 case LT:
	 case GT:
	 case EQ:
		 //������ �� ó�������Ƿ� Integer�� �޾Ƽ�  ���� �ְ� ������ �ָ� �˾Ƽ� true false ���� 
		 return op.LGE(a.getValue(),b.getValue(), operator);
	 default:
	 break;
	 }
	 
	 return null;
	 }
	 
	 private Node runQuote(ListNode node) {
	 return ((QuoteNode) node.car()).nodeInside();
	 }
	}
