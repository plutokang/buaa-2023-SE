//  FuncFParams → FuncFParam { ',' FuncFParam }
package Node;

import Main.Token;

import java.util.ArrayList;

public class FuncFParams {
    public ArrayList<Token> commaArray;
    public ArrayList<FuncFParam> funcFParamArray;
    public FuncFParams(ArrayList<Token> commaArray, ArrayList<FuncFParam> funcFParamArray)
    {
        this.commaArray = commaArray;
        this.funcFParamArray = funcFParamArray;
    }
}
