// ConstDecl → 'const' BType ConstDef { ',' ConstDef } ';'
package Node;
import Main.Token;

import java.util.ArrayList;

public class ConstDecl {
     Token constToken = null;
     BType btype = null;
     ArrayList<ConstDef> constDefsList = null;
     ArrayList<Token> commaArray = null;
     Token semiComma = null;
     public ConstDecl(Token constToken, BType btype, ArrayList<ConstDef> constDefsList, ArrayList<Token> commaArray,Token semiComma)
     {
          this.constToken = constToken;
          this.btype = btype;
          this.constDefsList = constDefsList;
          this.commaArray = commaArray;
          this.semiComma = semiComma;
     }
}
