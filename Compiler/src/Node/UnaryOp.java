//UnaryOp → '+' | '−' | '!'
package Node;

import Main.Token;

public class UnaryOp {
    Token Op = null;
    public UnaryOp(Token op)
    {
        this.Op = op;
    }
}
