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
	//���۷� �ٲ�
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
				//���� �Է½� 13 �� 10 �ΰ��� ���� �Է��� ��.
				//�Ʒ��� ���ǹ��� �ΰ��� �Է¿� ����  13 10�� ���������� �迭�ȿ� ���� �迭�� 0 1 �� �ε����� 13 10�� ��� ������
				//�Է� �޴� ���� ���� �Ҽ� �ֵ��� ch ���� -1�� �ٲ��ִ� ���̴�.
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
