//ConstExp → AddExp
package Node;

public class ConstExp {
    public AddExp addExp = null;
    public ConstExp(AddExp addExp)
    {
        this.addExp = addExp;
    }
}
