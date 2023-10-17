import Main.*;
import java.io.IOException;
import java.util.ArrayList;

public class Compiler {
    public static void main(String[] args)
    {
        ArrayList<Token> tokenList = new ArrayList<>();
        try {
            MyLexer lexer = new MyLexer();
            tokenList = lexer.getTokenList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Parser parser = new Parser(tokenList);
        parser.print();
        parser.printError();
//        TestSame test = new TestSame();
    }
}
