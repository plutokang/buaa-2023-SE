//EqExp → RelExp | EqExp ('==' | '!=') RelExp
package Node;

import Main.Token;

import java.util.ArrayList;

public class EqExp {
    ArrayList<Token> siganls = new ArrayList<>();
    ArrayList<RelExp> relExps = new ArrayList<>();
    public EqExp(ArrayList<RelExp> relExps,ArrayList<Token> siganls)
    {
        this.siganls = siganls;
        this.relExps = relExps;
    }
}
