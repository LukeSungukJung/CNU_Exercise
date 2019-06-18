package lexer;

import static lexer.TransitionOutput.GOTO_ACCEPT_ID;
import static lexer.TransitionOutput.GOTO_ACCEPT_INT;
import static lexer.TransitionOutput.GOTO_EOS;
import static lexer.TransitionOutput.GOTO_FAILED;
import static lexer.TransitionOutput.GOTO_MATCHED;
import static lexer.TransitionOutput.GOTO_SHARP;
import static lexer.TransitionOutput.GOTO_SIGN;
import static lexer.TransitionOutput.GOTO_START;
import static lexer.TokenType.FALSE;
import static lexer.TokenType.INT;
import static lexer.TokenType.MINUS;
import static lexer.TokenType.PLUS;
import static lexer.TokenType.TRUE;
//아래는 나머지 특수문자 상태들에 대한 처리를 위해 타입들을 통째로 로드함.
import static lexer.TokenType.*;


enum State {
	START {
		@Override
		public TransitionOutput transit(ScanContext context) {
			Char ch = context.getCharStream().nextChar();
			char v = ch.value();
			System.out.println(context.getLexime());
			switch ( ch.type() ) {
				case LETTER:
					context.append(v);
					return GOTO_ACCEPT_ID;
				case DIGIT:
					context.append(v);
					return GOTO_ACCEPT_INT;
				case SPECIAL_CHAR: 
					//+,-들에 대해 부호값을 매겨서 처리하는 것임 SIGN상태로 줘서 받는 문자에 따라 다르게 처리함.
					if (Character.toString(v).matches("[+|-]")) { 
						context.append(v);
						return GOTO_SIGN;
					}
					//TRUE ,FALSE에 대한 처리인데 여기서는 #을 먼저 받아서 #T,#F에 따라서 참 거짓이 되기때문에 일단 샤프로 보내고 본다.
					else if (v=='#') {  
						context.append(v);
						return GOTO_SHARP;
					}
					else { 
						
						switch(v) {
						//나머지 상태에 대한 처리임. 딱히 어디서 처리하는 로직이 없기때문에 처음에 import한 값에 대해서 메칭메소드를 이용해서 새로운 상태값을 정의하면서
						//넘긴다.
						case '*':
							context.append(v);
							return GOTO_MATCHED(TIMES, context.getLexime());
						case '/':
							context.append(v);
							return GOTO_MATCHED(DIV, context.getLexime());

							case '(':
								context.append(v);
								return GOTO_MATCHED(L_PAREN, context.getLexime());
							case ')':
								context.append(v);
								return GOTO_MATCHED(R_PAREN, context.getLexime());
							case '<':
								context.append(v);
								return GOTO_MATCHED(LT, context.getLexime());
							case '>':
								context.append(v);
								return GOTO_MATCHED(GT, context.getLexime());
							case '\'':
								context.append(v);
								return GOTO_MATCHED(APOSTROPHE, context.getLexime());
							case '=':
								context.append(v);
								return GOTO_MATCHED(EQ, context.getLexime());								
							case '?':	
								context.append(v);
								return GOTO_ACCEPT_ID;							
								
						}
					}
				case WS:
					return GOTO_START;
				case END_OF_STREAM:
					return GOTO_EOS;
				default:
					throw new AssertionError();
			}
		}
	},
	ACCEPT_ID {
		@Override
		public TransitionOutput transit(ScanContext context) {
			Char ch = context.getCharStream().nextChar();
			char v = ch.value();
			switch ( ch.type() ) {
				case LETTER:
				case DIGIT:
					context.append(v);
					return GOTO_ACCEPT_ID;
				case SPECIAL_CHAR:
					return GOTO_FAILED;
				case WS:
				case END_OF_STREAM:
					return GOTO_MATCHED(Token.ofName(context.getLexime()));
				default:
					throw new AssertionError();
			}
		}
	},
	ACCEPT_INT {
		@Override
		public TransitionOutput transit(ScanContext context) {
			Char ch = context.getCharStream().nextChar();
			switch ( ch.type() ) {
				case LETTER:
					return GOTO_FAILED;
				case DIGIT:
					context.append(ch.value());
					return GOTO_ACCEPT_INT;
				case SPECIAL_CHAR:
					
						
					return GOTO_FAILED;
				case WS:
					return GOTO_MATCHED(INT, context.getLexime());
				case END_OF_STREAM:
					return GOTO_FAILED;
				default:
					throw new AssertionError();
			}
		}
	},
	SHARP {
		@Override
		public TransitionOutput transit(ScanContext context) {
			Char ch = context.getCharStream().nextChar();
			char v = ch.value();
			switch ( ch.type() ) {
				case LETTER:
					switch ( v ) {
						case 'T':
							context.append(v);
							return GOTO_MATCHED(TRUE, context.getLexime());
						case 'F':
							context.append(v);
							return GOTO_MATCHED(FALSE, context.getLexime());
						default:
							return GOTO_FAILED;
					}
				default:
					return GOTO_FAILED;
			}
		}
	},
	SIGN {
		@Override
		public TransitionOutput transit(ScanContext context) {
			Char ch = context.getCharStream().nextChar();
			char v = ch.value();
			switch ( ch.type() ) {
				case LETTER:
					return GOTO_FAILED;
				case DIGIT:
					context.append(v);
					return GOTO_ACCEPT_INT;
				case SPECIAL_CHAR:
					return GOTO_FAILED;
				case WS:
					String lexme = context.getLexime();
					switch ( lexme ) {
						case "+":
							return GOTO_MATCHED(PLUS, lexme);
						case "-":
							return GOTO_MATCHED(MINUS, lexme);
						default:
							throw new AssertionError();
					}
				case END_OF_STREAM:
					return GOTO_FAILED;
				default:
					throw new AssertionError();
			}
		}
	},
	MATCHED {
		@Override
		public TransitionOutput transit(ScanContext context) {
			throw new IllegalStateException("at final state");
		}
	},
	FAILED{
		@Override
		public TransitionOutput transit(ScanContext context) {
			throw new IllegalStateException("at final state");
		}
	},
	EOS {
		@Override
		public TransitionOutput transit(ScanContext context) {
			return GOTO_EOS;
		}
	};
	
	abstract TransitionOutput transit(ScanContext context);
}
