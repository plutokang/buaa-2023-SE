package Node;

import Main.Token;

import java.util.ArrayList;

//ConstInitVal → ConstExp | '{' [ ConstInitVal { ',' ConstInitVal } ] '}'
public class ConstInitVal {
    public ConstExp constExp;
    public Token lBrace = null;
    public ArrayList<ConstInitVal> constInitVals = null;
    public ArrayList<Token> commaList = null;
    public Token rBrace = null;
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
