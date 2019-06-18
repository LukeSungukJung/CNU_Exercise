package lexer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Scanner {
    // return tokens as an Iterator
    public static Iterator<Token> scan(InputStreamReader input) throws IOException {
        ScanContext context = new ScanContext(input);
        return new TokenIterator(context);
    }

    // return tokens as a Stream 
    public static Stream<Token> stream(InputStreamReader file) throws IOException {
        Iterator<Token> tokens = scan(file);
        return StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(tokens, Spliterator.ORDERED), false);
    }
}