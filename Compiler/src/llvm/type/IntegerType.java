package llvm.type;

public class IntegerType extends Type{
    //IntegerType用来记录整数，主要用来记录整数类型，方便之后bool型变量和其他变量类型
    public int width = 32;
    public IntegerType(int width)
    {
        this.width = width;
    }
}
