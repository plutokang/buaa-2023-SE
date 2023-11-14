//BlockItem → Decl | Stmt
package Node;

public class BlockItem {
    public Decl decl = null;
    public Stmt stmt = null;
    public BlockItem(Decl decl)
    {
        this.decl = decl;
    }
    public BlockItem(Stmt stmt)
    {
        this.stmt = stmt;
    }


}
