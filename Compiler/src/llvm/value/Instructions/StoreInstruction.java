package llvm.value.Instructions;

import llvm.type.Type;
import llvm.type.VoidType;
import llvm.value.Value;
import llvm.value.VarValue;
import llvm.Module;

public class StoreInstruction extends Instruction{
    public Value fromValue = null;
    public Value toValue = null;
    public StoreInstruction(Value fromValue,Value toValue) {
        super(null, new VoidType());
        this.fromValue = fromValue;
        this.toValue = toValue;
    }
    public void print()
    {
        System.out.print("store i32 ");
        this.fromValue.print();
        System.out.print(", i32* ");
        this.toValue.print();
        System.out.print("\n");
    }
    public void printToFile()
    {
        Module module = Module.getModule();
        module.printForChile("store i32 ");
        this.fromValue.printToFile();
        module.printForChile(", i32* ");
        this.toValue.printToFile();
        module.printForChile("\n");
    }
}
