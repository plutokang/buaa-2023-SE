package llvm.value.Instructions;

import llvm.type.Type;
import llvm.type.VoidType;
import llvm.value.Value;
import llvm.Module;
public class LoadInstruction extends Instruction{
    public Value fromValue = null;
    public Value toValue = null;
    public LoadInstruction(Value fromValue,Value toValue) {
        super(null, new VoidType());
        this.fromValue = fromValue;
        this.toValue = toValue;
    }
    public void print()
    {
        toValue.print();
        System.out.print(" = load i32 ,i32* ");
        fromValue.print();
        System.out.print("\n");
    }
    public void printToFile()
    {
        Module module = Module.getModule();
        toValue.printToFile();
        module.printForChile(" = load i32 ,i32* ");
        fromValue.printToFile();
        module.printForChile("\n");
    }
}
