package Node;
import Main.Token;

import java.util.ArrayList;

//  VarDef → Ident { '[' ConstExp ']' }| Ident { '[' ConstExp ']' } '=' InitVal
public class VarDef {
    public Token Ident;
    public ArrayList<Token> leftBraceArray;
    public ArrayList<ConstExp> constExpArray;
    public ArrayList<Token>rightBraceArray;
    public Token eqSignal;
    public InitVal initVal;
    public VarDef(Token ident, ArrayList<Token> leftBraceArray, ArrayList<ConstExp> constExpArray, ArrayList<Token> rightBraceArray)
    {
        this.Ident = ident;
        this.leftBraceArray = leftBraceArray;
        this.constExpArray = constExpArray;
        this.rightBraceArray = rightBraceArray;
    }
    public VarDef(Token ident, ArrayList<Token> leftBraceArray, ArrayList<ConstExp> constExpArray, ArrayList<Token> rightBraceArray, Token eqSignal, InitVal initVal)
    {
        this.Ident = ident;
        this.leftBraceArray = leftBraceArray;
        this.constExpArray = constExpArray;
        this.rightBraceArray = rightBraceArray;
        this.eqSignal = eqSignal;
        this.initVal = initVal;
    }
}
