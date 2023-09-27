//MulExp → UnaryExp | MulExp ('*' | '/' | '%') UnaryExp
package Node;

import Main.Token;

import java.util.ArrayList;

public class MulExp {

    ArrayList<UnaryExp> unaryExps = new ArrayList<>();
    ArrayList<Token>signals = new ArrayList<>();
    public MulExp(ArrayList<UnaryExp> unaryExps,ArrayList<Token>signals)
    {
        this.unaryExps = unaryExps;
        this.signals = signals;
    }
}
