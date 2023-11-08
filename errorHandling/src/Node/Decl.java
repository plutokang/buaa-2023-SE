// Decl → ConstDecl | VarDecl
package Node;

public class Decl {
    ConstDecl constDecl = null;
    VarDecl varDecl = null;
    public Decl(ConstDecl constDecl)
    {
        this.constDecl = constDecl;
    }
    public Decl(VarDecl varDecl)
    {
        this.varDecl = varDecl;
    }
}
