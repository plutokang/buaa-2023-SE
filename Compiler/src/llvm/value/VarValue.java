package llvm.value;

import llvm.Module;
import llvm.type.LabelType;
import llvm.type.Pointer;
import llvm.type.Type;
import llvm.type.VarType;
import llvm.value.Instructions.Instruction;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class VarValue extends Value {
    public int registerNum = 0;
    public boolean isConst = false;
    public boolean isGlobal = false;
    public int width = 32;
    public boolean isChanged = false;

    public VarValue(String name, int registerNum, boolean isConst, boolean isGlobal, int width) {
        super(name, new VarType(width, isConst));
        this.isConst = isConst;
        this.registerNum = registerNum;
        this.isGlobal = isGlobal;
        this.width = width;
    }

    public void print() {
        if (!isGlobal)
            System.out.print("%");
        else
            System.out.print("@");
        System.out.print(this.registerNum);
    }

    public void printToFile() {
        Module module = Module.getModule();
        if (!isGlobal)
            module.printForChile("%" + this.registerNum);
        else
            module.printForChile("@" + this.name);
    }

    public void printToGlobalConsole() {
        System.out.print("@" + name + " = dso_local " + (isConst ? "constant " : "global") + " i32 " + value + "\n");
    }

    public void printToGlobalFile() {
        Module module = Module.getModule();
        module.printForChile("@" + name + " = dso_local " + (isConst ? "constant " : "global") + " i32 " + value + "\n");
    }
}
