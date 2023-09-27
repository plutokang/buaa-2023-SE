//FuncFParam → BType Ident ['[' ']' { '[' ConstExp ']' }]
package Node;

import Main.Token;

public class FuncFParam {
    BType btype;
    Token Ident;
    Token firstLeftBrace;
    Token firstRightBrace;
    Token secondLeftBrace;
    Token secondRightBrace;
    ConstExp constExp;
    public FuncFParam(BType btype, Token Ident, Token firstLeftBrace, Token firstRightBrace, Token secondLeftBrace, Token secondRightBrace, ConstExp constExp)
    {
        this.btype = btype;
        this.Ident = Ident;
        this.firstLeftBrace = firstLeftBrace;
        this.firstRightBrace = firstRightBrace;
        this.secondLeftBrace = secondLeftBrace;
        this.secondRightBrace = secondRightBrace;
        this.constExp = constExp;
    }
}
