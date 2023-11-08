//MainFuncDef → 'int' 'main' '(' ')' Block
package Node;
import Main.Token;
public class MainFuncDef {
    Token intSignal;
    Token mainSignal;
    Token leftBrace;
    Token rightBrace;
    Block block;
    public MainFuncDef(Token intSignal, Token mainSignal, Token leftBrace, Token rightBrace, Block block)
    {
        this.intSignal = intSignal;
        this.mainSignal = mainSignal;
        this.leftBrace = leftBrace;
        this.rightBrace = rightBrace;
        this.block = block;
    }
}
