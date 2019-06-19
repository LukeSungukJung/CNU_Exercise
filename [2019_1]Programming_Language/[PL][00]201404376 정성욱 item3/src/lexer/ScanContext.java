package lexer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;


class ScanContext {
	private final CharStream input;
	private StringBuilder builder;
	
	ScanContext(InputStreamReader input) throws IOException {
		this.input = CharStream.from(input);
		this.builder = new StringBuilder();
	}
	
	CharStream getCharStream() {
		return input;
	}
	
	String getLexime() {
		String str = builder.toString();
		builder.setLength(0);
		return str;
	}
	
	void append(char ch) {
		builder.append(ch);
	}
}
