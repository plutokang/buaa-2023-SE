package llvm.value;

import llvm.Module;
import llvm.type.FunctionType;
import llvm.type.LabelType;
import llvm.type.Type;
import llvm.type.VarType;
import llvm.value.Instructions.Instruction;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class FunctionValue extends Value {
    public ArrayList<Value> paramsList = new ArrayList<>();
    public ArrayList<BasicBlock> basicBlockList = new ArrayList<>();
    public boolean isVoid = false;
    public boolean isLibrary = false;
    public int registNum = 0;

    public FunctionValue(String name, FunctionType type, boolean isVoid, boolean isLibrary) {
        super(name, type);
        this.isVoid = isVoid;
        this.isLibrary = isLibrary;
    }

    public void addToParams(Value param) {
        this.paramsList.add(param);
        ((FunctionType) (type)).paramsType.add(param.type);
    }

    public void addToBasicBlockList(BasicBlock basicBlock) {
        this.basicBlockList.add(basicBlock);
    }

    public void print() {
        if (!this.isLibrary) {
            System.out.print("define dso_local ");
            if (this.isVoid)
                System.out.print("void ");
            else
                System.out.print("i32 ");
            System.out.print("@" + super.name + "(");
            for (Value value : this.paramsList) {
                System.out.print("i" + ((VarValue) value).width + " ");
                value.print();
                System.out.print(",");
            }
            if (!this.paramsList.isEmpty()) {
                System.out.print("\b");   // 输出退格字符，应该将 A 撤回
                System.out.flush();
            }
            System.out.println(") {");
            for (BasicBlock basicBlock : this.basicBlockList) {
                basicBlock.print();
            }
            System.out.println("}");
        }
    }

    public void printToFile() {
        Module module = Module.getModule();
        if (!this.isLibrary) {
            module.printForChile("define dso_local ");
            if (this.isVoid)
                module.printForChile("void ");
            else
                module.printForChile("i32 ");
            module.printForChile("@" + super.name + "(");
            for (int i = 0; i < this.paramsList.size(); i++) {
                Value value = this.paramsList.get(i);
                module.printForChile("i" + ((VarValue) value).width + " ");
                value.printToFile();
                if (i != this.paramsList.size() - 1)
                    module.printForChile(",");
            }
            module.printForChile(") {\n");
            for (BasicBlock basicBlock : this.basicBlockList) {
                basicBlock.printToFile();
            }
            module.printForChile("}\n");
        }
    }
//        declare i32 @getint()
//        declare void @putint(i32)
//        declare void @putch(i32)
//        declare void @putstr(i8*)
    public void printGlobalToConsole()
    {
        System.out.print("declare " + ((isVoid) ? "void" : "i32") + " @" + name + "(" + ((isVoid) ? "i32" : "") + ")\n");
    }
    public void printGlobalToFIle()
    {
        Module module = Module.getModule();
       module.printForChile("declare " + ((isVoid) ? "void" : "i32") + " @" + name + "(" + ((isVoid) ? "i32" : "") + ")\n");
    }

}
