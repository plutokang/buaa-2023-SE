package Node;

import Main.Token;

import java.util.ArrayList;

//VarDecl → BType VarDef { ',' VarDef } ';'
public class VarDecl {
    public BType btype;
    public ArrayList<VarDef> varDefs;
    public ArrayList<Token> commaList;
    public Token semicolon;
    public VarDecl(BType btype, ArrayList<VarDef> varDefs, ArrayList<Token> commaList, Token semicolon)
    {
        this.btype = btype;
        this.varDefs = varDefs;
        this.commaList = commaList;
        this.semicolon = semicolon;
    }
}
