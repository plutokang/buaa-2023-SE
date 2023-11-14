// Decl → ConstDecl | VarDecl
package Node;

public class Decl {
    public ConstDecl constDecl = null;
    public VarDecl varDecl = null;
    public Decl(ConstDecl constDecl)
    {
        this.constDecl = constDecl;
    }
    public Decl(VarDecl varDecl)
    {
        this.varDecl = varDecl;
    }
}
