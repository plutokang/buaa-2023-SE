//  FuncDef → FuncType Ident '(' [FuncFParams] ')' Block
package Node;
import Main.Token;
public class FuncDef {
    FuncType functype = null;
    Token Ident = null;
    Token leftBrace = null;
    Token rightBrace = null;
    FuncFParams funcFParams = null;
    Block block;
    public FuncDef(FuncType functype, Token Ident, Token leftBrace, Token rightBrace, FuncFParams funcFParams, Block block)
    {
        this.functype = functype;
        this.Ident = Ident;
        this.leftBrace = leftBrace;
        this.rightBrace = rightBrace;
        this.funcFParams = funcFParams;
        this.block = block;
    }
}
