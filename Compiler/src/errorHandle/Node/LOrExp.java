// LOrExp → LAndExp | LOrExp '||' LAndExp
package Node;

import Main.Token;

import java.util.ArrayList;

public class LOrExp {
    ArrayList<Token> signals = new ArrayList<>();
    ArrayList<LAndExp> exps = new ArrayList<>();
    public  LOrExp(ArrayList<LAndExp> exps,ArrayList<Token> signals)
    {
        this.exps = exps;
        this.signals = signals;
    }
}
