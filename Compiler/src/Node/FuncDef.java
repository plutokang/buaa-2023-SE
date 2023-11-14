//  FuncDef → FuncType Ident '(' [FuncFParams] ')' Block
package Node;
import Main.Token;
public class FuncDef {
    public FuncType functype = null;
    public Token Ident = null;
    public Token leftBrace = null;
    public Token rightBrace = null;
    public FuncFParams funcFParams = null;
    public Block block;
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
