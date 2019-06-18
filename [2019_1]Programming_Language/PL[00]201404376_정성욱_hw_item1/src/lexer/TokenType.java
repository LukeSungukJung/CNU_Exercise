package lexer;
public enum TokenType {
	INT,
	ID, 
	TRUE, FALSE, NOT,
	PLUS, MINUS, TIMES, DIV,   //special chractor
	LT, GT, EQ, APOSTROPHE,    //special chractor
	L_PAREN, R_PAREN, QUESTION, //special chractor
	DEFINE, LAMBDA, COND, QUOTE,
	CAR, CDR, CONS,
	ATOM_Q, NULL_Q, EQ_Q;

	// Í∞ÅÍ∞Å?ùò SpecialCharactor?óê ???ï¥?Ñú ?ïåÎßûÏ? enum type?ùÑ Î∞òÌôò?ï¥Ï£ºÎ©¥ ?êú?ã§.
	static TokenType fromSpecialCharactor(char ch) {
		switch ( ch ) {
			case '+':
				return PLUS;
			case '-':
				return MINUS;
			case '*':
				return TIMES;
			case '/':
				return DIV;
			case '<':
				return LT;
			case '=':
				return EQ;
			case '>':
				return GT;
			case '\'':
				return APOSTROPHE;
			case '(':
				return L_PAREN;
			case ')':
				return R_PAREN;
			case '?':
				return QUESTION;

			default:
				throw new IllegalArgumentException("unregistered char: " + ch);
		}
	}
}
