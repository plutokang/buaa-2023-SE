package Node;
//ConstDef → Ident { '[' ConstExp ']' } '=' ConstInitVal
import Main.Token;

import java.util.ArrayList;

public class ConstDef {
    public Token Ident = null;
    public ArrayList<ConstExp> constExpList = null;
    public Token eqSignal = null;
    public ConstInitVal constInitVal = null;
    public ArrayList<Token> leftBraces = null;
    public ArrayList<Token> rightBraces = null;
    public ConstDef(Token Ident, ArrayList<ConstExp> constExpList, Token eqSignal, ConstInitVal constInitVal, ArrayList<Token> leftBraces, ArrayList<Token> rightBraces)
    {
        this.Ident = Ident;
        this.constExpList = constExpList;
        this.eqSignal = eqSignal;
        this.constInitVal = constInitVal;
        this.leftBraces = leftBraces;
        this.rightBraces = rightBraces;
    }
}
