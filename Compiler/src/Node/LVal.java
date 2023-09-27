package Node;

import Main.Token;

import java.util.ArrayList;

public class LVal {
    Token Ident = null;
    ArrayList<Token>leftBraceArray = null;
    ArrayList<Token>rightBraceArray = null;
    ArrayList<Exp>expArray = null;
    public LVal(Token Ident, ArrayList<Token> leftBraceArray, ArrayList<Token> rightBraceArray, ArrayList<Exp> expArray)
    {
        this.Ident = Ident;
        this.leftBraceArray = leftBraceArray;
        this.rightBraceArray = rightBraceArray;
        this.expArray = expArray;
    }
}
