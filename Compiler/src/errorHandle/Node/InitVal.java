//InitVal → Exp | '{' [ InitVal { ',' InitVal } ] '}'
package Node;
import Main.Token;

import java.util.ArrayList;

public class InitVal {
    Exp exp;
    Token leftBrace;
    Token rightBrace;
    ArrayList<InitVal> initValArray;
    ArrayList<Token> commaArray;
    public InitVal(Exp exp)
    {
        this.exp = exp;
    }
    public InitVal(Token leftBrace, Token rightBrace, ArrayList<InitVal> initValArray, ArrayList<Token> commaArray)
    {
        this.leftBrace = leftBrace;
        this.rightBrace = rightBrace;
        this.initValArray = initValArray;
        this.commaArray = commaArray;
    }
}
