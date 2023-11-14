package llvm.type;

public class VarType extends Type{
    public int width = 32;
    public boolean isConst = false;
    public VarType(int width, boolean isConst)
    {
        this.width = width;
        this.isConst = isConst;
    }
}
