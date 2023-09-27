package Node;

import Main.Token;

import java.util.ArrayList;

//ConstInitVal → ConstExp | '{' [ ConstInitVal { ',' ConstInitVal } ] '}'
public class ConstInitVal {
    ConstExp constExp;
    Token lBrace = null;
    ArrayList<ConstInitVal> constInitVals = null;
    ArrayList<Token> commaList = null;
    Token rBrace = null;
    public ConstInitVal(ConstExp constExp)
    {
        this.constExp = constExp;
    }
    public ConstInitVal(Token lBrace, ArrayList<ConstInitVal> constInitVals, Token rBrace, ArrayList<Token> commaList)
    {
        this.lBrace = lBrace;
        this.constInitVals = constInitVals;
        this.rBrace = rBrace;
        this.commaList = commaList;
    }
}
