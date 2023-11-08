//BlockItem → Decl | Stmt
package Node;

public class BlockItem {
    Decl decl = null;
    Stmt stmt = null;
    public BlockItem(Decl decl)
    {
        this.decl = decl;
    }
    public BlockItem(Stmt stmt)
    {
        this.stmt = stmt;
    }


}
