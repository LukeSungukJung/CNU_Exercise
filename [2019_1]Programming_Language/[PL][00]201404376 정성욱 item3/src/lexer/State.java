package lexer;
enum State {
	START {
		@Override
		public TransitionOutput transit(ScanContext context) {
			Char ch = context.getCharStream().nextChar();
			char v = ch.value();
			switch ( ch.type() ) {
				case LETTER:
					context.append(v);
					return TransitionOutput.GOTO_ACCEPT_ID;
				case DIGIT:
					context.append(v);
					return TransitionOutput.GOTO_ACCEPT_INT;
				case SPECIAL_CHAR: //special charactor媛� �뱾�뼱�삩 寃쎌슦

					if ( v == '-' || v == '+' ) { // '-', '+'�씤 寃쎌슦�뿉�뒗 遺��샇�씠湲� �븣臾몄뿉 GOTO_SIGN�쑝濡� 蹂대궡二쇰㈃ �맂�떎.
						context.append(v);
						return TransitionOutput.GOTO_SIGN;
					}
					else if ( v == '#' ) {  // '#'�� boolean 媛믪쓣 �굹���궡湲� �쐞�빐�꽌 �궗�슜�릺誘�濡� boolean�쑝濡� �쟾�씠�븷 �닔 �엳寃� GOTO_SHARP�쑝濡� 蹂대궡以��떎.
						context.append(v);
						return TransitionOutput.GOTO_SHARP;
					}
					else { //洹몄쇅�쓽 寃쎌슦�뒗 SpecialCharactor瑜� 泥섎━�븯硫� �릺�뒗�뜲 TokenType�쓽 fromSpecialCharactor瑜� �씠�슜�빐�꽌 GOTO_MATCHED濡� 蹂대궡硫� �맂�떎.
						context.append(v);
						return TransitionOutput.GOTO_MATCHED(TokenType.fromSpecialCharactor(v), context.getLexime());
					}

				case WS:
					return TransitionOutput.GOTO_START;
				case END_OF_STREAM:
					return TransitionOutput.GOTO_EOS;
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
					return TransitionOutput.GOTO_ACCEPT_ID;
				case SPECIAL_CHAR:
					return TransitionOutput.GOTO_FAILED;
				case WS:
				case END_OF_STREAM:
					return TransitionOutput.GOTO_MATCHED(Token.ofName(context.getLexime()));
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
					return TransitionOutput.GOTO_FAILED;
				case DIGIT:
					context.append(ch.value());
					return TransitionOutput.GOTO_ACCEPT_INT;
				case SPECIAL_CHAR:
					return TransitionOutput.GOTO_FAILED;
				case WS:
					return TransitionOutput.GOTO_MATCHED(TokenType.INT, context.getLexime());
				case END_OF_STREAM:
					return TransitionOutput.GOTO_FAILED;
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
							return TransitionOutput.GOTO_MATCHED(TokenType.TRUE, context.getLexime());
						case 'F':
							context.append(v);
							return TransitionOutput.GOTO_MATCHED(TokenType.FALSE, context.getLexime());
						default:
							return TransitionOutput.GOTO_FAILED;
					}
				default:
					return TransitionOutput.GOTO_FAILED;
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
					return TransitionOutput.GOTO_FAILED;
				case DIGIT:
					context.append(v);
					return TransitionOutput.GOTO_ACCEPT_INT;
				case SPECIAL_CHAR:
					return TransitionOutput.GOTO_FAILED;
				case WS:
					String lexme = context.getLexime();
					switch ( lexme ) {
						case "+":
							return TransitionOutput.GOTO_MATCHED(TokenType.PLUS, lexme);
						case "-":
							return TransitionOutput.GOTO_MATCHED(TokenType.MINUS, lexme);
						default:
							throw new AssertionError();
					}
				case END_OF_STREAM:
					return TransitionOutput.GOTO_FAILED;
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
			return TransitionOutput.GOTO_EOS;
		}
	};
	
	abstract TransitionOutput 	transit(ScanContext context);
}
