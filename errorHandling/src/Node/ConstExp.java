//ConstExp → AddExp
package Node;

public class ConstExp {
    AddExp addExp = null;
    public ConstExp(AddExp addExp)
    {
        this.addExp = addExp;
    }
}
