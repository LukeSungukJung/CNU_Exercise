package interpreter;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
//static array list�� ����Ǿ����� Define_table��

class DF_Table<String,Node> { 
	//Ʃ�� ���·� ��Ʈ��,��带 ������.
	 private String key;
	 public Node value;
	 
	 public DF_Table(String k, Node v) { 
	    this.key = k; 
	    this.value = v;    
	  } 
	 //���� Ű��������.
	 public String getKey() {
		 return key;
	 }
	 //���� Ŭ������ ���� ��� �� ����
	 public Node getNode() {
		 return value;
	 }
	}

class DF_func_table<String,ListNode,Node>{
	private ListNode funchead;
	private String key;
	private Node funcbody;
	private CuteInterpreter ci = new CuteInterpreter();
	private Node oper;
	public DF_func_table(String k,ListNode head ,Node body) { 
		oper = find_oper(body);
		key = k;
		funcbody = body;
		funchead = head;
	}
	public Node getoper() {
		return oper;
	}
	public Node find_oper(Node body) {
		Node car_res = (Node) ((interpreter.ListNode)body).car();
		return car_res;
	}
	public Node find_operandA(Node body) {
		return (Node) ((interpreter.ListNode)body).cdr().car();	
	}
	
	public Node find_operandB(Node body) {
		return (Node) ((interpreter.ListNode)body).cdr().cdr().car();	
	}
	
	public String getKey() {
		return this.key;
	}
	
	public Node getFuncbody() {
		 return funcbody;
	 }
	
	}


class Table_List{
	//DEFINE�� ȣ���ؼ� ���ǵ� �������� ���α׷� ����ɶ����� �����ϱ����� Define_table�� ������.
	static ArrayList<DF_Table> DF_Table_list = new ArrayList<DF_Table> ();
	static ArrayList<DF_func_table> DFfunc_list = new ArrayList<DF_func_table>();
	
	//���� ����Ʈ�� ��� ������ ���ǵǾ��ִ��� ���Ű��Ƽ� �����ص״µ� ���⼭�� �Ⱦ��Ե�
	static int DF_Table_List_max_index_num =0;
	static int DF_TableFunc_List_max_index_num =0;
	//���� ������ �� ������ ������ �� ��ġ�� �O������ �Լ���
	//string k���� ���� ����� ��ġ�� ������
	public int find_table_index(String k) {
		DF_Table sort_res = null;
		int idx = 0;
		for (DF_Table one : DF_Table_list) {
			String tmp_str = (String) one.getKey();
			if(tmp_str.equals(k)) {
				return idx;
			}
			idx++;
		}
		return -1;
	}
	public Node getResofFunc(Node operator,ValueNode operandA, ValueNode operandB) {
		ListNode operlist =  ListNode.cons(operandB,ListNode.EMPTYLIST);
		CuteInterpreter ci = new CuteInterpreter();
		operlist =  ListNode.cons(operandA,operlist);
		operlist =  ListNode.cons(operator,operlist);
		return ci.runExpr(operlist);
	}
	
	//������ ���̺���Ʈ�� ������ ���̺��� ���� �߰��ϴ� ���
	 public void add_DF_table(String k, Node v) {
		 //������ ���̺� ����Ʈ�� ����ٸ�, ù�κ� ��带 �������
		 //max���� ��� index�� 0 �̹Ƿ� ���� ��Ű�� ����.
		 if(DF_Table_list.isEmpty()) {
			 DF_Table head= new DF_Table(k,v);
			 DF_Table_list.add(head);
			 //������ ���̺� ����Ʈ�� ��������ʴٸ�
			 //�߰�����
		 }else {
			 //���� Ű���� �ߺ��� ����� ��� �������ϴ� �����̹Ƿ� 
			 // ���̺���Ʈ�� �ش��ε����� �O�Ƽ� �����ϰ� 
			 Node duple = find_id_Node(k);
			 if(duple!=null) {
				int duple_idx= find_table_index(k);
				DF_Table_list.remove(duple_idx);
				DF_Table_list.add(duple_idx,new DF_Table(k,v));
			 }else{
			 DF_Table tmpnxt = new DF_Table(k,v);
			 DF_Table_list.add(tmpnxt);
			 DF_Table_List_max_index_num++;
			 }
		 }
	 }
	 
	public void add_DF_table_func(String k,ListNode head,Node body) {
		DF_func_table newdf =  new DF_func_table<String,ListNode,Node>(k,head,body);
		DFfunc_list.add(newdf);
		DF_TableFunc_List_max_index_num++;
	}
	public int find_ftable_index(String k) {
		DF_Table sort_res = null;
		int idx = 0;
		for (DF_func_table one : DFfunc_list) {
			String tmp_str = (String) one.getKey();
			if(tmp_str.equals(k)) {
				return idx;
			}
			idx++;
		}
		return -1;
	} 

	
	 //id�� �����Ǵ� key���� �ִ��� Ȯ���ϱ� ���� static arrayList�� DF_table�� ��ȸ�ϸ� �O�´�
	 //������ key�� ����ġ�� ���� value�� ,Node�� �����Ѵ�. ������ null��
	 public Node find_id_Node(String id) {
		for (DF_Table one : DF_Table_list) {
			String tmp_str = (String) one.getKey();
			if(tmp_str.equals(id)) {
				return (Node) one.getNode();
			}
			
		}
		return null;
	 }
	 public Node find_Func_body(String id) {
		for (DF_func_table one : DFfunc_list) {
			String tmp_str = (String) one.getKey();
			if(tmp_str.equals(id)) {
				return (Node) one.getFuncbody();
			}
			
		}
		return null;
	 }
	 public DF_func_table getDFT_index(int idx) {
		 return DFfunc_list.get(idx);
	 }
	 public boolean isFuncTableNull(){
		 return (DFfunc_list.isEmpty());
	 }
	 //���̺� ��尡 ������� Ȯ�� ��̸���Ʈ �����Լ� ����. 
	 public boolean isTableNull(){
		 return (DF_Table_list.isEmpty());
	 }
	 
}


 

public class CuteInterpreter {
	 static Table_List table_list;
	 public static void main(String[] args) throws Exception {
	 ClassLoader cloader = ParserMain.class.getClassLoader();
	 //������ �ּ�ó����
	 //File file = new File(cloader.getResource("interpreter/as08.txt").getFile());
	 while(true) {
	 table_list = new Table_List();
	 //������ Ŀ��ó�� �Է¹ް� $���� �ϳ� �����
	 System.out.print("$ ");
	 //CuteParser������ �ϵ� �Է� ��ü�� ���߿� ������ٲ��� �ϴ� null��
	 CuteParser cuteParser = new CuteParser(null);
	 //�Ʒ��� ������ ����
	 CuteInterpreter interpreter = new CuteInterpreter();
	 Node parseTree = cuteParser.parseExpr();
	 Node resultNode = interpreter.runExpr(parseTree);
	 //���������Ϳ� ���� �Է� $ a ������ ����� ���ؼ� �˻��ϴ°���
	 //���� ���Ǵ� Id�� �ϹǷ� Id�� ��� �ѹ� Ȯ���غ�
	 if(resultNode instanceof IdNode ) {
		 //Lookuptable���� Ư������ ������ null�� ����
		Node tmp =  lookupTable((String)resultNode.toString());
		//tmp�� null�� �ƴ϶�°��� ���� ����ƽ ��� ����Ʈ�� ����� ���� �O�Ҵٴ� ���̹Ƿ�
		//��½ÿ� �ش� ��带 �� ���� ��ü����
		if(tmp!=null)
			resultNode = tmp;
	 }
	 
	 NodePrinter nodePrinter = new NodePrinter(resultNode);
	 //���� ���� �������
	 boolean stop =nodePrinter.prettyPrint();
	 if(stop) break;
	 Table_List.DF_Table_list.clear();
	 }
	 }
	 
	 private void errorLog(String err) {
	 System.out.println(err);
	 }
	 
	 public static Node lookupTable(String id) {
		 Table_List fineList = new Table_List();
		 return fineList.find_id_Node(id);
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
	 private void varBind(ListNode var, ListNode param) {
	      while(var != ListNode.EMPTYLIST) {
	         table_list.add_DF_table(var.car().toString(), param.car());
	         var = var.cdr();
	         param = param.cdr();
	      }
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
	 if (list.car() instanceof ListNode) {
         if(((FunctionNode)((ListNode)list.car()).car()).funcType == FunctionNode.FunctionType.LAMBDA ) {
            if(list.cdr() != ListNode.EMPTYLIST) {
               ListNode var = (ListNode)((ListNode)list.car()).cdr().car();
               ListNode param = list.cdr();
               if(param.car() instanceof ListNode)
                  param = (ListNode)param.car();
               ListNode expr  = (ListNode)((ListNode)list.car()).cdr().cdr().car();
               varBind(var, param);

               return runList(expr);
            }
         }
      }
	 if(list.car() instanceof IdNode) {
		 if(table_list.find_ftable_index(list.car().toString())!=-1) {
			 int functionidx =table_list.find_ftable_index(list.car().toString());
			 Node operandA =  list.cdr().car();
			 Node operandB =  list.cdr().cdr().car();
			 boolean only_one_operand = (operandB ==null);
			 DF_func_table now_dft  =  table_list.getDFT_index(functionidx);
			 Node oper = (interpreter.Node) now_dft.getoper();
			 ListNode combinedA = ListNode.cons(operandA, ListNode.EMPTYLIST);
			 if(!only_one_operand) {
				 ListNode combinedAB = ListNode.cons(operandB, combinedA);
				 ListNode combinedABOP = ListNode.cons(oper, combinedAB);
			 return runList(combinedABOP);
			 }else {
				 operandB = oper_a_b(list.car().toString())[2];
				 ListNode combinedAB = ListNode.cons(operandB, combinedA);
				 ListNode combinedABOP = ListNode.cons(oper, combinedAB);
				 return runList(combinedABOP);
			 }
			 
			 
			 
		 }
	 }
	 
	 return list;
	 }

	 public Node[] oper_a_b(String fname) {
		 Table_List d = new Table_List();
		 int dfidx = d.find_ftable_index(fname); 
		 DF_func_table dft =  d.getDFT_index(dfidx);
		 Node body = (interpreter.Node) dft.getFuncbody();
		 Node oper  = (interpreter.Node) dft.find_oper(body);
		 Node operA = (interpreter.Node) dft.find_operandA(body);
		 Node operB = (interpreter.Node) dft.find_operandB(body);
		 Node [] nodearr = new Node[3];
		 nodearr[0] = oper;
		 nodearr[1] = operA;
		 nodearr[2] =  operB;
		 return nodearr;
	 }
	 
	 private Node runFunction(FunctionNode operator, ListNode operand) {
		 switch (operator.funcType) {
		 //DEFINE�Ͻÿ� ������
		 case DEFINE:
			 boolean funcmode ;
		// ù��° �� ��. ���� cdr�� ���� �θ� Ű����.
		Node dhead= operand.car();
		Table_List definedList = new Table_List();
		funcmode = (dhead instanceof ListNode);
		if(!(((ListNode)operand.cdr().car()).car() instanceof BinaryOpNode))
		if(((FunctionNode)((ListNode)operand.cdr().car()).car()).funcType == FunctionNode.FunctionType.LAMBDA ) {
			ListNode last = (ListNode)operand.cdr().car();
			System.out.println();
			funcmode = true;
			ListNode ee = ListNode.cons(((ListNode)((ListNode)last).cdr().car()).car(), ListNode.EMPTYLIST);
			ee = ListNode.cons(dhead, ee);
			ListNode dd = last.cdr().cdr();
			operand =  ListNode.cons(ee, dd);
			dhead= operand.car();
		}
		if(funcmode) {
			Node fhead = ((ListNode)dhead).car();
			Node fbody = operand.cdr().car();
			definedList.add_DF_table_func(fhead.toString(),(ListNode)dhead, fbody);
			Node [] res = oper_a_b(fhead.toString());
			//BinaryOpNode bp = new BinaryOpNode();
			//bp.setValue(BinaryOpNode.BinType.PLUS.tokenType());
			//IntNode a1 = new IntNode("1");
			
			return operand.cdr();
			
		}else {
		//cdr�� �ϳ��� ����Ʈ �Է����� ���� runExpr�� �ؼ� �װ��� ����Ű�� ������ ���� ���� �ٲ�
		Node dtail= runExpr(operand.cdr().car());
		//���̺� ����Ʈ�� �����ϰ� 
		
		//static arrayList�� DF_table��  ���� Ű���� ���κ��� ��Ʈ���� ������ �� ��带 �־���.
		definedList.add_DF_table(dhead.toString(),dtail);
		//�̰� ���ٴ� �ǹ̷� �Ŀ� ��¥ ���� ����Ű�� ���� ������.
		return dtail;
	 // CAR, CDR, CONS� ���� ���� ����
	 // CAR�� ��� ����
		}
	 case CAR:
		 // ����Ʈ�� ù��°�� ���� Ȯ����
		 Node head = operand.car();
		 //head�� Id����� ��� ������ ���̺� ���ǵ� ������ ���ɼ��������Ƿ� ������
		 if(head instanceof IdNode &&!(head instanceof QuoteNode)) {
			 Node tmp = lookupTable(head.toString());
			 //tmp�� null�� �ƴ� ���� ���ǵ� ���� �ִٴ� ���̹Ƿ� �װ��� ��ü����
			 if(tmp !=null) { 
				 head =  ((ListNode)tmp);
				 FunctionNode fnode = new FunctionNode();
				 //( car a ) a = '( 1 2 3 4 5 )  �̷���Ȳ �̹Ƿ� �׳� �����ϸ鼭��ͷ� �ѹ��� ȣ������.
				 fnode.setValue(FunctionNode.FunctionType.CAR.tokenType());
			 	 return runFunction(fnode,(ListNode)head);
			 }
			 }
		 
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
				 // ( car '(cdr ( 1 2 3 4 )   ) ) ��� �Է¿� ���ؼ� racket�� ��������� ������ ������
				 // ( car '( a 1 2 3 4 ) ) �� 'a�� ����� ���Ƿ� IdNode�� ���ؼ��� �̷��� �ϰ� ó����
			 }else if(res instanceof FunctionNode|| res instanceof IdNode) {
				 Node p = new QuoteNode(res);
				 return ListNode.cons(p, ListNode.EMPTYLIST);
			 }
			// quote �Ⱥٿ��൵ �ǹǷ� �� ( ...)�� ��ȯ��
			 return res;
		 }
		// �ƴϸ� 'a 'b�� �̹Ƿ�  �� ��ȯ�� 
		 System.out.print("??");
		 return operand;
	//CDR�� ��� ������
	 case CDR:
		 // CDR (...) �� �Ŵϱ�
		 //�Ǿպκ� ���� �� 
		 head =  operand.car();
		 //head�� Id����� ��� ������ ���̺� ���ǵ� ������ ���ɼ��������Ƿ� ������
		 if(head instanceof IdNode) {
			 Node tmp = lookupTable(head.toString());
			//tmp�� null�� �ƴ� ���� �����ΰ� ������ ���̺� ���ǵ� ���� �ִٴ� ���̹Ƿ� head�� �װ����� ��ü����.
			 if(tmp !=null) { 
				 head =  ((ListNode)tmp);
				 FunctionNode fnode = new FunctionNode();
				 //( cdr a ) a = '( 1 2 3 4 5 )  �̷���Ȳ �̹Ƿ� �׳� �����ϸ鼭��ͷ� �ѹ��� ȣ������.
				 fnode.setValue(FunctionNode.FunctionType.CDR.tokenType());
			 	 return runFunction(fnode,(ListNode)head);
			 }
			 }
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
		 //head�� Id����� ��� ������ ���̺� ���ǵ� ������ ���ɼ��������Ƿ� ������
		 if(head instanceof IdNode) {
			 Node tmp = lookupTable(head.toString());
			//tmp�� null�� �ƴ� ���� �����ΰ� ������ ���̺� ���ǵ� ���� �ִٴ� ���̹Ƿ� head�� �װ����� ��ü����.
			 if(tmp !=null) { 
				 head =  tmp;
			 }
			 }
		 ListNode tail;
		 //�����κе� ���� ����
		 Node tail_tmp = operand.cdr().car();
		///tail_tmp�� Id����� ��� ������ ���̺� ���ǵ� ������ ���ɼ��������Ƿ� ������
		 if(tail_tmp instanceof IdNode) {
			 Node tmp = lookupTable(tail_tmp.toString());
			 tail = (ListNode)tmp;
		 }else {
			 //�ƴϸ� �� ������ �־���
		 tail = ((ListNode)(tail_tmp));
		 }
		  
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
		//head�� Id����� ��� ������ ���̺� ���ǵ� ������ ���ɼ��������Ƿ� ������
		 if(head instanceof IdNode&&!(head instanceof QuoteNode)) {
			 Node tmp = lookupTable(head.toString());
			//tmp�� null�� �ƴ� ���� �����ΰ� ������ ���̺� ���ǵ� ���� �ִٴ� ���̹Ƿ� head�� �װ����� ��ü����.
			 //����� ��ü�� ���ָ� �� ���� ���� == ���길 �ϴ� ���̹Ƿ� �߰����� ��ʹ� �θ��� �ʴ´�.
			 if(tmp !=null) { 
				 head =  tmp;
			 }
			 }	 
		
		 
		 if( head instanceof ListNode){
		 	// ' ����
		 head =runQuote((ListNode)head);
		 }
		 
		 Node tail_ = operand.cdr().car();
		//tail_�� Id����� ��� ������ ���̺� ���ǵ� ������ ���ɼ��������Ƿ� ������		 
		 if(tail_ instanceof IdNode&&!(tail_ instanceof QuoteNode)) {
			 Node tmp = lookupTable(tail_.toString());
			//tmp�� null�� �ƴ� ���� �����ΰ� ������ ���̺� ���ǵ� ���� �ִٴ� ���̹Ƿ� tail_�� �װ����� ��ü����.

			 if(tmp !=null) { 
				 tail_ =  tmp;
			 }
			 }
		 if( tail_ instanceof ListNode){
			 //�޺κ� �о�ͼ� �Ȱ��� ' ����
			 tail_ = (ListNode)(operand.cdr()).car();
			 tail_ = runQuote((ListNode)tail_);
		 }
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
		//head�� Id����� ��� ������ ���̺� ���ǵ� ������ ���ɼ��������Ƿ� ������
		 if(head instanceof IdNode&&!(head instanceof QuoteNode)) {
			 Node tmp = lookupTable(head.toString());
			//tmp�� null�� �ƴ� ���� �����ΰ� ������ ���̺� ���ǵ� ���� �ִٴ� ���̹Ƿ� head�� �װ����� ��ü����.
			 if(tmp !=null) { 
				 head =  tmp;
				 FunctionNode fnode = new FunctionNode();
				//( null? a ) a = '( 1 2 3 4 5 )  �̷���Ȳ �̹Ƿ� �׳� �����ϸ鼭��ͷ� �ѹ��� ȣ������.
				 fnode.setValue(FunctionNode.FunctionType.NULL_Q.tokenType());
				 return runFunction(fnode,(ListNode)head);
			 }
			 }	
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
		//head�� Id����� ��� ������ ���̺� ���ǵ� ������ ���ɼ��������Ƿ� ������
		 if(head instanceof IdNode&&!(head instanceof QuoteNode)) {
			 Node tmp = lookupTable(head.toString());
			//tmp�� null�� �ƴ� ���� �����ΰ� ������ ���̺� ���ǵ� ���� �ִٴ� ���̹Ƿ� head�� �װ����� ��ü����.
			 if(tmp !=null) { 
				 head =  tmp;
				 FunctionNode fnode = new FunctionNode();
				//( atom? a ) a = '( 1 2 3 4 5 )  �̷���Ȳ �̹Ƿ� �׳� �����ϸ鼭��ͷ� �ѹ��� ȣ������.
				 fnode.setValue(FunctionNode.FunctionType.ATOM_Q.tokenType());
				 return runFunction(fnode,(ListNode)head);
			 }
			 }
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
		//head�� Id����� ��� ������ ���̺� ���ǵ� ������ ���ɼ��������Ƿ� ������
		 if(head instanceof IdNode&&!(head instanceof QuoteNode)) {
			 Node tmp = lookupTable(head.toString());
			//tmp�� null�� �ƴ� ���� �����ΰ� ������ ���̺� ���ǵ� ���� �ִٴ� ���̹Ƿ� head�� �װ����� ��ü����.
			 if(tmp !=null) { 
				 //���� �������� ���� �ݴ븸 �����ָ� �Ǵ� �״�� �ְ� ������
				 head =  tmp;
				 
			 }
			 }
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
			//h_cond_res�� Id����� ��� ������ ���̺� ���ǵ� ������ ���ɼ��������Ƿ� ������
			 if(h_cond_res instanceof IdNode &&!(h_cond_res instanceof QuoteNode)) {
				 Node tmp = lookupTable(h_cond_res.toString());
				//tmp�� null�� �ƴ� ���� �����ΰ� ������ ���̺� ���ǵ� ���� �ִٴ� ���̹Ƿ� h_res_tmp�� �װ����� ��ü����.
				 if(tmp !=null) { 
					 h_cond_res =  tmp;
				 }
				 }
			 //��͵� �׳� �ٷ� �޺κ� ���� �־���
			 Node h_res_tmp = ((ListNode)head).cdr().car();
			//h_res_tmp�� Id����� ��� ������ ���̺� ���ǵ� ������ ���ɼ��������Ƿ� ������
			 if(h_res_tmp instanceof IdNode &&!(h_res_tmp instanceof QuoteNode)) {
				 Node tmp = lookupTable(h_res_tmp.toString());
				//tmp�� null�� �ƴ� ���� �����ΰ� ������ ���̺� ���ǵ� ���� �ִٴ� ���̹Ƿ� h_res_tmp�� �װ����� ��ü����.

				 if(tmp !=null) { 
					 h_res_tmp =  tmp;
				 }
				 }
			 h_cond_ans_res = h_res_tmp;
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
			 if(t_cond_res instanceof IdNode &&!(t_cond_res instanceof QuoteNode)) {
				 Node tmp = lookupTable(t_cond_res.toString());
				 if(tmp !=null) { 
					 t_cond_res =  tmp;
				 }
				 }
			 //�׿� ���� �Ǵ� �޺κе� �״�� �����ͼ� �־���
			 
			 Node t_cond_ans_res_tmp = ((ListNode)tail).cdr().car();
			 if(t_cond_ans_res_tmp instanceof IdNode &&!(t_cond_ans_res_tmp instanceof QuoteNode)) {
				 Node tmp = lookupTable(t_cond_ans_res_tmp.toString());
				 if(tmp !=null) { 
					 t_cond_ans_res_tmp =  tmp;
				 }
				 }
			 t_cond_ans_res = t_cond_ans_res_tmp;
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
		 
	 case LAMBDA:
         return ListNode.cons(ListNode.cons(operator, operand), ListNode.EMPTYLIST);	 
		 
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
		//head�� Id����� ��� ������ ���̺� ���ǵ� ������ ���ɼ��������Ƿ� ������
		 if(head instanceof IdNode) {
			 Node tmp = lookupTable(head.toString());
			//tmp�� null�� �ƴ� ���� �����ΰ� ������ ���̺� ���ǵ� ���� �ִٴ� ���̹Ƿ� head�� �װ����� ��ü����.
			 if(tmp !=null) { 
				 head =  tmp;
			 }
			 } 
		a= (IntNode)(head);
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
		//tail_head�� Id����� ��� ������ ���̺� ���ǵ� ������ ���ɼ��������Ƿ� ������
		 if(tail_head instanceof IdNode) {
			//tmp�� null�� �ƴ� ���� �����ΰ� ������ ���̺� ���ǵ� ���� �ִٴ� ���̹Ƿ� tail_head�� �װ����� ��ü����.
			 Node tmp = lookupTable(tail_head.toString());
			 
			 if(tmp !=null) { 
				 tail_head =  tmp;
			 }
			 } 
		 b= (IntNode)(tail_head);
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
