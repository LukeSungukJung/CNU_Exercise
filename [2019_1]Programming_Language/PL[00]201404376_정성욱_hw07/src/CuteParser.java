

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Stream;
import lexer.Scanner;
import lexer.ScannerMain;
import lexer.Token;
import lexer.TokenType;

public class CuteParser {
	private Iterator<Token> tokens;
	private static Node END_OF_LIST = new Node(){};

	public CuteParser(File file) {
		try {
			tokens = Scanner.scan(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private Token getNextToken() {
		if (!tokens.hasNext())
			return null;
		return tokens.next();
	}

	public Node parseExpr() {
		Token t = getNextToken();
		if (t == null) {
			System.out.println("No more token");
			return null;
		}
		TokenType tType = t.type();
		String tLexeme = t.lexme();
		//System.out.println(tType);
		switch (tType) {
		
		case DIV:
		case EQ:
		case MINUS:
		case GT:
		case PLUS:
		case TIMES:
		case LT:
			//사칙연산 노드의 값(타입)을 설정하고 사칙연산노드를 리턴함
			BinaryNode binode = new BinaryNode();
			binode.setValue(tType);
			return binode;
		
		
		case ATOM_Q:
		case CAR:
		case CDR:
		case COND:
		case CONS:
		case DEFINE:
		case EQ_Q:
		case LAMBDA:
		case NOT:
		case NULL_Q:
			//함수노드의 타입을 설정하고 함수노드를 리턴하는 코드
			functionNode fnode = new functionNode();
			fnode.setValue(tType);
			return fnode;
		
		case ID:
			 return new IdNode(tLexeme);
			 case INT:
			 if (tLexeme == null)
			 System.out.println("???");
			 return new IntNode(tLexeme);
			//새로 구현된 BooleanNode Case
			case FALSE:
			return BooleanNode.FALSE_NODE;
			case TRUE:
			return BooleanNode.TRUE_NODE;
			//새로 구현된 L_PAREN, R_PAREN Case
			case L_PAREN:
			return parseExprList();
			case R_PAREN:
			return END_OF_LIST ;
			//새로 추가된 APOSTROPHE, QUOTE
			case APOSTROPHE:
			 QuoteNode quoteNode = new QuoteNode(parseExpr());
			 ListNode listNode = ListNode.cons(quoteNode, ListNode.ENDLIST);
			 return listNode;
			case QUOTE:
			return new QuoteNode(parseExpr());
			default:
			System.out.println("Parsing Error!");
			return null;
			}
			}
			private ListNode parseExprList() {
			Node head = parseExpr();
			if (head == null)
			return null;
			if (head == END_OF_LIST) // if next token is RPAREN
			return ListNode.ENDLIST;
			ListNode tail = parseExprList();
			if (tail == null)
			return null;
			return ListNode.cons(head, tail);
			}
}

