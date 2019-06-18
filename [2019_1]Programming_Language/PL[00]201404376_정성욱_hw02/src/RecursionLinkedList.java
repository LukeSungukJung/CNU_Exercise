import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class RecursionLinkedList {
private Node head;
private static char UNDEF = Character.MIN_VALUE;
/**
* 새롭게 생성된 노드를 리스트의 처음으로 연결
*/
private void linkFirst(char element) {
head = new Node(element, head);
}
/**
* 과제 (1) 주어진 Node x의 마지막으로 연결된 Node의 다음으로 새롭게 생성된 노드를 연결
*
* @param element
* 데이터
* @param x
* 노드
*/
//static class Node의 구조를 이용, 변수로 들어오는 x가 null이면 마지막이라 판단하고 linkNext를 시행함(다음꺼 추가).
//그렇지않으면 계속 재귀로 이동
private void linkLast(char element, Node x) {
	if(x.next==null)
		{
		linkNext(element,x);
		}else {
		x= x.next;
		linkLast(element,x);
	}
}
/**
* 이전 Node의 다음 Node로 새롭게 생성된 노드를 연결
*
* @param element
* 원소
* @param pred
* 이전노드
*/
private void linkNext(char element, Node pred) {
Node next = pred.next;
pred.next = new Node(element, next);
}
/**
* 리스트의 첫번째 원소 해제(삭제)
*
* @return 첫번째 원소의 데이터
*/
private char unlinkFirst() {
Node x = head;
char element = x.item;
head = head.next;
x.item = UNDEF;
x.next = null;
return element;
}
/**
* 이전Node의 다음 Node연결 해제(삭제)
*
* @param pred
* 이전노드
* @return 다음노드의 데이터
*/
private char unlinkNext(Node pred) {
Node x = pred.next;
Node next = x.next;
char element = x.item;
x.item = UNDEF;
x.next = null;
pred.next = next;
return element;
}
/**
* 과제 (2) x노드에서 index만큼 떨어진 Node 반환
*/
//n만큼 떨어진 것을 구하는데 index자체를 하나의 값으로 취급하고 재귀를 할때마다 가까워지므로 1씩 빼서 매개변수로 줌
//0이될때까지 계속 이동. 
private Node node(int index, Node x) {
	if(index==0) return x;
	index--;
	return node(index,x.next);
}
/**
* 과제 (3) 노드로부터 끝까지의 리스트의 노드 갯수 반환
*/
//기본적인 레퍼토리는 앞서 구현한 것과 재귀들과 같지만, 리턴값이 int라는 것을 이용해서 매 재귀가 일어날때마다 1씩 더해줌.
//다만,마지막은 0으로 한다. 그래야 3번일어날때 1+1+1+0(x가 null인것을 확인 후)=3이 되기때문에 
private int length(Node x) {
	if(x==null) {
		return 0;
	}else {		
		return 1+length(x.next);
	}
}
/**
* 과제 (4) 노드로부터 시작하는 리스트의 내용 반환
*/
//기본적인 레퍼토리는 앞서 구현한 것과 재귀들과 같다. 그리고 이역시 static으로 선언된 클래스 Node의 구조를 이용함
//Node의 item변수는 하나의 문자를 담고있으므로, 재귀의 리턴값으로 끝이 아니라면 현재가지고있는 문자열을 리턴하면서 다음재귀를 시작한다.
//그렇게 되면 현재+다음+다음+다음으로 쭉 이어지다가 마지막노드일경우(노드.next==null, 다음이 암것도없으면) 자기 캐릭터 변수를 문자열로 바꿔서 리턴한다.
//그리고 하나씩 탈출하면서 지금까지 뱉었던 변수들을 +로 다 이어붙임
private String toString(Node x) {
// 채워서 사용, recursion 사용	
	if(x.next==null)
		return Character.toString(x.item);
	return x.item+toString(x.next);
}
/**
* 추가 과제 (5) 현재 노드의 이전 노드부터 리스트의 끝까지를 거꾸로 만듬
* ex)노드가 [s]->[t]->[r]일 때, reverse 실행 후 [r]->[t]->[s]
* @param x
* 현재 노드
* @param pred
* 현재노드의 이전 노드
*/
//미리 next를 가르키는 변수하나를 더만들어서 현재의 next를 짜르고 pred로 이어붙이고 다음 재귀를 진행할때 
//선언해두었던 임시변수를 주고 pred로 현재를 줘서 진행한다. 
//그러다가 x.next==null이면 마지막이므로 자연스럽게 next만 pred로 주고 head를 여기로 지정한 후 탈출한다.
private void reverse(Node x, Node pred) {
	if(x.next ==null) {
		x.next = pred;
		head = x;
		return ;
	}else if(pred==null) {
		
		Node temp = x.next;
		x.next = pred;
		reverse(temp,x);
	}else {
	Node temp = x.next;
	x.next = pred;
	reverse(temp,x);
	}
} /**
* 리스트를 거꾸로 만듬
*/
public void reverse() {
reverse(head, null);
}
/**
* 추가 과제 (6) 두 리스트를 합침 ( A + B )
* ex ) list1 =[l]->[o]->[v]->[e] , list2=[p]->[l] 일 때,
* list1.addAll(list2) 실행 후 [l]->[o]->[v]->[e]-> [p]->[l] * @param x
* list1의 노드
* @param y
* list2 의 head
*/
//두개의 리스트를 받는데 먼저 a+b라고 쳤을때 a에 해당하는 친구를 계쏙 재귀시켜서 마지막으로 이동시킨다. 
//그후 b의 변수를 하나씩 a의 뒤쪽으로 이어붙이는 재귀를 한다. 물론 이걸 한번 하고나게되면 다시 위쪽 주석의 레퍼토리로써
//a를한번더 이동하고 b를 하나더 붙이면 다시 a를 이동시키고 이걸 둘의 next가 null을 만날때까지 반복한다.
///사실상 b의 next가 null이면 마지막 이므로 현재값을 a뒤에 붙이고 탈출
private void addAll(Node x, Node y) {
	if(x.next!=null){
		addAll(x.next,y);
	}else if(y.next!=null) {
		linkLast(y.item,x);
		addAll(x,y.next);
	}else {
		linkLast(y.item,x);
		return;
	}
}
/**
* 두 리스트를 합침 ( this + B )
*/
public void addAll(RecursionLinkedList list) {
addAll(this.head, list.head);
}
/**
* 원소를 리스트의 마지막에 추가
*/
public boolean add(char element) {
if (head == null) {
linkFirst(element);
} else {
linkLast(element, head);
}
return true;
}

/**
* 원소를 주어진 index 위치에 추가
*
* @param index
* 리스트에서 추가될 위치
* @param element
* 추가될 데이터
*/
public void add(int index, char element) {
if (!(index >= 0 && index <= size()))
throw new IndexOutOfBoundsException("" + index);
if (index == 0)
linkFirst(element);
else
linkNext(element, node(index - 1, head));
}
/**
* 리스트에서 index 위치의 원소 반환
*/
public char get(int index) {
if (!(index >= 0 && index < size()))
throw new IndexOutOfBoundsException("" + index);
return node(index, head).item;
}
/**
* 리스트에서 index 위치의 원소 삭제
*/
public char remove(int index) {
if (!(index >= 0 && index < size()))
throw new IndexOutOfBoundsException("" + index);
if (index == 0) {
return unlinkFirst();
}
return unlinkNext(node(index - 1, head));
}
/**
* 리스트의 원소 갯수 반환
*/
public int size() {
return length(head);
}
@Override
public String toString() {
if (head == null)
return "[]";
return "[" + toString(head) + "]";
}
/**
* 리스트에 사용될 자료구조
*/
private static class Node {
char item;
Node next;
Node(char element, Node next) {
this.item = element;
this.next = next;
}
}
public static void main(String[] args) throws IOException{ 
	RecursionLinkedList list = new RecursionLinkedList();
	//파일 경로만 바꾸시면되는데, 저는 일단 제 로컬 기준을 따랐습니다.
	File file = new File("C:\\Users\\su_j6\\Documents\\Programing\\PL_cnu\\PL[00]201404376정성욱_hw02\\src\\hw01.txt"); 
	//파일입력 받아와서 새 리스트를 만드는 과정의 코드입니다.
	if(file.exists()) { 
		BufferedReader inFile = new BufferedReader(new FileReader(file)); 
		String sLine = inFile.readLine(); 
		for(int i=0;i<sLine.length();i++) {
			//add의 구현 결과입니다.
			list.add(sLine.charAt(i));
		}
		}
	System.out.println(list.toString());
	//리버스의 구현결고입니다.
	list.reverse();
	System.out.println(list.toString());
	//remove 구현결과
	list.remove(0);
	System.out.println(list.toString());
	//length 구현 결과
	System.out.println(list.length(list.head));
	
	//addALL구현을 위한 정의
	RecursionLinkedList list2 = new RecursionLinkedList();
	list2.add('L');
	list2.add('O');
	list2.add('L');
	//add ALL 구현 결과
	list2.addAll(list);
	System.out.println(list2);
	
}
}


//201404376 정성욱
