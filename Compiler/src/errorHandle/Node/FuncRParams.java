
package Node;


import Main.Token;

import java.util.ArrayList;

public class FuncRParams {
    public ArrayList<Exp> expArray = null;
    ArrayList<Token> commaArray = null;
    public FuncRParams(ArrayList<Exp> expArray,ArrayList<Token> commaArray)
    {
        this.expArray = expArray;
        this.commaArray = commaArray;
    }


}
