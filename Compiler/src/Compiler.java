import java.io.IOException;

public class Compiler {
    public static void main(String[] args)
    {
        try {
            MyLexer lexer = new MyLexer();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
