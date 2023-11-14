package llvm.value.Instructions;

import llvm.type.LabelType;
import llvm.type.Type;
import llvm.type.VoidType;
import llvm.value.VarValue;
import llvm.Module;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class AllocInstruction extends Instruction {
    VarValue usedVar = null;
    public AllocInstruction(VarValue var) {
        super(null, new VoidType());
        this.usedVar = var;
    }
    public void print()
    {
        usedVar.print();
        System.out.println(" = alloca i" + usedVar.width);
    }
    public void printToFile()
    {
        Module module = Module.getModule();
        usedVar.printToFile();
        module.printForChile(" = alloca i" + usedVar.width + "\n");
    }
}
