package Node;

import Main.Token;

import java.util.ArrayList;

public class PrimaryExp {
    public Token leftBrace = null;
    public Token rightBrace = null;
    public Exp exp = null;
    public LVal lVal = null;
    public Number number = null;
    public PrimaryExp(Token leftBrace,Token rightBrace,Exp exp)
    {
        this.leftBrace = leftBrace; 
        this.rightBrace = rightBrace;
        this.exp = exp;
    }
    public PrimaryExp(LVal lVal)
    {
        this.lVal = lVal;
    }
    public PrimaryExp(Number number)
    {
        this.number = number;
    }
}

