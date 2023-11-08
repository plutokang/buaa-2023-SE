// UnaryExp → PrimaryExp | Ident '(' [FuncRParams] ')'| UnaryOp UnaryExp
package Node;

import Main.Token;

public class UnaryExp {
    public PrimaryExp primaryExp = null;
    public Token Ident = null;
    Token leftBrace = null;
    FuncRParams funcRParams = null;
    Token rightBrace = null;
    public UnaryOp unaryOp = null;
    public UnaryExp unaryExp = null;
    public UnaryExp(PrimaryExp primaryExp)
    {
        this.primaryExp = primaryExp;
    }
    public UnaryExp(Token Indet, Token leftBrace, Token rightBrace, FuncRParams funcRParams)
    {
        this.Ident = Indet;
        this.leftBrace = leftBrace;
        this.rightBrace = rightBrace;
        this.funcRParams = funcRParams;
    }
    public UnaryExp(UnaryOp unaryOp,UnaryExp unaryExp)
    {
        this.unaryOp = unaryOp;
        this.unaryExp = unaryExp;
    }
}
