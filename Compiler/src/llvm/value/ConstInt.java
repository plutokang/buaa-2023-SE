package llvm.value;

import llvm.Module;
import llvm.type.IntegerType;
import llvm.type.Type;

public class ConstInt extends Const{

    public ConstInt(int value) {
        super(null,new IntegerType(32));
        this.value = value;
    }
    public void print()
    {
        System.out.print(value);
    }
    public void printToFile()
    {
        Module module = Module.getModule();
        module.printForChile(String.valueOf(value));
    }
}
