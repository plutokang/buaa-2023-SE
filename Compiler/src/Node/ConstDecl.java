// ConstDecl → 'const' BType ConstDef { ',' ConstDef } ';'
package Node;
import Main.Token;

import java.util.ArrayList;

public class ConstDecl {
     public Token constToken = null;
     public BType btype = null;
     public ArrayList<ConstDef> constDefsList = null;
     public ArrayList<Token> commaArray = null;
     public Token semiComma = null;
     public ConstDecl(Token constToken, BType btype, ArrayList<ConstDef> constDefsList, ArrayList<Token> commaArray,Token semiComma)
     {
          this.constToken = constToken;
          this.btype = btype;
          this.constDefsList = constDefsList;
          this.commaArray = commaArray;
          this.semiComma = semiComma;
     }
}
