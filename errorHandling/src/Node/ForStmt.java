//ForStmt → LVal '=' Exp
package Node;

import Main.Token;

public class ForStmt {
    LVal lVal = null;
    Token eq = null;
    Exp exp = null;
    public ForStmt(LVal lVal, Token eq, Exp exp)
    {
        this.lVal = lVal;
        this.eq = eq;
        this.exp = exp;
    }
}
