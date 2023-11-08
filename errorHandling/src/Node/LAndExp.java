//LAndExp → EqExp | LAndExp '&&' EqExp
package Node;

import Main.Token;

import java.util.ArrayList;

public class LAndExp {
    ArrayList<Token> signals = new ArrayList<>();
    ArrayList<EqExp> exps = new ArrayList<>();
    public  LAndExp(ArrayList<EqExp> exps,ArrayList<Token> signals)
    {
        this.exps = exps;
        this.signals = signals;
    }
}
