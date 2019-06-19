package lexer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

class CharStream {
	private final Reader reader;
	private Character cache;
	int [] end_arr = new int[2];
	//버퍼로 바꿈
	static CharStream from(InputStreamReader input) throws IOException {
		return new CharStream(new BufferedReader(new InputStreamReader(System.in)));
	}
	
	CharStream(Reader reader) {
		this.reader = reader;
		this.cache = null;
	}
	
	Char nextChar() {
		if ( cache != null ) {
			char ch = cache;
			cache = null;
			
			return Char.of(ch);
		}
		else {
			try {
				int ch = reader.read();
				//엔터 입력시 13 과 10 두개의 값이 입력이 됨.
				//아래의 조건문은 두개의 입력에 대해  13 10이 순차적으로 배열안에 들어가서 배열의 0 1 의 인덱스가 13 10일 경우 강제로
				//입력 받는 것을 종료 할수 있도록 ch 값을 -1로 바꿔주는 것이다.
				if(ch==13) {
					end_arr[0]=ch;
					
					
				}else if(ch==10){
					end_arr[1] = ch;
				}
				if(end_arr[0]==13 ||end_arr[1]==10) {
						ch=-1;
				}
				if ( ch == -1 ) {
					return Char.end();
				}
				else {
					
					return Char.of((char)ch);
				}
				
			}
			catch ( IOException e ) {
				throw new ScannerException("" + e);
			}
		}
		
	}
	
	void pushBack(char ch) {
		cache = ch;
	}
}
