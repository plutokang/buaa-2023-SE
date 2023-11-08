//RelExp → AddExp | RelExp ('<' | '>' | '<=' | '>=') AddExp
package Node;

import Main.Token;

import java.util.ArrayList;

public class RelExp {
    ArrayList<AddExp> addExps = new ArrayList<>();
    ArrayList<Token> signals = new ArrayList<>();

    public RelExp(ArrayList<AddExp> addExps, ArrayList<Token> signals) {
        this.addExps = addExps;
        this.signals = signals;
    }
}
