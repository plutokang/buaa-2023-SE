package Node;
//ConstDef → Ident { '[' ConstExp ']' } '=' ConstInitVal
import Main.Token;

import java.util.ArrayList;

public class ConstDef {
    Token Ident = null;
    ArrayList<ConstExp> constExpList = null;
    Token eqSignal = null;
    ConstInitVal constInitVal = null;
    ArrayList<Token> leftBraces = null;
    ArrayList<Token> rightBraces = null;
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
