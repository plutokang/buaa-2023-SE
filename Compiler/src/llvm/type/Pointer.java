package llvm.type;

public class Pointer extends Type{
    int width = 32;
    public Pointer(int width)
    {
        this.width = width;
    }
}
