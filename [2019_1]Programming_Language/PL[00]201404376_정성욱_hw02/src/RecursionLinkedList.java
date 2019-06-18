import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class RecursionLinkedList {
private Node head;
private static char UNDEF = Character.MIN_VALUE;
/**
* ���Ӱ� ������ ��带 ����Ʈ�� ó������ ����
*/
private void linkFirst(char element) {
head = new Node(element, head);
}
/**
* ���� (1) �־��� Node x�� ���������� ����� Node�� �������� ���Ӱ� ������ ��带 ����
*
* @param element
* ������
* @param x
* ���
*/
//static class Node�� ������ �̿�, ������ ������ x�� null�̸� �������̶� �Ǵ��ϰ� linkNext�� ������(������ �߰�).
//�׷��������� ��� ��ͷ� �̵�
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
* ���� Node�� ���� Node�� ���Ӱ� ������ ��带 ����
*
* @param element
* ����
* @param pred
* �������
*/
private void linkNext(char element, Node pred) {
Node next = pred.next;
pred.next = new Node(element, next);
}
/**
* ����Ʈ�� ù��° ���� ����(����)
*
* @return ù��° ������ ������
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
* ����Node�� ���� Node���� ����(����)
*
* @param pred
* �������
* @return ��������� ������
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
* ���� (2) x��忡�� index��ŭ ������ Node ��ȯ
*/
//n��ŭ ������ ���� ���ϴµ� index��ü�� �ϳ��� ������ ����ϰ� ��͸� �Ҷ����� ��������Ƿ� 1�� ���� �Ű������� ��
//0�̵ɶ����� ��� �̵�. 
private Node node(int index, Node x) {
	if(index==0) return x;
	index--;
	return node(index,x.next);
}
/**
* ���� (3) ���κ��� �������� ����Ʈ�� ��� ���� ��ȯ
*/
//�⺻���� �����丮�� �ռ� ������ �Ͱ� ��͵�� ������, ���ϰ��� int��� ���� �̿��ؼ� �� ��Ͱ� �Ͼ������ 1�� ������.
//�ٸ�,�������� 0���� �Ѵ�. �׷��� 3���Ͼ�� 1+1+1+0(x�� null�ΰ��� Ȯ�� ��)=3�� �Ǳ⶧���� 
private int length(Node x) {
	if(x==null) {
		return 0;
	}else {		
		return 1+length(x.next);
	}
}
/**
* ���� (4) ���κ��� �����ϴ� ����Ʈ�� ���� ��ȯ
*/
//�⺻���� �����丮�� �ռ� ������ �Ͱ� ��͵�� ����. �׸��� �̿��� static���� ����� Ŭ���� Node�� ������ �̿���
//Node�� item������ �ϳ��� ���ڸ� ��������Ƿ�, ����� ���ϰ����� ���� �ƴ϶�� ���簡�����ִ� ���ڿ��� �����ϸ鼭 ������͸� �����Ѵ�.
//�׷��� �Ǹ� ����+����+����+�������� �� �̾����ٰ� ����������ϰ��(���.next==null, ������ �ϰ͵�������) �ڱ� ĳ���� ������ ���ڿ��� �ٲ㼭 �����Ѵ�.
//�׸��� �ϳ��� Ż���ϸ鼭 ���ݱ��� ����� �������� +�� �� �̾����
private String toString(Node x) {
// ä���� ���, recursion ���	
	if(x.next==null)
		return Character.toString(x.item);
	return x.item+toString(x.next);
}
/**
* �߰� ���� (5) ���� ����� ���� ������ ����Ʈ�� �������� �Ųٷ� ����
* ex)��尡 [s]->[t]->[r]�� ��, reverse ���� �� [r]->[t]->[s]
* @param x
* ���� ���
* @param pred
* �������� ���� ���
*/
//�̸� next�� ����Ű�� �����ϳ��� ������ ������ next�� ¥���� pred�� �̾���̰� ���� ��͸� �����Ҷ� 
//�����صξ��� �ӽú����� �ְ� pred�� ���縦 �༭ �����Ѵ�. 
//�׷��ٰ� x.next==null�̸� �������̹Ƿ� �ڿ������� next�� pred�� �ְ� head�� ����� ������ �� Ż���Ѵ�.
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
* ����Ʈ�� �Ųٷ� ����
*/
public void reverse() {
reverse(head, null);
}
/**
* �߰� ���� (6) �� ����Ʈ�� ��ħ ( A + B )
* ex ) list1 =[l]->[o]->[v]->[e] , list2=[p]->[l] �� ��,
* list1.addAll(list2) ���� �� [l]->[o]->[v]->[e]-> [p]->[l] * @param x
* list1�� ���
* @param y
* list2 �� head
*/
//�ΰ��� ����Ʈ�� �޴µ� ���� a+b��� ������ a�� �ش��ϴ� ģ���� ��� ��ͽ��Ѽ� ���������� �̵���Ų��. 
//���� b�� ������ �ϳ��� a�� �������� �̾���̴� ��͸� �Ѵ�. ���� �̰� �ѹ� �ϰ��ԵǸ� �ٽ� ���� �ּ��� �����丮�ν�
//a���ѹ��� �̵��ϰ� b�� �ϳ��� ���̸� �ٽ� a�� �̵���Ű�� �̰� ���� next�� null�� ���������� �ݺ��Ѵ�.
///��ǻ� b�� next�� null�̸� ������ �̹Ƿ� ���簪�� a�ڿ� ���̰� Ż��
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
* �� ����Ʈ�� ��ħ ( this + B )
*/
public void addAll(RecursionLinkedList list) {
addAll(this.head, list.head);
}
/**
* ���Ҹ� ����Ʈ�� �������� �߰�
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
* ���Ҹ� �־��� index ��ġ�� �߰�
*
* @param index
* ����Ʈ���� �߰��� ��ġ
* @param element
* �߰��� ������
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
* ����Ʈ���� index ��ġ�� ���� ��ȯ
*/
public char get(int index) {
if (!(index >= 0 && index < size()))
throw new IndexOutOfBoundsException("" + index);
return node(index, head).item;
}
/**
* ����Ʈ���� index ��ġ�� ���� ����
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
* ����Ʈ�� ���� ���� ��ȯ
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
* ����Ʈ�� ���� �ڷᱸ��
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
	//���� ��θ� �ٲٽø�Ǵµ�, ���� �ϴ� �� ���� ������ �������ϴ�.
	File file = new File("C:\\Users\\su_j6\\Documents\\Programing\\PL_cnu\\PL[00]201404376������_hw02\\src\\hw01.txt"); 
	//�����Է� �޾ƿͼ� �� ����Ʈ�� ����� ������ �ڵ��Դϴ�.
	if(file.exists()) { 
		BufferedReader inFile = new BufferedReader(new FileReader(file)); 
		String sLine = inFile.readLine(); 
		for(int i=0;i<sLine.length();i++) {
			//add�� ���� ����Դϴ�.
			list.add(sLine.charAt(i));
		}
		}
	System.out.println(list.toString());
	//�������� ��������Դϴ�.
	list.reverse();
	System.out.println(list.toString());
	//remove �������
	list.remove(0);
	System.out.println(list.toString());
	//length ���� ���
	System.out.println(list.length(list.head));
	
	//addALL������ ���� ����
	RecursionLinkedList list2 = new RecursionLinkedList();
	list2.add('L');
	list2.add('O');
	list2.add('L');
	//add ALL ���� ���
	list2.addAll(list);
	System.out.println(list2);
	
}
}


//201404376 ������
