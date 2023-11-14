//AddExp → MulExp | AddExp ('+' | '−') MulExp
package Node;

import Main.Token;

import java.util.ArrayList;

public class AddExp {
    public ArrayList<Token> signals = new ArrayList<>();
    public ArrayList<MulExp> mulExps = new ArrayList<>();

    public AddExp(ArrayList<MulExp> mulExps, ArrayList<Token> signals)
    {
        this.mulExps = mulExps;
        this.signals = signals;
    }
}
