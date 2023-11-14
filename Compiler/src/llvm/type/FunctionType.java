package llvm.type;

import llvm.value.Value;

import java.util.ArrayList;

public class FunctionType extends Type{
    public boolean isVoid = false;
    public ArrayList<Type> paramsType = new ArrayList<>();
    public FunctionType(boolean isVoid)
    {
        this.isVoid = isVoid;
    }
}
