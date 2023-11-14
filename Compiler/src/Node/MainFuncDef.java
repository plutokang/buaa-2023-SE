//MainFuncDef → 'int' 'main' '(' ')' Block
package Node;
import Main.Token;
public class MainFuncDef {
    public Token intSignal;
    public Token mainSignal;
    public Token leftBrace;
    public Token rightBrace;
    public Block block;
    public MainFuncDef(Token intSignal, Token mainSignal, Token leftBrace, Token rightBrace, Block block)
    {
        this.intSignal = intSignal;
        this.mainSignal = mainSignal;
        this.leftBrace = leftBrace;
        this.rightBrace = rightBrace;
        this.block = block;
    }
}
