package llvm.value;

import llvm.Module;
import llvm.type.LabelType;
import llvm.type.Type;
import llvm.value.Instructions.Instruction;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class BasicBlock extends Value {
    public ArrayList<Instruction> instructionList = new ArrayList<>();

    public BasicBlock(int num) {
        super(null, new LabelType(num));
    }

    public void addToInstruction(Instruction instruction) {
        this.instructionList.add(instruction);
    }

    public void print() {
//        System.out.println(((LabelType) (type)).num + ":");
        for (Instruction instruction : this.instructionList)
            instruction.print();
    }

    public void printToFile() {
        Module module = Module.getModule();
//        module.printForChile(((LabelType) (type)).num + ":\n");
        for (Instruction instruction : this.instructionList)
            instruction.printToFile();
    }
}
