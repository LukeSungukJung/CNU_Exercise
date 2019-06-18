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
	 
	 // CAR, CDR, CONS등에 대한 동작 구현
	 // CAR일 경우 진입
	 case CAR:
		 // 리스트의 첫번째를 떼서 확인함
		 Node head = operand.car();
		 //( 함수 뭐 ( ...) ) 일 경우를 대비한것
		 if(head instanceof FunctionNode) {
			 //재귀호출해서 안에꺼 처리함
			 Node res = runFunction((FunctionNode)head,((ListNode)operand.cdr().car()));
			 //( ...) 형식으로 나올텐데  ...를 읽어옴 ...일수도 있음 ... 일경우 ...의 
			 head = ((ListNode)res).car();
			 //operand를  나머지 뒷부분에 대한 처리인데 앞 부분만 따면 되므로  영엉 쓰일 일이 없음
			 operand = (ListNode)res;
		 }
		//'( ... ) 일경우 진입
		 if(head instanceof QuoteNode) {
			 // 쿼트 노드를 돌아서 car 즉 내부리스트 ( ... )를 반환함
			 Node res =  ((ListNode)runQuote(operand)).car();
			 // ...가 리스트 노드면 진입 
			 
			 if(res instanceof ListNode) {
				 //'( ... )의 형태로 줘야 하므로 쿼트노드 하나 만듬
				 Node p = new QuoteNode((ListNode)res);
				 //빈 리스트와 합쳐서 반환한다
				 return ListNode.cons(p, ListNode.EMPTYLIST);
			 }
			// quote 안붙여줘도 되므로 걍 ( ...)를 반환함
			 return res;
		 }
		// 아니면 'a 'b등 이므로  걍 반환함 
		 return head;
	//CDR일 경우 진입함
	 case CDR:
		 // CDR (...) 일 거니까
		 //맨앞부분 떼서 봄 
		 head =  operand.car();
		 //...가 어떤 함수 ( ...2 ) 일경우 를 가정함
		 if(head instanceof FunctionNode) {
			 // 어떤 함수 ...2를 인자로 줘서 함수 재귀한번 시켜줌
			 Node res = runFunction((FunctionNode)head,((ListNode)operand.cdr().car()));
			 // car일수도 cdr일수도 있지만 어쨌든 그결과값 ( ( ...) )의 형태일수도 (  a ) 일수도 있지만 기본적으로 괄호 하나씩 쳐서 나오게 했으므로
			 //첫번쨰 괄호 벗겨주는 작업으로car을 씀
			 head = ((ListNode)res).car();
			 //남은 뒷부분으로 operand에 넣어줌 cdr 이거 넣어준다
			 operand = (ListNode)res;
		 }

		 //위에서 처리한 head가 '( ...) 일수도 있고 그냥 '( ...)일수도 일때 if문 진입!
		 if(head instanceof QuoteNode) {
			 //'의 뒷부분 '( ...)에서 (...)에 대해서 quoteNode처리하는 함수 돌림
			 Node res =  ((ListNode)runQuote(operand)).cdr();
			 // 진짜 뒷부분 나온 것이므로 ... 만 나온것이므로 또 리턴 형식 지켜서 빈 '노드 하나 만들어서
			 Node p = new QuoteNode((ListNode)res);
			 //cons랑 합쳐줌
			 return ListNode.cons(p, ListNode.EMPTYLIST);
		 }
		 //리스트나 위 와같은 요상한 것 없으면 그냥 뒷부분 원소들 리턴함.
		 return operand.cdr();
	//cons일 경우 진입!
	 case CONS:
		 //일단 헤드부분 뗌
		 head =  operand.car();
		 //꼬리부분도 따로 떼줌
		 ListNode tail = ((ListNode)((ListNode)operand.cdr().car()));
		 // 헤드 부분이 a b 1 2 등 값을 가진 단일 노드 면 진입!
		 if(head instanceof ValueNode) {
			 	// 벨류일 경우 그냥 합치기만 하면 되기 때문에 꼬리부분 항상 '가 붙으므로 (pdf에서 list랑 합친다고 나와서 무조건적으로 붙는다고봄)
			 	//Quote 돌려서 리스트 빼낸다.
				 Node tail_res =  runQuote(tail);
				 //나온 결과의 리스트와 원소를 합쳐서
				 Node res = ListNode.cons(head,(ListNode)tail_res);
				 Node p = new QuoteNode((ListNode)res);
				 //합친 부분에 대해 다시 빈 쿼트 노드 하나만들어서 거기에 그 값을 추가해서 리턴함.
				 return ListNode.cons(p, ListNode.EMPTYLIST);
		 //head가 리스트 노드일 경우 진입!		 
		 }else if(head instanceof ListNode){
			 //헤드의 첫부분을 뗌
			 Node t_head = ((ListNode)head).car();
			 //헤드의 꼬리를 뗌
			 ListNode head_b = ((ListNode)head).cdr();
			 //헤드의 첫부분이 함수면 진입!
			 if(t_head instanceof FunctionNode) {
				 //헤드의 첫부분을 매개변수로 뒷부분을 연산하는 재귀 실행!
				 Node res = runFunction((FunctionNode)t_head,((ListNode)head_b.car()));
				 //꼬리부분 '걸려있으므로 벗겨준다.
				 Node t_tail = runQuote(tail);
				 //꼬리를 첫부분과
				 Node tail_h = ((ListNode)t_tail).car();
				 //뒷부분을 나눔
				 ListNode tail_b = ((ListNode)t_tail).cdr();
				 //그냥 일반적인 리스트 일수도 있으므로 일단 넣어줌
				 Node tail_res = t_tail;
				 //만약 함수가 쎃인 로직이면 들어감!
				 if(tail_h instanceof FunctionNode) { 
					 //앞부분 함수 뒷부분 매개변수 실행!
					 tail_res =  runFunction((FunctionNode)tail_h,(ListNode)tail_b.car());
					 //뒷부분은 항상 리스트이므로 '가 붙는데 여기서 이거 떼줌
					 tail_res = runQuote((ListNode)tail_res);}
				 // 함수 있는 로직이면 결과값 갖고오고 아니면 그냥위의 리스트랑 헤드부분 결과 합쳐줌
				 res = ListNode.cons(res,(ListNode)tail_res);
				//리스트라 '붙여야해서 하나 쿼트 노드만들어줌
				 Node p = new QuoteNode((ListNode)res);
				 //( )  씌워주려고 빈 리스트 하나만들어서 합쳐서 리턴함
				 return ListNode.cons(p, ListNode.EMPTYLIST);
			 }else {
			 //진짜 그냥 리스트만 있는 경우 각각 '떼서 붙여서 함쳐주는 과정임.
			 Node head_res = runQuote((ListNode)head);	 
			 Node tail_res =  runQuote(tail);
			 Node res =ListNode.cons(head_res,(ListNode)tail_res);
			 Node p = new QuoteNode((ListNode)res);
			 return ListNode.cons(p, ListNode.EMPTYLIST);
			 }
		 }
	 case EQ_Q:
		 //역시 헤드부분 뗌
		 head = operand.car();
		 // ' 떼줌
		 head =runQuote((ListNode)head);
		 //뒷부분 읽어와서 똑같이 ' 떼줌
		 tail = (ListNode)(operand.cdr()).car();
		 Node tail_ = runQuote(tail);
		 //앞 뒤 둘다 스트링으로 변환함
		 String a = head.toString();
		 String b =tail_.toString();
		 //스트링으로 값이 같은지 그리고 같은 객체를 참조하는지 and연산 돌려봄
		 boolean res = a.equals(b) && head.equals(tail_);
		 // 참이면 booleannode true를 리턴하고 아니면 거짓을 리턴함
		 return  res ?  BooleanNode.TRUE_NODE:BooleanNode.FALSE_NODE;
	 case NULL_Q:
		 //헤드부분 떼서봄
		 head = operand.car();
		 //헤드부분이 '노드이면, 뭔가 있을수도 있다면?
		 if(head instanceof QuoteNode) {
			// '떼봄
			Node res_head = runQuote((ListNode)operand);
			//'뗀 것의 뒤가  비었을 경우
			if((operand.cdr()).equals(ListNode.EMPTYLIST)) {
				//'뗸 리스트의 앞부분도 비었을경우 
				if(res_head.equals(ListNode.EMPTYLIST)){
					//즉 전체가 비었을 경우 트루 불린노드 리턴.
					return BooleanNode.TRUE_NODE;
				}
			}
		 }
		 // 저조건 만족하지 않으면 안에 뭐가 있는 것이므로 다 거짓임
		 return BooleanNode.FALSE_NODE;
	 case ATOM_Q:
		 //앞부분 또 떼어봄 
		 head = operand.car();
		 //head가 쿼트 노드라면
		 if(head instanceof QuoteNode) {
			 //'을 뗏는데 emptylist라면 true반환
			 if(runQuote(operand).equals(ListNode.EMPTYLIST))
				 return BooleanNode.TRUE_NODE;
			 //' 뗏는데 값을 가진 단일 노드라도 true반환
			 else if(runQuote(operand) instanceof ValueNode)
				 return BooleanNode.TRUE_NODE;
			 //나머지는 거짓임 
				 return BooleanNode.FALSE_NODE;
				 
		 }
	 case NOT:
		 //앞부분 떼봄
		 head = operand.car();
		 //그냥 불린 노드일시에 
		 if(head instanceof BooleanNode) {
			 //값을 갖고와서 반대값을 대입함
			 boolean h_val = !((BooleanNode) head).value; 
			 if(h_val == true)
				 //그갑따라 고대로 새 불린 노드 리턴함
				 return BooleanNode.TRUE_NODE;
			 else
				 return BooleanNode.FALSE_NODE;
			 
			 }	
		 Node n_res;
		 Boolean n_res_v;
		 //앞부분이 바이너리 노드라면
		 if(head instanceof BinaryOpNode) {
			 //Binary 연산돌림 > < = 일수 있으니
			 n_res = runBinary(operand);
			 //결과 값의 불린 노드값에 !로 반대를 저장해줌
			 n_res_v = !((BooleanNode)n_res).value;
			 //그것의 결과에 따라  트루 펄스를 리턴함
			 if(n_res_v == false)
				 return BooleanNode.FALSE_NODE;
			 else
				 return BooleanNode.TRUE_NODE;
						 
		//바이너리노드 아닐시에 펑션노드인지 보고 들어감
		 }else if(head instanceof FunctionNode){
			 //펑션노드 앞부분이랑 오퍼런드의 뒷 부분을 줌
			 n_res = runFunction((FunctionNode)head,operand.cdr());
			 n_res_v = !((BooleanNode)n_res).value;
			 //위와 마찬가지로 값 가지고와서 반대 작업 해준담에 
			 //반대인 값 참조해서 그에 맞게 리턴해줌.
			 if(n_res_v == true)
				 return BooleanNode.TRUE_NODE;
			 else
				 return BooleanNode.FALSE_NODE;
		 }else {
			 head = operand.car();
			 
		 }
		 //COND일 겨우 진입
	 case COND:
		 //들어온 객체의 앞부분을 떼어봄
		 head = ((ListNode)operand.car());
		 // 앞부분 헤드 부분이 결과를 저장할 변수들
		 //얘는 불린노드로 트루노드인지 펄스 노드인지 저장함
		 Node h_cond_res;
		 //트루 펄스 일때 가지는 값을 저장함.
		 Node h_cond_ans_res ;
		 //헤드의 앞부분을 저장함 head -> ( ?.....  ) 일때 ?를 갖고옴 
		 Node head_cond =((ListNode)head).car();
		 //위에서 갖고온 ? 가 리스트 노드면 진입함 ? ->  (.... )
		 if (head_cond instanceof ListNode) {
			 //?의 뒷부분을 갖고옴 (?2 ...) 에서 ...
			Node head_res = ((ListNode)head_cond).cdr();
			//?의 첫번째 노드를 갖고옴 
			Node list_head = ((ListNode) head_cond).car();
			//? 의 뒷부분 저기선 ?2 부분을 갖고옴
			Node list_tail = ((ListNode) head_cond).cdr();
			//이곳 까지 왔다는 것은 리스트가 어떤 연산을 통해서 true false를 결정해야되므로 특정 연산하는 부분이 있다고 가정함
			//?2가 뭔지 검사함 펑션 노드일 경우 진입!
			if(list_head instanceof FunctionNode) {
				//이부분은 연산 결과 즉 #f #T 중하나가 들어가는 부분임
				h_cond_res = runFunction((FunctionNode)list_head,(ListNode)list_tail);
				//앞의 h_cond_res의 값에 대응되는 가지는 조건의 결과값임 ans가 1이고 cond_res가 #T라고 가정할 때의 결과 형태: (#T 1)  
				h_cond_ans_res = ((ListNode)head).cdr().car();
			}else {
				//?2가 뭔지 검사함 바이너리 노드( < > = )일 경우 진입!
				//이부분은 연산 결과 즉 #f #T 중하나가 들어가는 부분임
				h_cond_res = runBinary((ListNode)head_cond);
				//앞의 h_cond_res의 값에 대응되는 가지는 조건의 결과값임 ans가 1이고 cond_res가 #T라고 가정할 때의 결과 형태: (#T 1) 
				h_cond_ans_res = ((ListNode)head).cdr().car();			
			}
		 }else {
			 //위 조건 아닐 경우 그냥 단일 참 거짓 노드 이므로 떼서 그대로 넣어줌
			 h_cond_res = head_cond;
			 //요것도 그냥 바로 뒷부분 떼서 넣어줌
			 h_cond_ans_res = ((ListNode)head).cdr().car();
		 }
		 
		 
		 //꼬리부분 리스트 반환
		 tail = (ListNode)operand.cdr().car();
		 // 앞부분 테일 부분이 결과를 저장할 변수들
		 //얘는 불린노드로 트루노드인지 펄스 노드인지 저장함		 
		 Node t_cond_res;
		 Node t_cond_ans_res ;
		 //트루 펄스 일때 가지는 값을 저장함.
		 Node tail_cond =((ListNode)tail).car();
		 //헤드의 앞부분을 저장함 head -> ( ?.....  ) 일때 ?를 갖고옴 		 
		 //위에서 갖고온 ? 가 리스트 노드면 진입함 ? ->  (?2 .... )
		 if (tail_cond instanceof ListNode) {
			//?의 첫번째 노드를 갖고옴. 즉 (?2 갖고 옴) 			 
			Node list_head = ((ListNode) tail_cond).car();
			//?의 뒷부분을 갖고옴 (?2 ...) 에서 ...
			Node list_tail = ((ListNode) tail_cond).cdr();
			// ?2가 펑션 노드면 진입함
			if(list_head instanceof FunctionNode) {
				//t_cond_res에 ?2의 함수와 ...를 매개변수로 준 연산의 결과값 #T #F 중 하나를 저장함
				t_cond_res = runFunction((FunctionNode)list_head,(ListNode)list_tail);
				//t_cond_res에 ?2의 함수와 ...를 매개변수로 준 연산에 대응되는 값을 저장함
				t_cond_ans_res = ((ListNode)tail).cdr().car();
			}else {
				//위 조건 아닐 경우 그냥 단일 참 거짓 노드 이므로 떼서 그대로 넣어줌
				t_cond_res = runBinary((ListNode)tail_cond);
				//요것도 그냥 바로 뒷부분 떼서 넣어줌 다음으로 오는 대응되는 값임.
				t_cond_ans_res = ((ListNode)tail).cdr().car();	
			}	
			//뒷부분의 car부분이  리스트가 아닐 경우, 그냥 단일 노드 이므로 그대로 갖고온다
		 }else {
			 //그대로 값 넣어줌
			 t_cond_res = tail_cond;
			 //그에 대응 되는 뒷부분도 그대로 가져와서 넣어줌
			 t_cond_ans_res = ((ListNode)tail).cdr().car();
		 }
		 //(a b) (c d)에서 조건인 a에 해당하는 부분의 값
		 Boolean head_right = ((BooleanNode)h_cond_res).value;
		 //(a b) (c d)에서 조건인 c에 해당하는 부분의 값
		 Boolean tail_right = ((BooleanNode)t_cond_res).value;
		 //a가 참일 경우 b를 반환
		 if(head_right)
			 return h_cond_ans_res;
		 //c가 참일 경우 d를 반환
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
	// 구현과정에서 필요한 변수 및 함수 작업 가능
	 //바이너리  연산 자체를 커버가능하게 그냥 내부 클래스 하나 만듬
	 class OPPMTD{
		 // 사칙연산을 커버함 인테저 a ,b 함수 연산값을 받음
	 public String PMTD(Integer a, Integer b,BinaryOpNode op){
		 int res =99;
		 //99는 초기화 해준것이고 저값이 리턴될리 없음
		 // 연산 타입에따라 그냥 인테저의 인트 값을 받아서 연산하고 스위치문 나옴
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
		 // 인테저 내장함수로 스트링값으로 변환해줌
		 return Integer.toString(res);
	 }
	 // LG GT EQ를 커버하는 함수임 역시 매개변수로 인테저값들과 연산하는거 받음
	 public BooleanNode LGE(Integer a, Integer b,BinaryOpNode op) {
		 switch(op.binType) {
		 //<라면 
		 case LT:
			 //보고 진짜 < 면 참 리턴 아니면 거짓 의 불린노드를 리턴함.
			 if(a.intValue()<b.intValue())
				 return BooleanNode.TRUE_NODE;
			 else
				 return BooleanNode.FALSE_NODE;
		 //연산자가 = 면
		 case EQ: 
			//보고 진짜 같으면 참 리턴 아니면 거짓 의 불린노드를 리턴함.
			 if(a.intValue()==b.intValue())
				 return BooleanNode.TRUE_NODE;
			 else
				 return BooleanNode.FALSE_NODE;
		 //연산자가 >면
		 case GT:
			//보고 진짜 > 면 참 리턴 아니면 거짓 의 불린노드를 리턴함.
			 if(a.intValue()>b.intValue())
				 return BooleanNode.TRUE_NODE;
			 else
				 return BooleanNode.FALSE_NODE;
		default:
			return null;
	 }
	 }
	 }
	 //리스트에서 앞부분 떼봄
	 Node head = list.cdr().car();
	 IntNode a;
	 //헤드가 리스트노드면
	 if(head instanceof ListNode) {
		 //헤드의 첫번째를 떼서보고 펑션이면 조건문 들어감
		 if(((ListNode)head).car() instanceof FunctionNode) {
			 // 조건으로 들어왔던 헤드의 첫부분 펑션노드 뗌
			 Node func = ((ListNode)head).car();
			 //뒷부분의 리스트를 분리해서
			 ListNode head_rest = (ListNode)((ListNode)head).cdr().car();
			 //펑션이므로 runfuntion 연산을 함
			 a = (IntNode)runFunction((FunctionNode)func,head_rest);
			 }else
			 //그렇지않은 경우는 ( + 2 3 ) 등 이런 경우 이므로 현재함수 재귀호출해서 값 계산함
			 a= (IntNode)runBinary((ListNode)head);
	 // 리스트 노드가 아니면 + 2 3 이므로 두번째가 첫번째 operand가 됨.
	 }else {
		a= (IntNode)list.cdr().car();
	 }
	 // + 1 2에서 2를 나타내는 즉 뒷부분의 뒷부분을 가르킴
	 ListNode tail = list.cdr().cdr();
	 //그 단일 리스트의 첫부분을 갖고 옴
	 Node tail_head = tail.car();
	 IntNode b;
	 // 그 첫부분이 리스트 노드라면
	 if(tail_head instanceof ListNode) {
		 //그 첫부분이 펑션 노드일 경우 들어감!
		 if(((ListNode)tail_head).car() instanceof FunctionNode) {
			 //앞부분 함수 빼고
			 Node func = ((ListNode)tail_head).car();
			 //뒷부분  ( ... ) 형태만 남겨둠 ...는 단일 벨류노드들임
			 ListNode tail_rest = (ListNode)((ListNode)tail_head).cdr().car();
			 // 방금 앞부분 뗀 함수와 뒷부분 리스트를 매개변수로 함수 값 계산함.(IntNode 리턴함) 이거를 b에 넣어줌 
			 b = (IntNode)runFunction((FunctionNode)func,tail_rest);
			 }
		 else
		// 펑션 아니면 그냥 ( + 2 1 ) 이런 케이스므로 재귀함수 호출해서 값 계산해줌
		 b = (IntNode)runBinary((ListNode)tail_head);
	 }else {
		 //리스트 아니면 단일 값이므로 그대로 b에 넣어줌
		 b= (IntNode)tail_head;
	 }
	 //시작할때 만든 내부 클래스 생성자 줘서 만들어줌
	 OPPMTD op = new OPPMTD();
	 
	 switch (operator.binType) {
	 // +,-,/ 등에 대한 바이너리 연산 동작 구현
	 case PLUS:
	 case MINUS:
	 case TIMES:
	 case DIV:
		 // IntNode를 리턴해야하므로 그것을 생성하기위한 매개변수를 제공하기위해 스트링으로 받는다  사칙연산에 대한 계산을 함
		 String res_r = op.PMTD(a.getValue(),b.getValue(),operator);
		 //인트 노드 만들어서 리턴함
		 IntNode res = new IntNode(res_r);
		 //System.out.println(res);
		 return res;
	 case LT:
	 case GT:
	 case EQ:
		 //위에서 다 처리했으므로 Integer만 받아서  값만 주고 연산자 주면 알아서 true false 나눔 
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
