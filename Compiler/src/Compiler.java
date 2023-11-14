import Main.*;
import llvm.Module;

import java.io.IOException;
import java.util.ArrayList;
import llvm.*;

public class Compiler {
    public Generator generator;
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
//        parser.print();
        Generator generator = new Generator(parser.getCompUnit());
        Module module = Module.getModule();
        module.print();
        module.printToFile();
        module.closeWriter();
    }
}
