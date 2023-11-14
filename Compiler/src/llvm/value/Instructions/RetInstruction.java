package llvm.value.Instructions;

import llvm.Module;
import llvm.type.Type;
import llvm.type.VoidType;
import llvm.value.Value;

public class RetInstruction extends Instruction{
    public Value value = null;
    public boolean isVoid = false;

    public RetInstruction(Value value) {
        super(null, new VoidType());
        this.value = value;
        this.isVoid = false;
    }
    public RetInstruction()
    {
        super(null, new VoidType());
        this.isVoid = true;
    }
    public void print()
    {
        if(isVoid)
            System.out.print("ret void\n");
        else
        {
            System.out.print("ret i32 ");
            value.print();
            System.out.print("\n");
        }
    }
    public void printToFile()
    {
        Module module = Module.getModule();
        if(isVoid)
            module.printForChile("ret void\n");
        else
        {
            module.printForChile("ret i32 ");
            value.printToFile();
            module.printForChile("\n");
        }
    }

}
